package com.kovlev.a2dirstat.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BoxesView extends View {
    private Paint paint;

    public BoxesView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

    }

    private Box root;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GREEN);
        canvas.drawRect(5, 10, 150, 200, paint);
    }
}
