package tech.wedev.wecombatch.utils;

import java.math.BigDecimal;

/**
 * 值类型相互转换工具类
 */
public class ValConvertUtils {
    public ValConvertUtils() {
    }

    /**
     * decimal类型转换为int基础类型
     *
     * @param val 被转换的值
     * @return
     */
    public static int decimalToInt(BigDecimal val) {
        int result = 0;
        if (val != null) {
            result = Integer.parseInt(val.toString());
        } else {
            throw new IllegalArgumentException("the val is not null");
        }
        return result;
    }

    /**
     * int基础类型转换为decimal类型
     *
     * @param val 被转换的值
     * @return
     */
    public static BigDecimal intToDecimal(int val) {
        return new BigDecimal(val);
    }
}
