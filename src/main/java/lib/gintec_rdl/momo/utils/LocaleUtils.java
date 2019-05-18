package lib.gintec_rdl.momo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class LocaleUtils {

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US).format(date);
    }
}
