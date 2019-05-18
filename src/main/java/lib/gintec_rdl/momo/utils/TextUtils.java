package lib.gintec_rdl.momo.utils;

public final class TextUtils {

    public static String trimmedOrNull(String text) {
        return (null != text) ? text.trim() : null;
    }

    public static double currency(String input) {
        if (input != null) {
            try {
                return Double.valueOf(input.replace(",", ""));
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        return 0D;
    }
}
