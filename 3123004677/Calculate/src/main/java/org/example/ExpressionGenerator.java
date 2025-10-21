package org.example;

import java.util.*;

/**
 * 表达式生成器，用于生成四则运算题目
 */
public class ExpressionGenerator {
    private final int range;
    private final Random random;
    private final Set<String> generatedExpressions;
    
    public ExpressionGenerator(int range) {
        this.range = range;
        this.random = new Random();
        this.generatedExpressions = new HashSet<>();
    }
    
    /**
     * 生成指定数量的不重复表达式
     */
    public List<Expression> generateExpressions(int count) {
        List<Expression> expressions = new ArrayList<>();
        int attempts = 0;
        int maxAttempts = count * 100; // 设置最大尝试次数，避免无限循环
        
        while (expressions.size() < count && attempts < maxAttempts) {
            try {
                Expression expr = generateSingleExpression();
                String canonicalForm = expr.getCanonicalForm();
                
                // 检查是否重复
                if (!generatedExpressions.contains(canonicalForm)) {
                    expressions.add(expr);
                    generatedExpressions.add(canonicalForm);
                }
            } catch (ArithmeticException e) {
                // 忽略无效表达式（如除以零、负数结果等）
            }
            
            attempts++;
        }
        
        if (expressions.size() < count) {
            throw new RuntimeException("无法生成" + count + "个不重复的表达式，请尝试增加数值范围");
        }
        
        return expressions;
    }
    
    /**
     * 生成单个表达式
     */
    Expression generateSingleExpression() {
        // 随机决定运算符的数量（1-3个）
        int operatorCount = random.nextInt(3) + 1;
        
        // 生成表达式树
        Expression.ExpressionNode root = generateExpressionTree(operatorCount);
        
        // 验证表达式是否有效（计算一次，如果有问题会抛出异常）
        root.evaluate();
        
        return new Expression(root);
    }
    
    /**
     * 生成表达式树
     */
    private Expression.ExpressionNode generateExpressionTree(int operatorCount) {
        if (operatorCount == 0) {
            return generateNumberNode();
        }
        
        // 随机决定左子树的运算符数量
        int leftOperatorCount = random.nextInt(operatorCount);
        int rightOperatorCount = operatorCount - 1 - leftOperatorCount;
        
        Expression.ExpressionNode left = leftOperatorCount > 0 ? 
                generateExpressionTree(leftOperatorCount) : generateNumberNode();
        Expression.ExpressionNode right = rightOperatorCount > 0 ? 
                generateExpressionTree(rightOperatorCount) : generateNumberNode();
        
        // 随机选择运算符
        Expression.OperatorType operator = getRandomOperator();
        
        return new Expression.OperatorNode(operator, left, right);
    }
    
    /**
     * 生成数值节点
     */
    private Expression.NumberNode generateNumberNode() {
        // 随机决定是生成自然数还是真分数
        boolean generateFraction = random.nextBoolean();
        
        if (generateFraction && range > 1) {
            // 生成真分数
            int denominator = random.nextInt(range - 1) + 2; // 分母范围 [2, range)
            int numerator = random.nextInt(denominator); // 分子范围 [0, denominator)
            if (numerator == 0) numerator = 1; // 确保分子不为0
            
            return new Expression.NumberNode(new Fraction(numerator, denominator));
        } else {
            // 生成自然数
            int value = random.nextInt(range); // 范围 [0, range)
            return new Expression.NumberNode(new Fraction(value));
        }
    }
    
    /**
     * 随机获取一个运算符
     */
    private Expression.OperatorType getRandomOperator() {
        Expression.OperatorType[] operators = Expression.OperatorType.values();
        return operators[random.nextInt(operators.length)];
    }
}