package org.example;

import java.io.*;
import java.util.*;

/**
 * 文件处理类，负责读写题目、答案和评分结果
 */
public class FileHandler {
    private String EXERCISES_FILE = "Exercises.txt";
    private String ANSWERS_FILE = "Answers.txt";
    private String GRADE_FILE = "Grade.txt";
    
    public FileHandler() {
        // 默认构造函数
    }
    
    /**
     * 写入表达式到文件
     */
    public void writeExpressions(List<Expression> expressions) throws IOException {
        File file = new File(EXERCISES_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < expressions.size(); i++) {
                writer.write((i + 1) + ". " + expressions.get(i).toString());
                writer.newLine();
            }
        }
    }
    
    /**
     * 写入答案到文件
     */
    public void writeAnswers(List<String> answers) throws IOException {
        File file = new File(ANSWERS_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < answers.size(); i++) {
                writer.write((i + 1) + ". " + answers.get(i));
                writer.newLine();
            }
        }
    }
    
    /**
     * 读取题目文件
     */
    public List<String> readExercises(String filePath) throws IOException {
        List<String> exercises = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // 去掉题号，只保留表达式
                    int dotIndex = line.indexOf('.');
                    if (dotIndex != -1 && dotIndex + 1 < line.length()) {
                        exercises.add(line.substring(dotIndex + 1).trim());
                    } else {
                        exercises.add(line.trim());
                    }
                }
            }
        }
        return exercises;
    }
    
    /**
     * 读取题目文件（使用默认路径）
     */
    public List<String> readExercises() throws IOException {
        return readExercises(EXERCISES_FILE);
    }
    
    /**
     * 读取答案文件
     */
    public List<String> readAnswers(String filePath) throws IOException {
        List<String> answers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // 去掉题号，只保留答案
                    int dotIndex = line.indexOf('.');
                    if (dotIndex != -1 && dotIndex + 1 < line.length()) {
                        answers.add(line.substring(dotIndex + 1).trim());
                    } else {
                        answers.add(line.trim());
                    }
                }
            }
        }
        return answers;
    }
    
    /**
     * 读取答案文件（使用默认路径）
     */
    public List<String> readAnswers() throws IOException {
        return readAnswers(ANSWERS_FILE);
    }
    
    /**
     * 写入评分结果
     */
    public void writeGrade(List<Integer> correctIndices, List<Integer> wrongIndices) throws IOException {
        File file = new File(GRADE_FILE);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // 写入正确题目
            writer.write("Correct: " + correctIndices.size());
            if (!correctIndices.isEmpty()) {
                writer.write(" (" + formatIndices(correctIndices) + ")");
            }
            writer.newLine();
            
            // 写入错误题目
            writer.write("Wrong: " + wrongIndices.size());
            if (!wrongIndices.isEmpty()) {
                writer.write(" (" + formatIndices(wrongIndices) + ")");
            }
            writer.newLine();
        }
    }
    
    /**
     * 格式化索引列表为字符串
     */
    private String formatIndices(List<Integer> indices) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indices.size(); i++) {
            sb.append(indices.get(i));
            if (i < indices.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    
    /**
     * 设置文件路径，用于测试
     */
    public void setFilePaths(String exercisesFile, String answersFile, String gradeFile) {
        this.EXERCISES_FILE = exercisesFile;
        this.ANSWERS_FILE = answersFile;
        this.GRADE_FILE = gradeFile;
    }
}