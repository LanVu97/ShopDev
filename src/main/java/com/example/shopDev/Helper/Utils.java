package com.example.shopDev.Helper;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static <T> T cast(Object value, String type) {
        switch (type){
            case "float":
                return (T)Float.valueOf(value.toString());
            case "int":
                return (T)Integer.valueOf(value.toString());
            case "long":
                return (T)Long.valueOf(value.toString());
            case "double":
                return (T)Double.valueOf(value.toString());
            case "boolean":
                return (T)Boolean.valueOf(value.toString());
            default:
                break;
        }
        return (T) value;
    }


}
