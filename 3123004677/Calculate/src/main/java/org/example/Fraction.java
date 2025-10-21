package org.example;

import java.util.Objects;

/**
 * 表示一个分数，可以是自然数或真分数
 */
public class Fraction {
    private int numerator;   // 分子
    private int denominator; // 分母

    /**
     * 创建一个自然数
     */
    public Fraction(int value) {
        this.numerator = value;
        this.denominator = 1;
    }

    /**
     * 创建一个分数
     */
    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("分母不能为0");
        }
        
        // 处理负数情况
        if (denominator < 0) {
            numerator = -numerator;
            denominator = -denominator;
        }
        
        // 约分
        int gcd = gcd(Math.abs(numerator), denominator);
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
    }

    /**
     * 计算最大公约数
     */
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    /**
     * 计算最小公倍数
     */
    private int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }

    /**
     * 加法
     */
    public Fraction add(Fraction other) {
        int lcm = lcm(this.denominator, other.denominator);
        int newNumerator = this.numerator * (lcm / this.denominator) + 
                          other.numerator * (lcm / other.denominator);
        return new Fraction(newNumerator, lcm);
    }

    /**
     * 减法
     */
    public Fraction subtract(Fraction other) {
        int lcm = lcm(this.denominator, other.denominator);
        int newNumerator = this.numerator * (lcm / this.denominator) - 
                          other.numerator * (lcm / other.denominator);
        return new Fraction(newNumerator, lcm);
    }

    /**
     * 乘法
     */
    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator, 
                           this.denominator * other.denominator);
    }

    /**
     * 除法
     */
    public Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new ArithmeticException("除数不能为0");
        }
        return new Fraction(this.numerator * other.denominator, 
                           this.denominator * other.numerator);
    }

    /**
     * 比较两个分数的大小
     * @return 如果this > other返回正数，相等返回0，小于返回负数
     */
    public int compareTo(Fraction other) {
        int thisNumerator = this.numerator * other.denominator;
        int otherNumerator = other.numerator * this.denominator;
        return Integer.compare(thisNumerator, otherNumerator);
    }

    /**
     * 判断是否为真分数
     */
    public boolean isProperFraction() {
        return Math.abs(numerator) < denominator;
    }

    /**
     * 判断是否为自然数
     */
    public boolean isNaturalNumber() {
        return denominator == 1 && numerator >= 0;
    }

    /**
     * 获取分子
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * 获取分母
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * 转换为字符串表示
     */
    @Override
    public String toString() {
        if (denominator == 1) {
            // 整数
            return String.valueOf(numerator);
        } else if (Math.abs(numerator) > denominator) {
            // 假分数转换为带分数
            int integerPart = numerator / denominator;
            int remainder = Math.abs(numerator) % denominator;
            if (remainder == 0) {
                return String.valueOf(integerPart);
            } else {
                return integerPart + "'" + remainder + "/" + denominator;
            }
        } else {
            // 真分数
            return numerator + "/" + denominator;
        }
    }

    /**
     * 判断两个分数是否相等
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Fraction fraction = (Fraction) obj;
        return numerator == fraction.numerator && denominator == fraction.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}