package com.kovlev.a2dirstat.algo;

import android.graphics.Rect;

import com.kovlev.a2dirstat.view.Box;

public class NaiveAlgorithm extends Algorithm {
    @Override
    public void calculate(Box box, boolean isHorizontal) {
        long len = isHorizontal ? box.getView().width() : box.getView().height();
        double increment = (double)len/box.getSize();
        double acc = 0;
        for (Box b : box.getChildren()) {
            double incAcc = acc + b.getSize() * increment;
            if (isHorizontal) {
                b.calculate(new Rect(box.getView().left + (int) acc, box.getView().top,
                        box.getView().left + (int) incAcc, box.getView().bottom), false);
            } else {
                b.calculate(new Rect(box.getView().left, box.getView().top + (int) acc,
                        box.getView().right, box.getView().top + (int) incAcc), true);
            }
            acc = incAcc;
        }
    }
}
