package com.moshoujingli.arena.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

import com.moshoujingli.arena.R;
import com.moshoujingli.arena.utils.opengles.GlShader;
import com.moshoujingli.arena.utils.opengles.GlUtil;
import com.moshoujingli.arena.utils.LogHelper;
import com.moshoujingli.arena.utils.opengles.RendererCommon;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by bixiaopeng on 2017/2/23.
 */

public class MediaCaptureActivity extends BaseActivity implements View.OnTouchListener, GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    public static final int MAX_HISTORY = 60;
    private GLSurfaceView previewSurface;
    private Camera c;
    private SurfaceTexture surfaceTexture;
    private int cameraTextureId;
    private int height;
    private int width;
    private HashMap<String, Shader> shaderMap;
    private float[] transMtx;
    private int heartTextureId;
    private Bitmap heartBitmap;
    private LinkedList<Point> posQueue;
    private float[] historyPoints;
    private int texMatrixLocation;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        c.stopPreview();
        c.release();
    }

    @Override
    protected void setOnClick() {

    }

    @Override
    protected void initView() {
        previewSurface = (GLSurfaceView) findViewById(R.id.preview_area);
        previewSurface.setOnTouchListener(this);
        previewSurface.setEGLContextClientVersion(2);
        previewSurface.setRenderer(this);
        previewSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        transMtx = new float[16];
        heartBitmap = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.heart));
        historyPoints = new float[MAX_HISTORY * 3];

    }

    @Override
    protected void findView() {
        setContentView(R.layout.activity_media_capture);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
//        motionEvent.
        String TAG = "MotionEvent";
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            LogHelper.i(TAG, motionEvent.getHistorySize() + " ");
            Point a = new Point();
            a.x = (int) motionEvent.getX();
            a.y = (int) motionEvent.getY();
            synchronized (posQueue){
                posQueue.addFirst(a);
                if (posQueue.size() > MAX_HISTORY) {
                    posQueue.removeLast();
                }
            }
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            posQueue = new LinkedList<>();
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            synchronized (posQueue) {
                posQueue.clear();
            }
        }
        return true;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        cameraTextureId = GlUtil.generateTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
        heartTextureId = GlUtil.createTexture(new Bitmap[]{heartBitmap})[0];

        surfaceTexture = new SurfaceTexture(cameraTextureId);
        surfaceTexture.setOnFrameAvailableListener(this);
        try {
            c = Camera.open(1);
            c.setPreviewTexture(surfaceTexture);
            c.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        surfaceTexture.updateTexImage();
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0,0,0,1);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        prepareShader();
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTextureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glViewport(0, 0, width, height);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);

        Shader shader = shaderMap.get(OES_FRAGMENT_SHADER_STRING);
        shader.glShader.stopProgram();

        int pointCount = prepareTracePoint();
        if (pointCount ==0){
            return;
        }

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, heartTextureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, cameraTextureId);
        GLES20.glViewport(0, 0, width, height);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, pointCount);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        String pointTag = "pointTag";
        shader = shaderMap.get(pointTag);
        shader.glShader.stopProgram();

    }

    private int prepareTracePoint() {

        if (posQueue == null || posQueue.size() == 0) {
            return 0;
        }
        int i = 0;
        synchronized (posQueue){
            for (Point p : posQueue) {
                historyPoints[i] = 2*(p.x / ((float) width)-0.5f);
                historyPoints[i + 1] = -2*(p.y / ((float) height)-0.5f);
                historyPoints[i + 2] = 1.0f;
                i += 3;
            }
        }

        FloatBuffer vertex = GlUtil.createFloatBuffer(historyPoints);


        if (shaderMap == null) {
            shaderMap = new HashMap<>(2);
        }
        String pointTag = "pointTag";
        Shader shader = shaderMap.get(pointTag);
        if (shader == null) {
            shader = new Shader(POINT_VERTEX_SHADER_STRING, RGB_FRAGMENT_SHADER_STRING);
            shaderMap.put(pointTag, shader);
            shader.glShader.useProgram();
            GLES20.glUniform1i(shader.glShader.getUniformLocation("rgb_tex"), 1);
//            GLES20.glUniform1i(shader.glShader.getUniformLocation("oes_tex"), 0);
            GlUtil.checkNoGLES2Error("Initialize fragment shader uniform values.");

        }
        shader.glShader.useProgram();
        shader.glShader.setVertexAttribArray("trace_pos", 3, vertex);

        return i / 3;
    }

    private static final String POINT_VERTEX_SHADER_STRING =
            "varying vec2 interp_tc;\n"

    +"attribute vec4 trace_pos;\n"
                    + "\n"
                    + "\n"
                    + "void main() {\n"
                    + "    gl_Position = trace_pos;\n"
                    + "    interp_tc = trace_pos.xy;\n"
                    + "    gl_PointSize = 44.0;\n"
                    + "}\n";
    private static final String RGB_FRAGMENT_SHADER_STRING =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 interp_tc;\n" +
                    "\n" +
                    "uniform sampler2D rgb_tex;\n" +
