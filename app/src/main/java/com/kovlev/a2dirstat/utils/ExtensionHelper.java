package com.kovlev.a2dirstat.utils;

import android.graphics.Color;

import org.apache.commons.io.FilenameUtils;

import java.util.Random;

public class ExtensionHelper {

    private static Random r = new Random();

    public static int perturbColor(int color) {
        /*float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] += (r.nextDouble()-.5);
        return Color.HSVToColor(hsv);*/
        return color;
    }

    public static int getColorFromFileType(String path) {
        switch (FilenameUtils.getExtension(path)) {
            case "png":
            case "jpg":
            case "gif":
                return Color.RED;
            case "mp3":
            case "flac":
                return Color.BLUE;
            case "apk":
                return Color.GREEN;
            case "csv":
                return Color.MAGENTA;
            case "pdf":
                return Color.YELLOW;
            case "rar":
            case "zip":
            case "7z":
                return Color.DKGRAY;
            case "mp4":
            case "webm":
                return Color.CYAN;
            default:
                return Color.GRAY;
        }
    }
}
