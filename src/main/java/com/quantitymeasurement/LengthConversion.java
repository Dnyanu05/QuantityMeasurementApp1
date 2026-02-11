package com.quantitymeasurement;

public class LengthConversion {
    private static final double EPSILON = 1e-6;

    public static double convert(double value, LengthUnit from, LengthUnit to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Units cannot be null");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid numeric value");
        }
        if (from == to) return value;

        Length src = new Length(value, from);
        Length out = src.convertTo(to);
        return round6(out.getValue());
    }

    private static double round6(double v) {
        return Math.round(v * 1_000_000d) / 1_000_000d;
    }

    public static boolean almostEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }
}