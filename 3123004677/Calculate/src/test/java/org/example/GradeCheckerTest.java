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

public class GradeCheckerTest {

    @TempDir
    Path tempDir;
    
    @Test
    public void testCheckAnswers() throws IOException {
        // Create test data
        List<String> correctAnswers = Arrays.asList("7", "12", "3/4", "1/2", "10");
        List<String> userAnswers = Arrays.asList("7", "13", "3/4", "2/4", "10");
        
        // Set up temporary file path
        File gradeFile = tempDir.resolve("Grade.txt").toFile();
        FileHandler fileHandler = new FileHandler();
        fileHandler.setFilePaths(null, null, gradeFile.getAbsolutePath());
        
        // Execute answer checking
        GradeChecker checker = new GradeChecker();
        checker.checkAnswers(correctAnswers, userAnswers, fileHandler);
        
        // Verify file exists
        assertTrue(gradeFile.exists());
        
        // Verify file content
        List<String> lines = Files.readAllLines(gradeFile.toPath());
        assertEquals(2, lines.size());
        assertEquals("Correct: 4 (1, 3, 4, 5)", lines.get(0));
        assertEquals("Wrong: 1 (2)", lines.get(1));
    }
    
    @Test
    public void testNormalizeAnswer() throws IOException {
        // 创建测试数据 - 不同格式但等价的答案
        List<String> correctAnswers = Arrays.asList("7", "1/2", "2'1/2");
        List<String> userAnswers = Arrays.asList("007", "1 / 2", "2' 1/2");
        
        // 设置临时文件路径
        File gradeFile = tempDir.resolve("Grade.txt").toFile();
        FileHandler fileHandler = new FileHandler();
        fileHandler.setFilePaths(null, null, gradeFile.getAbsolutePath());
        
        // 执行答案检查
        GradeChecker checker = new GradeChecker();
        checker.checkAnswers(correctAnswers, userAnswers, fileHandler);
        
        // 验证文件存在
        assertTrue(gradeFile.exists());
        
        // 读取文件内容
        List<String> lines = Files.readAllLines(gradeFile.toPath());
        assertEquals(2, lines.size());
        assertEquals("Correct: 3 (1, 2, 3)", lines.get(0));
        assertEquals("Wrong: 0", lines.get(1));
    }
    
    @Test
    public void testMissingAnswers() throws IOException {
        // 创建测试数据 - 用户答案少于正确答案
        List<String> correctAnswers = Arrays.asList("7", "12", "3/4", "1/2", "10");
        List<String> userAnswers = Arrays.asList("7", "12", "3/4");
        
        // 设置临时文件路径
        File gradeFile = tempDir.resolve("Grade.txt").toFile();
        FileHandler fileHandler = new FileHandler();
        fileHandler.setFilePaths(null, null, gradeFile.getAbsolutePath());
        
        // 执行答案检查
        GradeChecker checker = new GradeChecker();
        checker.checkAnswers(correctAnswers, userAnswers, fileHandler);
        
        // 验证文件存在
        assertTrue(gradeFile.exists());
        
        // 读取文件内容
        List<String> lines = Files.readAllLines(gradeFile.toPath());
        assertEquals(2, lines.size());
        assertEquals("Correct: 3 (1, 2, 3)", lines.get(0));
        assertEquals("Wrong: 2 (4, 5)", lines.get(1));
    }
}