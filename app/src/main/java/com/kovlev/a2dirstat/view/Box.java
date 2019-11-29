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

/**
 * Represents a Box, which is az entry (folder or file) in the directory structure
 */
public class Box {
    private File file;

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

    /**
     * Calculates the total size of the entry (file or folder)
     * To do this, this method creates new Box classes and registers them as their children, then calls
     *      totalSize() on them
     * @return The total size in bytes
     */
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

    /**
     * Calculates the rectangles based on the sizes of the boxes and its children
     * @param parent The parent rectangle, which this box can use and further subdivide
     */
    public void calculate(Rect parent) {
        calculate(parent, true);
    }

    /**
     * Helper function for calculation
     * @param parent The parent rectangle
     * @param isHorizontal Whether the screen has a width greater than its height
     */
    public void calculate(Rect parent, boolean isHorizontal) {
        view = parent;
        algorithm.calculate(this, isHorizontal);

    }

    /**
     * Draws a box
     * If it is a file, draws a colorful rectangle
     * Else calls draw() recursively on its children
     * @param canvas The canvas to draw to
     * @param paintFill Used to draw filled colored rects
     * @param paintBorder Used to draw borders
     */
    public void draw(Canvas canvas, Paint paintFill, Paint paintBorder) {
        if (file.isFile()) {
            paintFill.setColor(ExtensionHelper.getColorFromFileType(file.getAbsolutePath()));

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

    /**
     * Retrieves the box at a given point (for selection)
     * Since multiple boxes can occupy the same spot (a file, and all its parent directories)
     *      only the box corresponding to a file will be returned
     * @param p The touched point
     * @return The box at the given point
     */
    public Box getBoxAtPoint(Point p) {
        if (file.isFile()) return this;
        for (Box child : children) {
            if (child.view.contains((int)p.getX(), (int)p.getY())) {
                return child.getBoxAtPoint(p);
            }
        }
        return null;
    }

    // Getters

    public File getFile() {
        return file;
    }

    public Rect getView() {
        return view;
    }

    public List<Box> getChildren() {
        return children;
    }

    public long getSize() {
        return size;
    }
}
