package de.jdsoft.nyup.Utils;


import java.awt.*;

public class Utils {
    public static double[][] copy2DArray(double[][] arr) {
        int n = arr.length;
        double[][] copy = new double[n][];
        for (int i = 0; i < n; i++) {
            copy[i] = new double[arr[i].length];
            System.arraycopy(arr[i], 0, copy[i], 0, arr[i].length);
        }
        return copy;
    }

    public static float[][] copy2DArray(float[][] arr) {
        int n = arr.length;
        float[][] copy = new float[n][];
        for (int i = 0; i < n; i++) {
            copy[i] = new float[arr[i].length];
            System.arraycopy(arr[i], 0, copy[i], 0, arr[i].length);
        }
        return copy;
    }

    public static double[][] copyInto2DArray(double[][] dst,double[][] src) {
        int n = src.length;
        double[][] copy = new double[n][];
        for (int i = 0; i < n; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }
        return copy;
    }

    public static float[][] copyInto2DArray(float[][] dst, float[][] src) {
        int n = src.length;
        float[][] copy = new float[n][];
        for (int i = 0; i < n; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, src[i].length);
        }
        return copy;
    }

    public static double min(double[][] arr) {
        double min = Double.POSITIVE_INFINITY;
        for (double[] anArr : arr) {
            for (double anAnArr : anArr) {
                if (anAnArr < min) {
                    min = anAnArr;
                }
            }
        }
        return min;
    }

    public static double max(double[][] arr) {
        double max = Double.NEGATIVE_INFINITY;
        for (double[] anArr : arr) {
            for (double anAnArr : anArr) {
                if (anAnArr > max) {
                    max = anAnArr;
                }
            }
        }
        return max;
    }
}
