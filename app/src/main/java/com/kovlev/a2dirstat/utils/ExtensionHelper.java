package com.kovlev.a2dirstat.utils;

import android.graphics.Color;

import org.apache.commons.io.FilenameUtils;

import java.util.Random;

public class ExtensionHelper {

    private static Random r = new Random();

    public static int perturbColor(int color) {
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
            default:
                return Color.GRAY;
        }
    }
}
