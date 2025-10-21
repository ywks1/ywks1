package org.example;

import java.util.*;

/**
 * 表示一个算术表达式
 */
public class Expression {
    public enum OperatorType {
        ADD("+"), SUBTRACT("-"), MULTIPLY("×"), DIVIDE("÷");
        
        private final String symbol;
        
        OperatorType(String symbol) {
            this.symbol = symbol;
        }
        
        public String getSymbol() {
            return symbol;
        }
        
        public static OperatorType fromSymbol(String symbol) {
            for (OperatorType op : values()) {
                if (op.getSymbol().equals(symbol)) {
                    return op;
                }
            }
            throw new IllegalArgumentException("未知的运算符: " + symbol);
        }
    }
    
    private ExpressionNode root;
    
    public Expression(ExpressionNode root) {
        this.root = root;
    }
    
    /**
     * 计算表达式的结果
     */
    public String calculateResult() {
        return root.evaluate().toString();
    }
    
    /**
     * 获取表达式的字符串表示
     */
    @Override
    public String toString() {
        return root.toString() + " = ";
    }
    
    /**
     * 获取表达式的规范形式（用于查重）
     */
    public String getCanonicalForm() {
        return root.getCanonicalForm();
    }
    
    /**
     * 表达式节点接口
     */
    public interface ExpressionNode {
        Fraction evaluate();
        String toString();
        String getCanonicalForm();
    }
    
    /**
     * 数值节点
     */
    public static class NumberNode implements ExpressionNode {
        private Fraction value;
        
        public NumberNode(Fraction value) {
            this.value = value;
        }
        
        @Override
        public Fraction evaluate() {
            return value;
        }
        
        @Override
        public String toString() {
            return value.toString();
        }
        
        @Override
        public String getCanonicalForm() {
            return "N" + value.toString();
        }
    }
    
    /**
     * 操作符节点
     */
    public static class OperatorNode implements ExpressionNode {
        private OperatorType operator;
        private ExpressionNode left;
        private ExpressionNode right;
        
        public OperatorNode(OperatorType operator, ExpressionNode left, ExpressionNode right) {
            this.operator = operator;
            this.left = left;
            this.right = right;
        }
        
        @Override
        public Fraction evaluate() {
            Fraction leftValue = left.evaluate();
            Fraction rightValue = right.evaluate();
            
            switch (operator) {
                case ADD:
                    return leftValue.add(rightValue);
                case SUBTRACT:
                    if (leftValue.compareTo(rightValue) < 0) {
                        throw new ArithmeticException("减法运算结果为负数");
                    }
                    return leftValue.subtract(rightValue);
                case MULTIPLY:
                    return leftValue.multiply(rightValue);
                case DIVIDE:
                    if (rightValue.getNumerator() == 0) {
                        throw new ArithmeticException("除数不能为0");
                    }
                    Fraction result = leftValue.divide(rightValue);
                    if (!result.isProperFraction() && !result.isNaturalNumber()) {
                        throw new ArithmeticException("除法运算结果必须是真分数或自然数");
                    }
                    return result;
                default:
                    throw new IllegalStateException("未知的运算符: " + operator);
            }
        }
        
        @Override
        public String toString() {
            String leftStr = needParentheses(left) ? "(" + left.toString() + ")" : left.toString();
            String rightStr = needParentheses(right) ? "(" + right.toString() + ")" : right.toString();
            
            return leftStr + " " + operator.getSymbol() + " " + rightStr;
        }
        
        private boolean needParentheses(ExpressionNode node) {
            if (!(node instanceof OperatorNode)) {
                return false;
            }
            
            OperatorNode opNode = (OperatorNode) node;
            
            // 根据运算符优先级决定是否需要括号
            if ((operator == OperatorType.ADD || operator == OperatorType.SUBTRACT) &&
                (opNode.operator == OperatorType.ADD || opNode.operator == OperatorType.SUBTRACT)) {
                return false;
            }
            
            if ((operator == OperatorType.MULTIPLY || operator == OperatorType.DIVIDE) &&
                (opNode.operator == OperatorType.MULTIPLY || opNode.operator == OperatorType.DIVIDE)) {
                return false;
            }
            
            return true;
        }
        
        @Override
        public String getCanonicalForm() {
            // 对于加法和乘法，交换左右子节点以获得规范形式
            if (operator == OperatorType.ADD || operator == OperatorType.MULTIPLY) {
                String leftForm = left.getCanonicalForm();
                String rightForm = right.getCanonicalForm();
                
                // 按字典序排序，确保相同的表达式有相同的规范形式
                if (leftForm.compareTo(rightForm) > 0) {
                    return operator.getSymbol() + rightForm + leftForm;
                } else {
                    return operator.getSymbol() + leftForm + rightForm;
                }
            } else {
                return operator.getSymbol() + left.getCanonicalForm() + right.getCanonicalForm();
            }
        }
    }
}