//                    "uniform samplerExternalOES oes_tex;\n" +
                    "\n" +
                    "void main() {\n" +
                    "\tvec4 acolor = texture2D(rgb_tex, gl_PointCoord);\n" +
                    "\tif (acolor.a<0.1) discard;\n" +
                    "\tgl_FragColor = acolor;\n" +
                    "}";


    private static class Shader {
        public final GlShader glShader;

        public Shader(String fragmentShader) {
            this(NORMAL_VERTEX_SHADER_STRING, fragmentShader);
        }

        public Shader(String vertexShader, String fragmentShader) {
            this.glShader = new GlShader(vertexShader, fragmentShader);
        }
    }

    // Vertex coordinates in Normalized Device Coordinates, i.e. (-1, -1) is bottom-left and (1, 1) is
    // top-right.
    private static final FloatBuffer FULL_RECTANGLE_BUF = GlUtil.createFloatBuffer(new float[]{
            -1.0f, -1.0f, // Bottom left.
            1.0f, -1.0f, // Bottom right.
            -1.0f, 1.0f, // Top left.
            1.0f, 1.0f, // Top right.
    });

    // Texture coordinates - (0, 0) is bottom-left and (1, 1) is top-right.
    private static final FloatBuffer FULL_RECTANGLE_TEX_BUF = GlUtil.createFloatBuffer(new float[]{
            0.0f, 0.0f, // Bottom left.
            1.0f, 0.0f, // Bottom right.
            0.0f, 1.0f, // Top left.
            1.0f, 1.0f // Top right.
    });

    private static final String NORMAL_VERTEX_SHADER_STRING =
            "varying vec2 interp_tc;\n"
                    + "attribute vec4 in_pos;\n"
                    + "attribute vec4 in_tc;\n"
                    + "\n"
                    + "uniform mat4 texMatrix;\n"
                    + "\n"
                    + "void main() {\n"
                    + "    gl_Position = in_pos;\n"
                    + "    interp_tc = (texMatrix * in_tc).xy;\n"
                    + "}\n";

    private static final String OES_FRAGMENT_SHADER_STRING =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;\n" +
                    "varying vec2 interp_tc;\n" +
                    "\n" +
                    "uniform samplerExternalOES oes_tex;\n" +
                    "\n" +
                    "void main() {\n" +
                    "\tvec2 targetPos = vec2(1.0,1.0);\n" +
                    "\tif (interp_tc.y<=0.5){\n" +
                    "\t\ttargetPos = vec2(interp_tc.x,interp_tc.y);\n" +
                    "\t} else {\n" +
                    "\t\ttargetPos = vec2(interp_tc.x,1.0 - interp_tc.y);\n" +
                    "\t}\n" +
                    "\tgl_FragColor = texture2D(oes_tex, targetPos);\n" +
                    "}";

    private void prepareShader() {
        if (shaderMap == null) {
            shaderMap = new HashMap<>(2);
        }
        Shader shader = shaderMap.get(OES_FRAGMENT_SHADER_STRING);
        if (shader == null) {
            shader = new Shader(OES_FRAGMENT_SHADER_STRING);
            shaderMap.put(OES_FRAGMENT_SHADER_STRING, shader);
            shader.glShader.useProgram();
            GLES20.glUniform1i(shader.glShader.getUniformLocation("oes_tex"), 0);
            GlUtil.checkNoGLES2Error("Initialize fragment shader uniform values.");


            this.texMatrixLocation = shader.glShader.getUniformLocation("texMatrix");
        }
        shader.glShader.useProgram();
        GLES20.glUniformMatrix4fv(this.texMatrixLocation, 1, false,
                RendererCommon.rotateTextureMatrix(transMtx, -90), 0);
        shader.glShader.setVertexAttribArray("in_pos", 2, FULL_RECTANGLE_BUF);
        shader.glShader.setVertexAttribArray("in_tc", 2, FULL_RECTANGLE_TEX_BUF);
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        previewSurface.requestRender();
        surfaceTexture.getTransformMatrix(transMtx);
    }
}
