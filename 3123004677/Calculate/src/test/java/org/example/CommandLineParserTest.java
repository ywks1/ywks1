package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommandLineParserTest {

    @Test
    public void testGenerateMode() {
        // 测试生成模式参数解析
        String[] args = {"-n", "10", "-r", "20"};
        CommandLineParser parser = new CommandLineParser(args);
        
        try {
            parser.parse();
            assertTrue(parser.isGenerateMode());
            assertFalse(parser.isGradeMode());
            assertEquals(10, parser.getCount());
            assertEquals(20, parser.getRange());
        } catch (Exception e) {
            fail("解析有效参数时不应抛出异常: " + e.getMessage());
        }
    }
    
    @Test
    public void testGradeMode() {
        // 测试判定模式参数解析
        String[] args = {"-e", "test.txt", "-a", "answer.txt"};
        CommandLineParser parser = new CommandLineParser(args);
        
        try {
            parser.parse();
            assertFalse(parser.isGenerateMode());
            assertTrue(parser.isGradeMode());
            assertEquals("test.txt", parser.getExerciseFile());
            assertEquals("answer.txt", parser.getAnswerFile());
        } catch (Exception e) {
            fail("解析有效参数时不应抛出异常: " + e.getMessage());
        }
    }
    
    @Test
    public void testInvalidParameters() {
        // 测试参数不足
        String[] args1 = {"-n", "10"};
        CommandLineParser parser1 = new CommandLineParser(args1);
        
        Exception exception1 = assertThrows(Exception.class, parser1::parse);
        assertTrue(exception1.getMessage().contains("必须指定"));
        
        // 测试无效的数值
        String[] args2 = {"-n", "abc", "-r", "10"};
        CommandLineParser parser2 = new CommandLineParser(args2);
        
        Exception exception2 = assertThrows(Exception.class, parser2::parse);
        assertTrue(exception2.getMessage().contains("必须为整数"));
        
        // 测试负数范围
        String[] args3 = {"-n", "10", "-r", "-5"};
        CommandLineParser parser3 = new CommandLineParser(args3);
        
        Exception exception3 = assertThrows(Exception.class, parser3::parse);
        assertTrue(exception3.getMessage().contains("必须大于0"));
        
        // 测试混合模式
        String[] args4 = {"-n", "10", "-r", "20", "-e", "test.txt"};
        CommandLineParser parser4 = new CommandLineParser(args4);
        
        Exception exception4 = assertThrows(Exception.class, parser4::parse);
        assertTrue(exception4.getMessage().contains("不能同时使用"));
    }
}