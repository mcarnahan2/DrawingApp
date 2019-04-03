package edu.apsu.drawingapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import static edu.apsu.drawingapp.MainActivity.backgroundColor;

public class DrawingView extends View {
    Paint backgroundPaint;

    private int currentHeight;
    private int currentWidth;

    public DrawingView(Context context) {
        super(context);
        setup(null);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(attrs);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(attrs);
    }

    private void setup(AttributeSet attrs){
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.RED);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(MainActivity.bitmap != null) {
            canvas.drawBitmap(MainActivity.bitmap, 0, 0, backgroundPaint);
            MainActivity.bitmap = null;
        } else if(MainActivity.backgroundColor == 0) {
            backgroundPaint.setColor(MainActivity.backgroundColor);
            canvas.drawPaint(backgroundPaint);
            MainActivity.backgroundColor = 0;
        } else {
            canvas.drawPaint(backgroundPaint);
        }
    }
}
