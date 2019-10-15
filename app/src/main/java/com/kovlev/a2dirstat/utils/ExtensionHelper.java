package com.kovlev.a2dirstat.utils;

import android.graphics.Color;

import org.apache.commons.io.FilenameUtils;

public class ExtensionHelper {

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
            default:
                return Color.GRAY;
        }
    }
}
