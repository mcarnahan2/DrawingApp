package edu.apsu.drawingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static edu.apsu.drawingapp.MainActivity.backgroundColor;

public class DrawingView extends View {
    Paint backgroundPaint;
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
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(pX, pY,event.getX(), event.getY());
                pX = event.getX();
                pY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path, paint);
                path.reset();
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
