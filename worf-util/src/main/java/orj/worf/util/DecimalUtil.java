package orj.worf.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 金额数据处理工具类.
 */
public final class DecimalUtil {
    private DecimalUtil() {
        /* EMPTY */
    }

    private static final DecimalFormat FORMAT = new DecimalFormat("0.00");
    static {
        FORMAT.setRoundingMode(RoundingMode.HALF_EVEN);
    }

    public static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 小数点后位数
     */
    private static final int SCALE = 2;

    /**
     * 转换double到BigDecimal.
     */
    public static BigDecimal format(final double value) {
        return new BigDecimal(FORMAT.format(value));
    }

    /**
     * 转换long到BigDecimal.
     */
    public static BigDecimal format(final long value) {
        return new BigDecimal(FORMAT.format(value));
    }

    /**
     * 格式化BigDecimal类型的数值(无小数).
     */
    public static BigDecimal formatWithoutScale(final BigDecimal value) {
        return value.setScale(0, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 格式化BigDecimal类型的数值(两位小数).
     */
    public static BigDecimal format(final BigDecimal value) {
        return value.setScale(SCALE, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 加法运算.
     */
    public static BigDecimal add(final BigDecimal d1, final BigDecimal d2) {
        return format(d1.add(d2));
    }

    /**
     * 多个数据相加.
     */
    public static BigDecimal add(final BigDecimal v1, final BigDecimal... vs) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final BigDecimal v : vs) {
            sum = add(sum, v);
        }
        return add(v1, sum);
    }

    /**
     * 多个数据连减.
     */
    public static BigDecimal subtract(final BigDecimal v1, final BigDecimal... vs) {
        final BigDecimal sum = add(BigDecimal.ZERO, vs);
        return subtract(v1, sum);
    }

    /**
     * 减法运算.
     */
    public static BigDecimal subtract(final BigDecimal d1, final BigDecimal d2) {
        return format(d1.subtract(d2));
    }

    /**
     * 除法操作.
     */
    public static BigDecimal divide(final BigDecimal v1, final BigDecimal v2) {
        return format(v1.divide(v2));
    }

    /** 乘法操作 */
    public static BigDecimal multiply(final BigDecimal v1, final BigDecimal v2) {
        return format(v1.multiply(v2));
    }

    private static int compareBigDecimal(final BigDecimal v1, final BigDecimal v2) {
        return format(v1).compareTo(format(v2));
    }

    /**
     * 大于.
     */
    public static boolean gt(final BigDecimal v1, final BigDecimal v2) {
        return compareBigDecimal(v1, v2) > 0;
    }

    /**
     * 大于等于.
     */
    public static boolean ge(final BigDecimal v1, final BigDecimal v2) {
        return compareBigDecimal(v1, v2) >= 0;
    }

    /**
     * 等于.
     */
    public static boolean eq(final BigDecimal v1, final BigDecimal v2) {
        return compareBigDecimal(v1, v2) == 0;
    }

    /**
     * 小于.
     */
    public static boolean lt(final BigDecimal v1, final BigDecimal v2) {
        return compareBigDecimal(v1, v2) < 0;
    }

    /**
     * 小于等于.
     */
    public static boolean le(final BigDecimal v1, final BigDecimal v2) {
        return compareBigDecimal(v1, v2) <= 0;
    }

    /**
     * 不等于.
     */
    public static boolean ne(final BigDecimal v1, final BigDecimal v2) {
        return compareBigDecimal(v1, v2) != 0;
    }

}
