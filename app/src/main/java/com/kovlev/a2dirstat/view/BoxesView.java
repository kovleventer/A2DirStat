package com.kovlev.a2dirstat.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kovlev.a2dirstat.algo.Algorithm;
import com.kovlev.a2dirstat.utils.Point;

/**
 * Custom view which holds boxes
 */
public class BoxesView extends View {
    private Paint paintFill, paintBorder;
    private int width, height;
    private OnBoxSelectedListener onBoxSelectedListener;
    // Whether the root view has finished size calculation and drawing is safe
    private boolean finishedCalc = false;

    public void setOnBoxSelectedListener(OnBoxSelectedListener onBoxSelectedListener) {
        this.onBoxSelectedListener = onBoxSelectedListener;
    }

    public BoxesView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        paintFill = new Paint();
        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.BLACK);
        paintBorder.setStrokeWidth(1);
    }

    /**
     * Only can be called after filesystem read permissions are granted
     * @param algorithm The algorithm to calculate rect dimensions with
     */
    public void initView(Algorithm algorithm) {
        root = new Box(Environment.getExternalStorageDirectory().getAbsolutePath(), this, algorithm);
        System.out.println(Environment.getExternalStorageState());
    }

    private Box root;
    private Box selected = null;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (root != null && finishedCalc) {
            root.draw(canvas, paintFill, paintBorder);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        if (root != null) {
            // Recalculates rect sizes on a different thread
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    finishedCalc = false;
                    root.totalSize();
                    root.calculate(new Rect(0, 0, width, height));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    finishedCalc = true;
                    invalidate();
                }
            }.execute();

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handles box selection events
        Point p = new Point(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                Box newSelected = root.getBoxAtPoint(p);
                // Deselect if needed
                if (selected == newSelected) {
                    selected = null;
                } else {
                    selected = newSelected;
                    if (onBoxSelectedListener != null)
                        onBoxSelectedListener.onBoxSelected(selected);
                }
                invalidate();
                break;
            default:
                return false;
        }
        return true;
    }

    public Box getSelected() {
        return selected;
    }

    /**
     * Selected box events are sent to this interface, which should be implemented by the holding activity
     */
    public interface OnBoxSelectedListener {
        void onBoxSelected(Box box);
    }

}
