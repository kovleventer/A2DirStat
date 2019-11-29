package com.kovlev.a2dirstat.algo;

import android.graphics.Rect;

import com.kovlev.a2dirstat.view.Box;

import java.util.Collections;
import java.util.Comparator;

public class SortedAlgorithm extends Algorithm {
    @Override
    public void calculate(Box box, boolean isHorizontal) {
        long len = isHorizontal ? box.getView().width() : box.getView().height();
        double increment = (double)len/box.getSize();
        double acc = 0;
        System.out.println("asd");
        Collections.sort(box.getChildren(), new Comparator<Box>() {
            @Override
            public int compare(Box b1, Box b2) {
                return (int) (b1.getSize() - b2.getSize());
            }
        });
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
