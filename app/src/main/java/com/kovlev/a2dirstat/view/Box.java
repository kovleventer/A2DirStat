package com.kovlev.a2dirstat.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.kovlev.a2dirstat.utils.ExtensionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Box {
    private File file;

    private Rect view = new Rect(0,0,0,0);

    private List<Box> children = new ArrayList<>();

    private long size;


    public Box(String path) {
        this.file = new File(path);
    }

    public Box(File f) {
        file = f;
    }

    public long totalSize() {
        if (file.isFile()) {
            size = file.length();
            return size;
        } else {
            File[] subentries = file.listFiles();
            size = 0;
            for (File entry : subentries) {
                Box b = new Box(entry);
                children.add(b);
                size += b.totalSize();
            }
            return size;
        }
    }

    public void calculate(Rect parent) {
        calculate(parent, true);
    }

    public void calculate(Rect parent, boolean isHorizontal) {
        view = parent;
        long len = isHorizontal ? parent.width() : parent.height();
        double increment = (double)len/size;
        double acc = 0;
        for (Box b : children) {
            double incAcc = acc + b.size * increment;
            if (isHorizontal) {
                b.calculate(new Rect(parent.left + (int) acc, parent.top,
                        parent.left + (int) incAcc, parent.bottom), false);
            } else {
                b.calculate(new Rect(parent.left, parent.top + (int) acc,
                        parent.right, parent.top + (int) incAcc), true);
            }
            acc = incAcc;
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        if (file.isFile()) {
            paint.setColor(ExtensionHelper.perturbColor(ExtensionHelper.getColorFromFileType(file.getAbsolutePath())));
            canvas.drawRect(view, paint);
        } else {
            for (Box b : children) {
                b.draw(canvas, paint);
            }
        }
    }
}
