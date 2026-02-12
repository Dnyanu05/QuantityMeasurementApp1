package com.quantitymeasurement;

/**
 * Length units with INCH as the base unit for normalization.
 *
 * Notes:
 * - Both singular and plural enum constants are provided to keep tests flexible:
 *   INCH/INCHES, FOOT/FEET, YARD/YARDS, CENTIMETER/CENTIMETERS.
 * - Internally we use conversion factors "to inches".
 * - Base unit is INCH (and alias INCHES).
 */
public enum LengthUnit {

    // Base unit(s)
    INCH(1.0),
    INCHES(1.0),

    // Feet
    FOOT(12.0),          // 1 ft = 12 in
    FEET(12.0),

    // Yards
    YARD(36.0),          // 1 yd = 36 in
    YARDS(36.0),

    // Centimeters
    CENTIMETER(0.3937007874),   // 1 cm = 1/2.54 in (≈ 0.3937007874")
    CENTIMETERS(0.3937007874);

    /** multiplicative factor to convert THIS unit to base inches. */
    private final double toInchesFactor;

    LengthUnit(double toInchesFactor) {
        this.toInchesFactor = toInchesFactor;
    }

    // ----------------------------------------------------------------------
    // Core API (kept for backward compatibility with your existing code)
    // ----------------------------------------------------------------------

    /** @return true if this unit is one of the base-unit aliases (INCH / INCHES). */
    public boolean isBase() {
        return this == INCH || this == INCHES;
    }

    /** @return the multiplicative factor that converts THIS unit to inches. */
    public double factorToInches() {
        return toInchesFactor;
    }

    /** Convert a value of THIS unit to base inches. */
    public double toBaseInches(double value) {
        return value * toInchesFactor;
    }

    /** Convert a value in base inches to THIS unit. */
    public double fromBaseInches(double inches) {
        return inches / toInchesFactor;
    }

    // ----------------------------------------------------------------------
    // Convenience wrappers (used by some UC7 snippets; same math as above)
    // ----------------------------------------------------------------------

    /** Alias of {@link #toBaseInches(double)} for cleaner naming. */
    public double toBase(double value) {
        return toBaseInches(value);
    }

    /** Alias of {@link #fromBaseInches(double)} for cleaner naming. */
    public double fromBase(double baseInches) {
        return fromBaseInches(baseInches);
    }

    // ----------------------------------------------------------------------
    // Quality-of-life helpers
    // ----------------------------------------------------------------------

    /**
     * Returns the canonical representative for this unit:
     * INCHES -> INCH, FEET -> FOOT, YARDS -> YARD, CENTIMETERS -> CENTIMETER.
     * This is useful when you want to display or compare in a normalized form.
     */
    public LengthUnit canonical() {
        switch (this) {
            case INCH:
            case INCHES:
                return INCH;
            case FOOT:
            case FEET:
                return FOOT;
            case YARD:
            case YARDS:
                return YARD;
            case CENTIMETER:
            case CENTIMETERS:
                return CENTIMETER;
            default:
                return this; // should not happen
        }
    }

    /**
     * Lenient parser for unit names. Accepts case-insensitive strings and common spellings.
     * Examples: "inch", "INCHES", "Feet", "yard", "centimeter", "cms" -> CENTIMETER.
     *
     * @throws IllegalArgumentException if the text cannot be parsed to a known length unit.
     */
    public static LengthUnit of(String text) {
        if (text == null) throw new IllegalArgumentException("unit text cannot be null");
        String t = text.trim().toLowerCase();

        switch (t) {
            case "inch":
            case "inches":
            case "in":
                return INCH;
            case "foot":
            case "feet":
            case "ft":
                return FOOT;
            case "yard":
            case "yards":
            case "yd":
                return YARD;
            case "centimeter":
            case "centimeters":
            case "centimetre":
            case "centimetres":
            case "cm":
            case "cms":
                return CENTIMETER;
            default:
                // As a fallback, try exact enum names (developer inputs).
                try {
                    return LengthUnit.valueOf(text.toUpperCase());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Unknown length unit: " + text);
                }
        }
    }

    /** For future extensibility; always true for this enum. */
    public boolean isLength() {
        return true;
    }
}