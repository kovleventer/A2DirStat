package com.kovlev.a2dirstat.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

public class BoxesView extends View {
    private Paint paint;
    private int width, height;

    public BoxesView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

    }

    public void initView() {
        root = new Box(Environment.getExternalStorageDirectory().getAbsolutePath());
        String state = Environment.getExternalStorageState();
        System.out.println(state);
        System.out.println(root.totalSize());
    }

    private Box root;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (root != null) {
            root.draw(canvas, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        if (root != null) {
            root.calculate(new Rect(0, 0, width, height));
        }
    }
}
