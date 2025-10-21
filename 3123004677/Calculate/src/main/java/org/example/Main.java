package org.example;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // 解析命令行参数
        CommandLineParser parser = new CommandLineParser(args);
        try {
            parser.parse();
            if (parser.isGenerateMode()) {
                // 生成题目模式
                int count = parser.getCount();
                int range = parser.getRange();
                
                // 生成题目（使用性能优化）
                List<Expression> expressions;
                if (count > 100) {
                    // 大量题目使用并行生成
                    System.out.println("使用并行处理生成" + count + "道题目...");
                    expressions = PerformanceOptimizer.generateExpressionsInParallel(count, range, 4);
                } else {
                    // 少量题目使用普通生成
                    ExpressionGenerator generator = new ExpressionGenerator(range);
                    expressions = generator.generateExpressions(count);
                }
                
                // 计算答案
                List<String> answers = new ArrayList<>();
                for (Expression expr : expressions) {
                    answers.add(expr.calculateResult());
                }
                
                // 输出到文件
                FileHandler fileHandler = new FileHandler();
                fileHandler.writeExpressions(expressions);
                fileHandler.writeAnswers(answers);
                
                System.out.println("已生成" + count + "道题目，范围为" + range + "，结果已保存到Exercises.txt和Answers.txt");
            } else if (parser.isGradeMode()) {
                // 判定答案模式
                String exerciseFile = parser.getExerciseFile();
                String answerFile = parser.getAnswerFile();
                
                // 读取文件
                FileHandler fileHandler = new FileHandler();
                List<String> exercises = fileHandler.readExercises(exerciseFile);
                List<String> userAnswers = fileHandler.readAnswers(answerFile);
                
                // 计算正确答案
                List<String> correctAnswers = new ArrayList<>();
                ExpressionParser exprParser = new ExpressionParser();
                for (String exercise : exercises) {
                    Expression expr = exprParser.parse(exercise.replace(" = ", ""));
                    correctAnswers.add(expr.calculateResult());
                }
                
                // 判定对错
                GradeChecker checker = new GradeChecker();
                checker.checkAnswers(correctAnswers, userAnswers);
                
                System.out.println("答案判定完成，结果已保存到Grade.txt");
            }
        } catch (Exception e) {
            System.out.println("错误: " + e.getMessage());
            System.out.println("用法: \n" +
                    "生成题目: Myapp.exe -n <题目数量> -r <数值范围>\n" +
                    "判定答案: Myapp.exe -e <exercisefile>.txt -a <answerfile>.txt");
        }
    }
}