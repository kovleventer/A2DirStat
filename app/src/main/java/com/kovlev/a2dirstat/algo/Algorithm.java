package com.kovlev.a2dirstat.algo;

import com.kovlev.a2dirstat.view.Box;

/**
 * Abstract class different treemap creating algorithms
 */
public abstract class Algorithm {
    public abstract void calculate(Box box, boolean isHorizontal);
}
