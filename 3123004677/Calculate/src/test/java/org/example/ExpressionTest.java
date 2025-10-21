package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpressionTest {

    @Test
    public void testExpressionEvaluation() {
        // 测试简单加法
        Expression.ExpressionNode node1 = new Expression.NumberNode(new Fraction(3));
        Expression.ExpressionNode node2 = new Expression.NumberNode(new Fraction(4));
        Expression.ExpressionNode addNode = new Expression.OperatorNode(
                Expression.OperatorType.ADD, node1, node2);
        
        Expression expr1 = new Expression(addNode);
        assertEquals("7", expr1.calculateResult());
        
        // 测试简单减法
        Expression.ExpressionNode subtractNode = new Expression.OperatorNode(
                Expression.OperatorType.SUBTRACT, node1, new Expression.NumberNode(new Fraction(2)));
        
        Expression expr2 = new Expression(subtractNode);
        assertEquals("1", expr2.calculateResult());
        
        // 测试简单乘法
        Expression.ExpressionNode multiplyNode = new Expression.OperatorNode(
                Expression.OperatorType.MULTIPLY, node1, node2);
        
        Expression expr3 = new Expression(multiplyNode);
        assertEquals("12", expr3.calculateResult());
        
        // 测试简单除法
        Expression.ExpressionNode divideNode = new Expression.OperatorNode(
                Expression.OperatorType.DIVIDE, node1, node2);
        
        Expression expr4 = new Expression(divideNode);
        assertEquals("3/4", expr4.calculateResult());
        
        // 测试复杂表达式
        // (3 + 4) × 2
        Expression.ExpressionNode complexNode = new Expression.OperatorNode(
                Expression.OperatorType.MULTIPLY, 
                addNode, 
                new Expression.NumberNode(new Fraction(2)));
        
        Expression expr5 = new Expression(complexNode);
        assertEquals("14", expr5.calculateResult());
    }
    
    @Test
    public void testExpressionToString() {
        // 测试简单表达式
        Expression.ExpressionNode node1 = new Expression.NumberNode(new Fraction(3));
        Expression.ExpressionNode node2 = new Expression.NumberNode(new Fraction(4));
        Expression.ExpressionNode addNode = new Expression.OperatorNode(
                Expression.OperatorType.ADD, node1, node2);
        
        Expression expr1 = new Expression(addNode);
        assertEquals("3 + 4 = ", expr1.toString());
        
        // 测试带括号的表达式
        // (3 + 4) × 2
        Expression.ExpressionNode complexNode = new Expression.OperatorNode(
                Expression.OperatorType.MULTIPLY, 
                addNode, 
                new Expression.NumberNode(new Fraction(2)));
        
        Expression expr2 = new Expression(complexNode);
        assertEquals("(3 + 4) × 2 = ", expr2.toString());
    }
    
    @Test
    public void testExpressionGenerator() {
        ExpressionGenerator generator = new ExpressionGenerator(10);
        List<Expression> expressions = generator.generateExpressions(5);
        
        // 验证生成的表达式数量
        assertEquals(5, expressions.size());
        
        // 验证表达式不重复
        Set<String> uniqueForms = new HashSet<>();
        for (Expression expr : expressions) {
            String canonicalForm = expr.getCanonicalForm();
            uniqueForms.add(canonicalForm);
        }
        assertEquals(5, uniqueForms.size());
        
        // 验证每个表达式都能正确计算
        for (Expression expr : expressions) {
            try {
                String result = expr.calculateResult();
                assertNotNull(result);
                assertFalse(result.isEmpty());
            } catch (Exception e) {
                fail("表达式计算失败: " + expr.toString() + ", 错误: " + e.getMessage());
            }
        }
    }
    
    @Test
    public void testExpressionParser() {
        ExpressionParser parser = new ExpressionParser();
        
        // 测试简单表达式
        Expression expr1 = parser.parse("3 + 4");
        assertEquals("7", expr1.calculateResult());
        
        // 测试带括号的表达式
        Expression expr2 = parser.parse("(3 + 4) × 2");
        assertEquals("14", expr2.calculateResult());
        
        // 测试带分数的表达式
        Expression expr3 = parser.parse("1/2 + 1/3");
        assertEquals("5/6", expr3.calculateResult());
    }
}