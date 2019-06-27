
package com.didi365.carlife.android.phone.util;

import android.util.Log;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogUtil {
    public static final boolean IS_WRITE_TO_FILE = false;
    public static final String LOG_FILE = "_Carlife.log";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());

    public static void dumpException(Throwable t) {
        if (CommonParams.LOG_LEVEL <= Log.WARN) {
            final int innerBufLen = 256;
            StringBuilder err = new StringBuilder(innerBufLen);

            err.append("Got exception: ");
            err.append(t.toString());
            err.append("\n");

            System.out.println(err.toString());
            t.printStackTrace(System.out);
        }
    }

    public static void v(String tag, String format, Object... args) {
        outMsg(Log.VERBOSE, tag, format, args);
    }

    public static void v(String tag, String format) {
        if (CommonParams.LOG_LEVEL <= Log.VERBOSE) {
            Log.v(tag, format);
        }
    }

    public static void d(String tag, String format, Object... args) {
        outMsg(Log.DEBUG, tag, format, args);
    }

    public static void d(String tag, String format) {
        if (CommonParams.LOG_LEVEL <= Log.DEBUG) {
            Log.d(tag, format);
        }
    }

    public static void i(String tag, String format, Object... args) {
        outMsg(Log.INFO, tag, format, args);
    }

    public static void i(String tag, String format) {
        if (CommonParams.LOG_LEVEL <= Log.INFO) {
            Log.i(tag, format);
            f(tag, format);
        }
    }

    public static void w(String tag, String format, Object... args) {
        outMsg(Log.WARN, tag, format, args);
    }

    public static void w(String tag, String format) {
        if (CommonParams.LOG_LEVEL <= Log.WARN) {
            Log.w(tag, format);
            f(tag, format);
        }
    }

    public static void e(String tag, String format, Object... args) {
        outMsg(Log.ERROR, tag, format, args);
    }

    public static void e(String tag, String format) {
        Log.e(tag, format);
    }

    private static void outMsg(int level, String tag, String format, Object... args) {
        if (CommonParams.LOG_LEVEL <= level && tag != null && format != null) {
            Log.println(level, tag, String.format(format, args));
            f(tag, format);
        }
    }

    public static void f(String tag, String format) {
        FileWriter fw = null;
        try {
            if (tag == null || format == null) {
                return;
            }
            int len = format.length();
            int max = CommonParams.MAX_DATA_DISPLAY_LENGTH;
            String tformat = format.substring(0, len > max ? max : len);

            String date = DATE_FORMAT.format(new Date());
            String log = "[" + date + "]" + tag + "--->" + tformat + "\r\n";
            String logFile;
            if (date != null && date.length() >= 10) {
                logFile = CommonParams.SD_DIR + "/" + date.substring(0, 10) + LOG_FILE;
            } else {
                return;
            }
            fw = new FileWriter(logFile, true);
            fw.write(log);
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
