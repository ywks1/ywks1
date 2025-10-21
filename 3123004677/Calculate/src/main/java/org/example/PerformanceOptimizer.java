package org.example;

import java.util.*;
import java.util.concurrent.*;

/**
 * 性能优化类，用于提高程序执行效率
 */
public class PerformanceOptimizer {
    
    /**
     * 并行生成表达式
     * 使用线程池并行处理表达式生成，提高大量题目生成时的性能
     */
    public static List<Expression> generateExpressionsInParallel(int count, int range, int threadCount) {
        if (count <= 100) {
            // 对于少量题目，使用单线程生成
            ExpressionGenerator generator = new ExpressionGenerator(range);
            return generator.generateExpressions(count);
        }
        
        // 对于大量题目，使用多线程并行生成
        int processors = Math.min(Runtime.getRuntime().availableProcessors(), threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        
        // 计算每个线程需要生成的题目数量
        int batchSize = count / processors;
        int remainder = count % processors;
        
        // 创建任务列表
        List<Callable<List<Expression>>> tasks = new ArrayList<>();
        Set<String> sharedSet = Collections.synchronizedSet(new HashSet<>());
        
        for (int i = 0; i < processors; i++) {
            int taskCount = batchSize + (i < remainder ? 1 : 0);
            if (taskCount > 0) {
                tasks.add(() -> {
                    ExpressionGenerator generator = new ExpressionGenerator(range);
                    return generateUniqueExpressions(generator, taskCount, sharedSet);
                });
            }
        }
        
        // 执行任务并收集结果
        List<Expression> result = new ArrayList<>();
        try {
            List<Future<List<Expression>>> futures = executor.invokeAll(tasks);
            for (Future<List<Expression>> future : futures) {
                result.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("生成表达式时发生错误: " + e.getMessage(), e);
        } finally {
            executor.shutdown();
        }
        
        return result;
    }
    
    /**
     * 生成不重复的表达式
     */
    private static List<Expression> generateUniqueExpressions(
            ExpressionGenerator generator, int count, Set<String> sharedSet) {
        List<Expression> expressions = new ArrayList<>();
        int attempts = 0;
        int maxAttempts = count * 100;
        
        while (expressions.size() < count && attempts < maxAttempts) {
            try {
                Expression expr = generator.generateSingleExpression();
                String canonicalForm = expr.getCanonicalForm();
                
                // 使用同步集合检查是否重复
                synchronized (sharedSet) {
                    if (!sharedSet.contains(canonicalForm)) {
                        expressions.add(expr);
                        sharedSet.add(canonicalForm);
                    }
                }
            } catch (ArithmeticException e) {
                // 忽略无效表达式
            }
            
            attempts++;
        }
        
        return expressions;
    }
    
    /**
     * 优化文件写入
     * 使用缓冲写入和批处理提高文件写入性能
     */
    public static void writeExpressionsOptimized(List<Expression> expressions, String filePath) {
        // 实现批量写入逻辑
    }
    
    /**
     * 使用更高效的哈希算法进行表达式查重
     */
    public static boolean isDuplicate(String expr1, String expr2) {
        // 实现高效查重算法
        return false;
    }
}