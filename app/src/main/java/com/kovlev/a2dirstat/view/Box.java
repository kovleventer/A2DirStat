package com.kovlev.a2dirstat.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.kovlev.a2dirstat.utils.ExtensionHelper;

import java.util.List;

public class Box {
    private String path;

    private Rect view;

    private List<Box> children;

    private boolean isfile;

    public Box(String path) {
        this.path = path;
    }

    public void calculate(Rect parent) {
        view = parent;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(ExtensionHelper.getColorFromFileType(path));
        canvas.drawRect(view, paint);
    }
}
