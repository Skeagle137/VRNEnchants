package net.skeagle.vrnenchants.util;

import java.math.BigInteger;
import java.util.regex.Pattern;

public final class TimeUtil {

    private static final Pattern ALLOWED_VALUES = Pattern.compile("[smhdy]");

    public static String timeToMessage(final long time) {
        final long years = time / 31536000;
        final long days = (time % 31536000) / 86400;
        final long hours = ((time % 31536000) % 86400) / 3600;
        final long minutes = (((time % 31536000) % 86400) % 3600) / 60;
        final long seconds = (((time % 31536000) % 86400) % 3600) % 60;
        String s = ((years != 0) ? years + " " + timeGrammarCheck("year", years) + (days != 0 || hours != 0 || minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((days != 0) ? days + " " + timeGrammarCheck("day", days) + (hours != 0 || minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((hours != 0) ? hours + " " + timeGrammarCheck("hour", hours) + (minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((minutes != 0) ? minutes + " " + timeGrammarCheck("minute", minutes) + (seconds != 0 ? ", " : "") : "") +
                ((seconds != 0) ? seconds + " " + timeGrammarCheck("second", seconds) : "");
        if (s.equals(""))
            return "less than a second";
        return s;
    }

    public static String timeToMessage(final int time) {
        return timeToMessage((long) time);
    }

    private static String timeGrammarCheck(final String s, final long i) {
        if (i != 1)
            return s + "s";
        return s;
    }

    private enum TimeUnits {
        s(1),
        m(60),
        h(3600),
        d(86400),
        w(604800),
        y(31556926);

        private final long time_seconds;

        TimeUnits(final long time_seconds) {
            this.time_seconds = time_seconds;
        }
    }

    public static long parseTimeString(String s) throws TimeFormatException {
        s = s.replaceAll("[^a-zA-Z0-9]","");
        s = s.toLowerCase();
        long num = 0;
        BigInteger big;
        String nums = "";
        String tempunit = "";
        int matches = 0;
        char[] c = s.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (ALLOWED_VALUES.matcher(Character.toString(c[i])).matches()) {
                if (nums.equals(""))
                    throw new TimeFormatException("&cError in position " + (i + 1) + " of the time, you must provide a number before a time unit.");
                matches += 1;
                big = new BigInteger(nums);
                num += big.longValue() * convertUnitToTime(c[i]);
                tempunit = Character.toString(c[i]);
                nums = "";
            }
            else if (Character.getNumericValue(c[i]) > -1) {
                nums += c[i];
                tempunit = "";
            }
            else {
                if (!ALLOWED_VALUES.matcher(Character.toString(c[i])).matches() && Character.getNumericValue(c[i]) >= 10)
                    throw new TimeFormatException("&cError in position " + (i + 1) + " of the time, unknown time unit. The accepted values are seconds (s), minutes (m), hours (h), days (d), or years (y).");
                throw new TimeFormatException("&cError in parsing the time, please check if you formatted the time correctly.");
            }
        }
        if (!nums.equals("") && matches == 0 || tempunit.equals("")) {
            if (!ALLOWED_VALUES.matcher(s.substring(c.length - 1)).matches() && Character.getNumericValue(s.charAt(c.length - 1)) >= 10)
                throw new TimeFormatException("&cError in position " + c.length + " of the time, unknown time unit. The accepted values are seconds (s), minutes (m), hours (h), days (d), or years (y).");
            throw new TimeFormatException("&cError in position " + c.length + " of the time, you must specify the time unit.");
        }
        return num;
    }

    private static long convertUnitToTime(char c) {
        for (TimeUnits t : TimeUnits.values())
            if (Character.toString(c).equalsIgnoreCase(t.name()))
                return t.time_seconds;
        return 1;
    }

    public static final class TimeFormatException extends Exception {

        private static final long serialVersionUID = -8147740739527052671L;
        private final String message;

        public TimeFormatException(final String message) {
            super("");
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
