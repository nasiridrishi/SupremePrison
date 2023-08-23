package net.primegames.supremeprison.utils.misc;

import java.math.BigInteger;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class NumberUtils {

    //source https://stackoverflow.com/questions/4753251/how-to-go-about-formatting-1200-to-1-2k-in-java
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    private NumberUtils() {
        throw new UnsupportedOperationException("Cannot instantiate");
    }

    public static boolean isLongGreaterThanMaxLong(String input) {
        BigInteger bigIntegerInput = new BigInteger(input);
        return bigIntegerInput.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0;
    }

    public static boolean wouldAdditionBeOverMaxLong(long number1, long number2) {
        BigInteger bigInteger1 = new BigInteger(String.valueOf(number1));
        BigInteger bigInteger2 = new BigInteger(String.valueOf(number2));
        BigInteger result = bigInteger1.add(bigInteger2);

        return result.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0;
    }

    //source https://stackoverflow.com/questions/4753251/how-to-go-about-formatting-1200-to-1-2k-in-java
    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE, so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != ((double) truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String format(double value) {
        return format((long) value);
    }
}
