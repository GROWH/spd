package org.jrplat.platform.util;

import java.math.BigDecimal;

/**
 * Created by 赵腾飞 on 16-12-1.
 * <p>
 * add         +++
 * subtract    ---
 * multiply    ***
 * divide      ///
 */
public class BigDecimalUtils {


    public static BigDecimal add(String num1, String num2) {
        return new BigDecimal(num1).add(new BigDecimal(num2));
    }

    public static BigDecimal add(Double num1, String num2) {
        return BigDecimal.valueOf(num1).add(new BigDecimal(num2));
    }

    public static BigDecimal add(String num1, Double num2) {
        return new BigDecimal(num1).add(BigDecimal.valueOf(num2));
    }

    public static BigDecimal add(Double num1, Double num2) {
        return BigDecimal.valueOf(num1).add(BigDecimal.valueOf(num2));
    }


    public static BigDecimal subtract(String num1, String num2) {
        return new BigDecimal(num1).subtract(new BigDecimal(num2));
    }

    public static BigDecimal subtract(Double num1, String num2) {
        return BigDecimal.valueOf(num1).subtract(new BigDecimal(num2));
    }

    public static BigDecimal subtract(String num1, Double num2) {
        return new BigDecimal(num1).subtract(BigDecimal.valueOf(num2));
    }

    public static BigDecimal subtract(Double num1, Double num2) {
        return BigDecimal.valueOf(num1).subtract(BigDecimal.valueOf(num2));
    }


    public static BigDecimal multiply(String num1, String num2) {
        return new BigDecimal(num1).multiply(new BigDecimal(num2));
    }

    public static BigDecimal multiply(Double num1, String num2) {
        return BigDecimal.valueOf(num1).multiply(new BigDecimal(num2));
    }

    public static BigDecimal multiply(String num1, Double num2) {
        return new BigDecimal(num1).multiply(BigDecimal.valueOf(num2));
    }

    public static BigDecimal multiply(Double num1, Double num2) {
        return BigDecimal.valueOf(num1).multiply(BigDecimal.valueOf(num2));
    }


    public static BigDecimal divide(String num1, String num2) {
        return new BigDecimal(num1).divide(new BigDecimal(num2));
    }

    public static BigDecimal divide(Double num1, String num2) {
        return BigDecimal.valueOf(num1).divide(new BigDecimal(num2));
    }

    public static BigDecimal divide(String num1, Double num2) {
        return new BigDecimal(num1).divide(BigDecimal.valueOf(num2));
    }

    public static BigDecimal divide(Double num1, Double num2) {
        return BigDecimal.valueOf(num1).divide(BigDecimal.valueOf(num2));
    }


}
