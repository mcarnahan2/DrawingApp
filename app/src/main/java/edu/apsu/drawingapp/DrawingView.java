package edu.apsu.drawingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import static edu.apsu.drawingapp.MainActivity.backgroundColor;
import static edu.apsu.drawingapp.MainActivity.buttonPressed;

public class DrawingView extends View {
    Paint backgroundPaint;
    Paint rectanglePaint;
    Paint circlePaint;
    Paint textPaint;
    private Paint paint;
    private Paint BitmapPaint;
    private Path path;

    private int currentHeight;
    private int currentWidth;

    private float pX;
    private float pY;

    private int paintColor = Color.BLACK;
    private static Paint.Style paintStyle = Paint.Style.STROKE;
    private static int paintWidth = 3;

    private Canvas canvas;
    private Canvas cacheCanvas;
    private Bitmap cacheBitmap;

    float rBeginX;
    float rEndX;
    float rBeginY;
    float rEndY;
    private RectF rect;

    float cBeginX;
    float cEndX;
    float cBeginY;
    float cEndY;
    private RectF circ;

    float tBeginX;
    float tBeginY;

    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.RED);
        backgroundPaint.setStyle(Paint.Style.FILL);
        cacheBitmap = Bitmap.createBitmap(currentWidth, currentHeight, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cacheBitmap);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        BitmapPaint = new Paint();
        updatePaint();

        rectanglePaint = new Paint();
        rectanglePaint.setColor(Color.BLACK);
        rectanglePaint.setStyle(Paint.Style.FILL);

        circlePaint = new Paint();
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);

        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.LEFT);

        float spSize = 30; //30 sp
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float pixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spSize, dm);

        textPaint.setTextSize(pixelSize);
    }

    private void updatePaint() {
        paint.setColor(paintColor);
        paint.setStyle(paintStyle);
        paint.setStrokeWidth(paintWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        canvas.drawBitmap(cacheBitmap,0, 0, BitmapPaint);
        canvas.drawPath(path, paint);
        if (MainActivity.bitmap != null) {
            canvas.drawBitmap(MainActivity.bitmap, 0, 0, backgroundPaint);
            MainActivity.bitmap = null;
        } else if (MainActivity.backgroundColor == 0) {
            backgroundPaint.setColor(MainActivity.backgroundColor);
            canvas.drawPaint(backgroundPaint);
            MainActivity.backgroundColor = 0;
        } else {
            canvas.drawPaint(backgroundPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        currentHeight = h;
        currentWidth = w;
        init();
 }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());

                pX = event.getX();
                pY = event.getY();

                if(buttonPressed==2){
                    rBeginX=pX;
                    rBeginY=pY;
                } else if(buttonPressed ==3){
                    cBeginX=pX;
                    cBeginY=pY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(buttonPressed==1) {
                    path.quadTo(pX, pY, event.getX(), event.getY());
                }
                pX = event.getX();
                pY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                if(buttonPressed==1) {
                    cacheCanvas.drawPath(path, paint);
                    path.reset();
                } else if(buttonPressed==2) {
                    rEndX = event.getX();
                    rEndY = event.getY();
                    rect = new RectF(rBeginX, rBeginY, rEndX, rEndY);
                    cacheCanvas.drawRect(rect, rectanglePaint);
                } else if(buttonPressed==3){
                    cEndX =event.getX();
                    cEndY = event.getY();
                    circ = new RectF(cBeginX, cBeginY, cEndX, cEndY);
                    cacheCanvas.drawOval(circ, circlePaint);
                } else if(buttonPressed==4){
                    tBeginX = event.getX();
                    tBeginY = event.getY();
                    cacheCanvas.drawText(MainActivity.text, tBeginX, tBeginY, textPaint);
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setColor(int color){
        paintColor = color;
        updatePaint();
    }


    public void setPaintWidth(int width){
        paintWidth = width;
        updatePaint();
    }

    public static final int PEN = 3;
    public static final int PAIL = 2;


    public void setStyle(int style){
        switch(style){
            case PEN:
                paintStyle = Paint.Style.STROKE;
                break;
            case PAIL:
                paintStyle = Paint.Style.FILL;
                break;
        }
        updatePaint();
    }

    public void clearScreen() {
        if(canvas != null){
            Paint backPaint = new Paint();
            backPaint.setColor(Color.WHITE);
            canvas.drawRect(new Rect(0, 0, currentWidth, currentHeight), backPaint);
            cacheCanvas.drawRect(new Rect(0, 0, currentWidth, currentHeight), backPaint);
        }
        invalidate();
    }


}
