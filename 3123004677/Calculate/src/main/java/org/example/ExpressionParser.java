package org.example;

import java.util.*;

/**
 * 表达式解析器，用于将字符串形式的表达式解析为表达式对象
 */
public class ExpressionParser {
    private String expression;
    private int position;
    
    /**
     * 解析表达式字符串
     */
    public Expression parse(String expressionStr) {
        if (expressionStr == null || expressionStr.trim().isEmpty()) {
            throw new IllegalArgumentException("表达式不能为空");
        }
        
        this.expression = expressionStr.trim();
        this.position = 0;
        
        Expression.ExpressionNode root = parseExpression();
        
        // 允许表达式末尾有空格
        skipWhitespace();
        
        if (position < expression.length()) {
            throw new IllegalArgumentException("表达式解析失败：未处理的字符: " + 
                    expression.substring(position));
        }
        
        return new Expression(root);
    }
    
    /**
     * 解析表达式
     */
    private Expression.ExpressionNode parseExpression() {
        Expression.ExpressionNode left = parseTerm();
        
        while (position < expression.length()) {
            skipWhitespace(); // 先跳过空格，再检查运算符
            
            if (position >= expression.length()) {
                break;
            }
            
            char c = expression.charAt(position);
            
            if (c == '+' || c == '-') {
                position++; // 跳过运算符
                
                // 跳过空格
                skipWhitespace();
                
                Expression.ExpressionNode right = parseTerm();
                Expression.OperatorType operator = c == '+' ? 
                        Expression.OperatorType.ADD : Expression.OperatorType.SUBTRACT;
                
                left = new Expression.OperatorNode(operator, left, right);
            } else {
                break;
            }
        }
        
        return left;
    }
    
    /**
     * 解析项（乘除法优先级）
     */
    private Expression.ExpressionNode parseTerm() {
        Expression.ExpressionNode left = parseFactor();
        
        while (position < expression.length()) {
            skipWhitespace(); // 先跳过空格，再检查运算符
            
            if (position >= expression.length()) {
                break;
            }
            
            char c = expression.charAt(position);
            
            if (c == '×' || c == '÷') {
                position++; // 跳过运算符
                
                // 跳过空格
                skipWhitespace();
                
                Expression.ExpressionNode right = parseFactor();
                Expression.OperatorType operator = c == '×' ? 
                        Expression.OperatorType.MULTIPLY : Expression.OperatorType.DIVIDE;
                
                left = new Expression.OperatorNode(operator, left, right);
            } else {
                break;
            }
        }
        
        return left;
    }
    
    /**
     * 解析因子（括号、数值）
     */
    private Expression.ExpressionNode parseFactor() {
        skipWhitespace();
        
        char c = expression.charAt(position);
        
        if (c == '(') {
            position++; // 跳过左括号
            skipWhitespace();
            
            Expression.ExpressionNode node = parseExpression();
            
            skipWhitespace();
            if (position >= expression.length() || expression.charAt(position) != ')') {
                throw new IllegalArgumentException("缺少右括号");
            }
            position++; // 跳过右括号
            
            return node;
        } else {
            return parseNumber();
        }
    }
    
    /**
     * 解析数值（自然数或真分数）
     */
    private Expression.NumberNode parseNumber() {
        skipWhitespace();
        
        StringBuilder sb = new StringBuilder();
        boolean hasFraction = false;
        boolean hasInteger = false;
        
        // 解析整数部分
        while (position < expression.length() && Character.isDigit(expression.charAt(position))) {
            sb.append(expression.charAt(position));
            position++;
            hasInteger = true;
        }
        
        // 检查是否是带分数
        if (position < expression.length() && expression.charAt(position) == '\'' && hasInteger) {
            int integerPart = Integer.parseInt(sb.toString());
            position++; // 跳过单引号
            
            // 解析分数部分
            int numerator = parseNumerator();
            int denominator = parseDenominator();
            
            // 计算真分数值
            int totalNumerator = integerPart * denominator + numerator;
            return new Expression.NumberNode(new Fraction(totalNumerator, denominator));
        }
        
        // 检查是否是真分数
        if (position < expression.length() && expression.charAt(position) == '/' && hasInteger) {
            int numerator = Integer.parseInt(sb.toString());
            position++; // 跳过斜杠
            
            int denominator = parseDenominator();
            return new Expression.NumberNode(new Fraction(numerator, denominator));
        }
        
        // 是自然数
        if (hasInteger) {
            int value = Integer.parseInt(sb.toString());
            return new Expression.NumberNode(new Fraction(value));
        }
        
        throw new IllegalArgumentException("无效的数值表达式");
    }
    
    /**
     * 解析分子
     */
    private int parseNumerator() {
        StringBuilder sb = new StringBuilder();
        
        while (position < expression.length() && Character.isDigit(expression.charAt(position))) {
            sb.append(expression.charAt(position));
            position++;
        }
        
        if (sb.length() == 0) {
            throw new IllegalArgumentException("缺少分子");
        }
        
        return Integer.parseInt(sb.toString());
    }
    
    /**
     * 解析分母
     */
    private int parseDenominator() {
        if (position >= expression.length() || expression.charAt(position) != '/') {
            throw new IllegalArgumentException("缺少分数符号");
        }
        position++; // 跳过斜杠
        
        StringBuilder sb = new StringBuilder();
        
        while (position < expression.length() && Character.isDigit(expression.charAt(position))) {
            sb.append(expression.charAt(position));
            position++;
        }
        
        if (sb.length() == 0) {
            throw new IllegalArgumentException("缺少分母");
        }
        
        return Integer.parseInt(sb.toString());
    }
    
    /**
     * 跳过空白字符
     */
    private void skipWhitespace() {
        while (position < expression.length() && 
               Character.isWhitespace(expression.charAt(position))) {
            position++;
        }
    }
}