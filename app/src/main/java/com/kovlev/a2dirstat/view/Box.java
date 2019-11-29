package com.kovlev.a2dirstat.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.kovlev.a2dirstat.algo.Algorithm;
import com.kovlev.a2dirstat.utils.ExtensionHelper;
import com.kovlev.a2dirstat.utils.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Box {
    public File getFile() {
        return file;
    }

    private File file;

    public Rect getView() {
        return view;
    }

    public List<Box> getChildren() {
        return children;
    }

    public long getSize() {
        return size;
    }

    private Rect view = new Rect(0,0,0,0);

    private List<Box> children = new ArrayList<>();

    private long size;

    private BoxesView container;

    private Algorithm algorithm;


    public Box(String path, BoxesView container, Algorithm algorithm) {
        this.file = new File(path);
        this.container = container;
        this.algorithm = algorithm;
    }

    public Box(File f, BoxesView container, Algorithm algorithm) {
        file = f;
        this.container = container;
        this.algorithm = algorithm;
    }

    public long totalSize() {
        if (file.isFile()) {
            size = file.length();
            return size;
        } else {
            File[] subentries = file.listFiles();
            size = 0;
            for (File entry : subentries) {
                Box b = new Box(entry, container, algorithm);
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
        algorithm.calculate(this, isHorizontal);

    }

    public void draw(Canvas canvas, Paint paintFill, Paint paintBorder) {
        if (file.isFile()) {
            paintFill.setColor(ExtensionHelper.perturbColor(ExtensionHelper.getColorFromFileType(file.getAbsolutePath())));

            if (container.getSelected() == this) {
                paintFill.setColor(Color.WHITE);
            }
            canvas.drawRect(view, paintFill);
            canvas.drawRect(view, paintBorder);
        } else {
            for (Box b : children) {
                b.draw(canvas, paintFill, paintBorder);
            }
        }
    }

    public Box getBoxAtPoint(Point p) {
        if (file.isFile()) return this;
        for (Box child : children) {
            if (child.view.contains((int)p.getX(), (int)p.getY())) {
                return child.getBoxAtPoint(p);
            }
        }
        return null;
    }
}
