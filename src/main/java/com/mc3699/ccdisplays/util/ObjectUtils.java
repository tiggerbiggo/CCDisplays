package com.mc3699.ccdisplays.util;

import java.util.Map;

public class ObjectUtils {
    /**
     * Gets a float value from a map, with type conversion support.
     *
     * @param params Map containing parameters
     * @param key Key to look up
     * @param defaultValue Default value to return if key doesn't exist or value can't be converted
     * @return The float value, or defaultValue if not found or not convertible
     */
    public static float getFloatOrDefault(Map<String, Object> params, String key, float defaultValue) {
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            if (value instanceof Float) {
                return (Float) value;
            } else if (value instanceof Number) {
                return ((Number) value).floatValue();
            } else if (value instanceof String) {
                try {
                    return Float.parseFloat((String) value);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Gets a generic value from a map with type conversion support.
     *
     * @param <T> The expected return type
     * @param params Map containing parameters
     * @param key Key to look up
     * @param defaultValue Default value to return if key doesn't exist or conversion fails
     * @param type Class of the expected type
     * @return The value as the requested type, or defaultValue if not found or not convertible
     */
    public static <T> T getParamOrDefault(Map<String, Object> params, String key, T defaultValue, Class<T> type) {
        if(params == null){
            return defaultValue;
        }
        Object value = params.get(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            // Direct instance check
            if (type.isInstance(value)) {
                return type.cast(value);
            }

            // Number conversion for numeric types
            if (Number.class.isAssignableFrom(type) && value instanceof Number) {
                Number numValue = (Number) value;

                if (type == Integer.class) {
                    return type.cast(Integer.valueOf(numValue.intValue()));
                } else if (type == Float.class) {
                    return type.cast(Float.valueOf(numValue.floatValue()));
                } else if (type == Double.class) {
                    return type.cast(Double.valueOf(numValue.doubleValue()));
                } else if (type == Long.class) {
                    return type.cast(Long.valueOf(numValue.longValue()));
                } else if (type == Short.class) {
                    return type.cast(Short.valueOf(numValue.shortValue()));
                } else if (type == Byte.class) {
                    return type.cast(Byte.valueOf(numValue.byteValue()));
                }
            }

            // String parsing for numeric types
            if (value instanceof String) {
                String strValue = (String) value;

                if (type == Integer.class) {
                    try { return type.cast(Integer.valueOf(strValue)); } catch (NumberFormatException e) { return defaultValue; }
                } else if (type == Float.class) {
                    try { return type.cast(Float.valueOf(strValue)); } catch (NumberFormatException e) { return defaultValue; }
                } else if (type == Double.class) {
                    try { return type.cast(Double.valueOf(strValue)); } catch (NumberFormatException e) { return defaultValue; }
                } else if (type == Long.class) {
                    try { return type.cast(Long.valueOf(strValue)); } catch (NumberFormatException e) { return defaultValue; }
                } else if (type == Boolean.class) {
                    return type.cast(Boolean.valueOf(strValue));
                }
                // Add other type conversions as needed
            }

            // Boolean handling
            if (type == Boolean.class && value instanceof Number) {
                // Treat 0 as false, anything else as true
                return type.cast(Boolean.valueOf(((Number) value).intValue() != 0));
            }

        } catch (Exception e) {
            // Any exception during conversion returns the default
        }

        // Default fallback
        return defaultValue;
    }
}
