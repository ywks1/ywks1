package org.example;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 性能测试类，用于测量和比较优化前后的性能差异
 */
public class PerformanceTest {
    
    /**
     * 主方法，运行性能测试
     */
    public static void main(String[] args) {
        // 测试参数
        int[] counts = {100, 500, 1000, 5000};
        int range = 20;
        int iterations = 3;
        
        System.out.println("性能测试开始...");
        System.out.println("测试环境: " + System.getProperty("os.name") + ", " + 
                          Runtime.getRuntime().availableProcessors() + " 核处理器");
        System.out.println("测试参数: 范围 = " + range + ", 每组迭代 " + iterations + " 次");
        System.out.println("-----------------------------------------");
        System.out.println("题目数量\t单线程(ms)\t多线程(ms)\t性能提升");
        System.out.println("-----------------------------------------");
        
        for (int count : counts) {
            // 测量单线程性能
            long singleThreadTime = testSingleThread(count, range, iterations);
            
            // 测量多线程性能
            long multiThreadTime = testMultiThread(count, range, iterations);
            
            // 计算性能提升
            double improvement = (double) singleThreadTime / multiThreadTime;
            
            // 输出结果
            System.out.printf("%d\t%d\t%d\t%.2f倍%n", 
                    count, singleThreadTime, multiThreadTime, improvement);
        }
        
        System.out.println("-----------------------------------------");
        System.out.println("性能测试完成");
    }
    
    /**
     * 测试单线程性能
     */
    private static long testSingleThread(int count, int range, int iterations) {
        List<Long> times = new ArrayList<>();
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            
            ExpressionGenerator generator = new ExpressionGenerator(range);
            generator.generateExpressions(count);
            
            long endTime = System.nanoTime();
            times.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
        }
        
        // 计算平均时间
        return calculateAverage(times);
    }
    
    /**
     * 测试多线程性能
     */
    private static long testMultiThread(int count, int range, int iterations) {
        List<Long> times = new ArrayList<>();
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            
            PerformanceOptimizer.generateExpressionsInParallel(count, range, 4);
            
            long endTime = System.nanoTime();
            times.add(TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
        }
        
        // 计算平均时间
        return calculateAverage(times);
    }
    
    /**
     * 计算平均时间
     */
    private static long calculateAverage(List<Long> times) {
        long sum = 0;
        for (Long time : times) {
            sum += time;
        }
        return sum / times.size();
    }
}