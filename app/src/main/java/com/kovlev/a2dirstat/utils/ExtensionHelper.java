package com.kovlev.a2dirstat.utils;

import android.graphics.Color;

import org.apache.commons.io.FilenameUtils;

/**
 * Assigns a color for "most" known extensions
 */
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
