package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FractionTest {

    @Test
    public void testFractionCreation() {
        // 测试自然数
        Fraction f1 = new Fraction(5);
        assertEquals(5, f1.getNumerator());
        assertEquals(1, f1.getDenominator());
        
        // 测试真分数
        Fraction f2 = new Fraction(1, 2);
        assertEquals(1, f2.getNumerator());
        assertEquals(2, f2.getDenominator());
        
        // 测试约分
        Fraction f3 = new Fraction(2, 4);
        assertEquals(1, f3.getNumerator());
        assertEquals(2, f3.getDenominator());
        
        // 测试负分数处理
        Fraction f4 = new Fraction(-1, 2);
        assertEquals(-1, f4.getNumerator());
        assertEquals(2, f4.getDenominator());
        
        // 测试分母为负数的处理
        Fraction f5 = new Fraction(1, -2);
        assertEquals(-1, f5.getNumerator());
        assertEquals(2, f5.getDenominator());
    }
    
    @Test
    public void testFractionAddition() {
        // 测试整数加法
        Fraction f1 = new Fraction(3);
        Fraction f2 = new Fraction(5);
        Fraction result1 = f1.add(f2);
        assertEquals(8, result1.getNumerator());
        assertEquals(1, result1.getDenominator());
        
        // 测试分数加法
        Fraction f3 = new Fraction(1, 4);
        Fraction f4 = new Fraction(1, 6);
        Fraction result2 = f3.add(f4);
        assertEquals(5, result2.getNumerator());
        assertEquals(12, result2.getDenominator());
        
        // 测试混合加法
        Fraction f5 = new Fraction(2);
        Fraction f6 = new Fraction(1, 2);
        Fraction result3 = f5.add(f6);
        assertEquals(5, result3.getNumerator());
        assertEquals(2, result3.getDenominator());
    }
    
    @Test
    public void testFractionSubtraction() {
        // 测试整数减法
        Fraction f1 = new Fraction(7);
        Fraction f2 = new Fraction(3);
        Fraction result1 = f1.subtract(f2);
        assertEquals(4, result1.getNumerator());
        assertEquals(1, result1.getDenominator());
        
        // 测试分数减法
        Fraction f3 = new Fraction(3, 4);
        Fraction f4 = new Fraction(1, 4);
        Fraction result2 = f3.subtract(f4);
        assertEquals(1, result2.getNumerator());
        assertEquals(2, result2.getDenominator());
    }
    
    @Test
    public void testFractionMultiplication() {
        // 测试整数乘法
        Fraction f1 = new Fraction(3);
        Fraction f2 = new Fraction(4);
        Fraction result1 = f1.multiply(f2);
        assertEquals(12, result1.getNumerator());
        assertEquals(1, result1.getDenominator());
        
        // 测试分数乘法
        Fraction f3 = new Fraction(2, 3);
        Fraction f4 = new Fraction(3, 4);
        Fraction result2 = f3.multiply(f4);
        assertEquals(1, result2.getNumerator());
        assertEquals(2, result2.getDenominator());
    }
    
    @Test
    public void testFractionDivision() {
        // 测试整数除法
        Fraction f1 = new Fraction(8);
        Fraction f2 = new Fraction(2);
        Fraction result1 = f1.divide(f2);
        assertEquals(4, result1.getNumerator());
        assertEquals(1, result1.getDenominator());
        
        // 测试分数除法
        Fraction f3 = new Fraction(1, 2);
        Fraction f4 = new Fraction(2, 3);
        Fraction result2 = f3.divide(f4);
        assertEquals(3, result2.getNumerator());
        assertEquals(4, result2.getDenominator());
    }
    
    @Test
    public void testFractionComparison() {
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(1, 3);
        Fraction f3 = new Fraction(1, 2);
        
        assertTrue(f1.compareTo(f2) > 0);
        assertTrue(f2.compareTo(f1) < 0);
        assertEquals(0, f1.compareTo(f3));
    }
    
    @Test
    public void testFractionToString() {
        // 测试整数
        Fraction f1 = new Fraction(5);
        assertEquals("5", f1.toString());
        
        // 测试真分数
        Fraction f2 = new Fraction(1, 2);
        assertEquals("1/2", f2.toString());
        
        // 测试带分数
        Fraction f3 = new Fraction(5, 2);
        assertEquals("2'1/2", f3.toString());
    }
}