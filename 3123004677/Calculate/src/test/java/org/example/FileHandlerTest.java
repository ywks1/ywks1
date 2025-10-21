package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FileHandlerTest {

    @TempDir
    Path tempDir;
    
    @Test
    public void testWriteAndReadExpressions() throws IOException {
        // 创建测试表达式
        Expression.ExpressionNode node1 = new Expression.NumberNode(new Fraction(3));
        Expression.ExpressionNode node2 = new Expression.NumberNode(new Fraction(4));
        Expression.ExpressionNode addNode = new Expression.OperatorNode(
                Expression.OperatorType.ADD, node1, node2);
        
        Expression expr = new Expression(addNode);
        List<Expression> expressions = Arrays.asList(expr);
        
        // 设置临时文件路径
        File exercisesFile = tempDir.resolve("Exercises.txt").toFile();
        FileHandler fileHandler = new FileHandler();
        fileHandler.setFilePaths(exercisesFile.getAbsolutePath(), null, null);
        
        // 写入文件
        fileHandler.writeExpressions(expressions);
        
        // 验证文件存在
        assertTrue(exercisesFile.exists());
        
        // 读取文件内容
        List<String> lines = Files.readAllLines(exercisesFile.toPath());
        assertEquals(1, lines.size());
        assertEquals("1. 3 + 4 = ", lines.get(0));
    }
    
    @Test
    public void testWriteAndReadAnswers() throws IOException {
        // 创建测试答案
        List<String> answers = Arrays.asList("7", "12", "3/4");
        
        // 设置临时文件路径
        File answersFile = tempDir.resolve("Answers.txt").toFile();
        FileHandler fileHandler = new FileHandler();
        fileHandler.setFilePaths(null, answersFile.getAbsolutePath(), null);
        
        // 写入文件
        fileHandler.writeAnswers(answers);
        
        // 验证文件存在
        assertTrue(answersFile.exists());
        
        // 读取文件内容
        List<String> lines = Files.readAllLines(answersFile.toPath());
        assertEquals(3, lines.size());
        assertEquals("1. 7", lines.get(0));
        assertEquals("2. 12", lines.get(1));
        assertEquals("3. 3/4", lines.get(2));
    }
    
    @Test
    public void testWriteGrade() throws IOException {
        // 创建测试结果
        List<Integer> correctIndices = Arrays.asList(1, 3, 5);
        List<Integer> wrongIndices = Arrays.asList(2, 4);
        
        // 设置临时文件路径
        File gradeFile = tempDir.resolve("Grade.txt").toFile();
        FileHandler fileHandler = new FileHandler();
        fileHandler.setFilePaths(null, null, gradeFile.getAbsolutePath());
        
        // 写入文件
        fileHandler.writeGrade(correctIndices, wrongIndices);
        
        // 验证文件存在
        assertTrue(gradeFile.exists());
        
        // 读取文件内容
        List<String> lines = Files.readAllLines(gradeFile.toPath());
        assertEquals(2, lines.size());
        assertEquals("Correct: 3 (1, 3, 5)", lines.get(0));
        assertEquals("Wrong: 2 (2, 4)", lines.get(1));
    }
}