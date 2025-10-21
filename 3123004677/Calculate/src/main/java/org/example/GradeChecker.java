package org.example;

import java.io.IOException;
import java.util.*;

/**
 * 答案判定和统计类
 */
public class GradeChecker {
    
    /**
     * 检查用户答案与正确答案
     */
    public void checkAnswers(List<String> correctAnswers, List<String> userAnswers) throws IOException {
        FileHandler fileHandler = new FileHandler();
        checkAnswers(correctAnswers, userAnswers, fileHandler);
    }
    
    /**
     * 检查用户答案与正确答案（使用指定的FileHandler）
     */
    public void checkAnswers(List<String> correctAnswers, List<String> userAnswers, FileHandler fileHandler) throws IOException {
        List<Integer> correctIndices = new ArrayList<>();
        List<Integer> wrongIndices = new ArrayList<>();
        
        int minSize = Math.min(correctAnswers.size(), userAnswers.size());
        
        for (int i = 0; i < minSize; i++) {
            String correctAnswer = normalizeAnswer(correctAnswers.get(i));
            String userAnswer = normalizeAnswer(userAnswers.get(i));
            
            if (correctAnswer.equals(userAnswer)) {
                correctIndices.add(i + 1); // 题号从1开始
            } else {
                wrongIndices.add(i + 1); // 题号从1开始
            }
        }
        
        // 如果用户答案数量少于正确答案，将剩余题目标记为错误
        for (int i = minSize; i < correctAnswers.size(); i++) {
            wrongIndices.add(i + 1);
        }
        
        // 写入评分结果
        fileHandler.writeGrade(correctIndices, wrongIndices);
    }
    
    /**
     * 标准化答案格式，去除空格和前导零等
     */
    public String normalizeAnswer(String answer) {
        // 去除所有空格
        String normalized = answer.replaceAll("\\s+", "");
        
        // 尝试解析为分数
        try {
            if (normalized.contains("/")) {
                // 处理带分数
                if (normalized.contains("'")) {
                    String[] parts = normalized.split("'");
                    int integerPart = Integer.parseInt(parts[0]);
                    String[] fractionParts = parts[1].split("/");
                    int numerator = Integer.parseInt(fractionParts[0]);
                    int denominator = Integer.parseInt(fractionParts[1]);
                    
                    // 转换为假分数
                    numerator = integerPart * denominator + numerator;
                    
                    // 约分
                    Fraction fraction = new Fraction(numerator, denominator);
                    return fraction.toString();
                } else {
                    // 处理普通分数
                    String[] parts = normalized.split("/");
                    int numerator = Integer.parseInt(parts[0]);
                    int denominator = Integer.parseInt(parts[1]);
                    
                    // 约分
                    Fraction fraction = new Fraction(numerator, denominator);
                    return fraction.toString();
                }
            } else {
                // 处理整数，去除前导零
                return String.valueOf(Integer.parseInt(normalized));
            }
        } catch (NumberFormatException e) {
            // 如果解析失败，返回原始标准化字符串
            return normalized;
        }
    }
}