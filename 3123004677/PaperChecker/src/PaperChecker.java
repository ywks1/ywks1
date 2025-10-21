import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 论文查重系统
 * 功能：计算两篇论文的文本相似度
 * 输入：原文文件路径、抄袭版文件路径、输出结果文件路径
 * 输出：相似度（0.00-1.00之间的浮点数）
 */
public class PaperChecker {

    /**
     * 主方法 - 程序入口点
     * @param args 命令行参数，包含三个文件路径：
     *             args[0]: 原文文件绝对路径
     *             args[1]: 抄袭版论文文件绝对路径
     *             args[2]: 输出答案文件绝对路径
     */
    public static void main(String[] args) {
        // 检查参数数量是否正确
        if (args.length != 3) {
            System.err.println("使用方法: java -jar main.jar [原文文件] [抄袭版论文文件] [答案文件]");
            System.exit(1); // 参数错误，退出程序
        }

        // 从命令行参数获取文件路径
        String originalPath = args[0];    // 原文文件路径
        String copiedPath = args[1];      // 抄袭版文件路径
        String outputPath = args[2];      // 输出结果文件路径

        try {
            // 检查文件是否存在，避免文件不存在导致的异常
            if (!Files.exists(Paths.get(originalPath))) {
                throw new IOException("原文文件不存在: " + originalPath);
            }
            if (!Files.exists(Paths.get(copiedPath))) {
                throw new IOException("抄袭版论文文件不存在: " + copiedPath);
            }

            // 读取文件内容到字符串
            String originalText = readFile(originalPath);
            String copiedText = readFile(copiedPath);

            // 计算两篇文本的相似度
            double similarity = calculateSimilarity(originalText, copiedText);

            // 将相似度结果写入输出文件
            writeResult(outputPath, similarity);

            // 在控制台输出结果（便于调试和查看）
            System.out.println("查重完成！相似度: " + String.format("%.2f", similarity));

        } catch (IOException e) {
            // 处理文件读写相关的异常
            System.err.println("错误: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            // 处理其他未知异常
            System.err.println("处理过程中发生错误: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @return 文件内容的字符串形式
     * @throws IOException 当文件不存在或读取失败时抛出
     */
    private static String readFile(String filePath) throws IOException {
        // 使用NIO的Files类一次性读取所有字节，然后转换为UTF-8编码的字符串
        return new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
    }

    /**
     * 计算两段文本的相似度
     * @param text1 第一段文本（原文）
     * @param text2 第二段文本（抄袭版）
     * @return 相似度值，范围0.0-1.0
     */
    private static double calculateSimilarity(String text1, String text2) {
        // 检查文本是否为空，空文本相似度为0
        if (text1.isEmpty() || text2.isEmpty()) {
            return 0.0;
        }

        // 文本预处理：分词、去除停用词等
        List<String> words1 = preprocessText(text1); // 处理原文
        List<String> words2 = preprocessText(text2); // 处理抄袭版

        // 输出分词结果（调试用）
        System.out.println("原文分词: " + words1);
        System.out.println("抄袭版分词: " + words2);

        // 处理特殊情况：两段文本都为空或其中一段为空
        if (words1.isEmpty() && words2.isEmpty()) {
            return 1.0; // 两段都为空，认为完全相似
        } else if (words1.isEmpty() || words2.isEmpty()) {
            return 0.0; // 其中一段为空，认为完全不相似
        }

        // 使用余弦相似度算法计算相似度
        return computeCosineSimilarity(words1, words2);
    }

    /**
     * 文本预处理方法
     * 功能：清洗文本、分词、去除停用词
     * @param text 原始文本
     * @return 处理后的词汇列表
     */
    private static List<String> preprocessText(String text) {
        // 1. 文本清洗：移除非中文标点符号，保留中文字符
        // [\\p{P}&&[^\\u4e00-\\u9fa5]] 匹配所有非中文的标点符号
        // \\s+ 匹配一个或多个空白字符
        String cleanedText = text.replaceAll("[\\p{P}&&[^\\u4e00-\\u9fa5]]", " ") // 移除非中文标点
                .replaceAll("\\s+", " ") // 合并多个连续空格为单个空格
                .trim(); // 去除首尾空格

        // 如果清洗后文本为空，返回空列表
        if (cleanedText.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 中文分词：按字符分割（简单的一元分词）
        // 对于中文，每个汉字作为一个词汇单元
        List<String> characters = new ArrayList<>();
        for (char c : cleanedText.toCharArray()) {
            if (c != ' ') { // 跳过空格字符
                characters.add(String.valueOf(c)); // 将字符转换为字符串并添加到列表
            }
        }

        // 3. 定义停用词列表（这些词在相似度计算中权重较低或无关紧要）
        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "的", "了", "在", "是", "我", "有", "和", "就", "不", "人", "都", "一"
        ));

        // 4. 过滤处理：去除停用词和空字符串
        return characters.stream()
                .filter(word -> !stopWords.contains(word) && word.length() > 0) // 过滤条件
                .collect(Collectors.toList()); // 收集为列表
    }

    /**
     * 计算余弦相似度
     * 基于词频向量计算两个文本的余弦相似度
     * @param words1 第一个文本的词汇列表
     * @param words2 第二个文本的词汇列表
     * @return 余弦相似度值
     */
    private static double computeCosineSimilarity(List<String> words1, List<String> words2) {
        // 获取两个文本的所有不重复词汇（词汇表）
        Set<String> allWords = new HashSet<>();
        allWords.addAll(words1); // 添加第一个文本的词汇
        allWords.addAll(words2); // 添加第二个文本的词汇

        // 创建两个词频向量（Map形式）
        // key: 词汇, value: 该词汇在文本中出现的次数
        Map<String, Integer> vector1 = new HashMap<>(); // 原文词频向量
        Map<String, Integer> vector2 = new HashMap<>(); // 抄袭版词频向量

        // 初始化词频向量，所有词汇频率初始化为0
        for (String word : allWords) {
            vector1.put(word, 0);
            vector2.put(word, 0);
        }

        // 统计第一个文本中每个词汇的出现次数
        for (String word : words1) {
            vector1.put(word, vector1.get(word) + 1); // 词频加1
        }

        // 统计第二个文本中每个词汇的出现次数
        for (String word : words2) {
            vector2.put(word, vector2.get(word) + 1); // 词频加1
        }

        // 计算余弦相似度的三个分量：
        double dotProduct = 0.0;   // 点积（向量内积）
        double magnitude1 = 0.0;   // 第一个向量的模长
        double magnitude2 = 0.0;   // 第二个向量的模长

        // 遍历所有词汇，计算点积和模长
        for (String word : allWords) {
            int freq1 = vector1.get(word); // 词汇在原文中的频率
            int freq2 = vector2.get(word); // 词汇在抄袭版中的频率

            dotProduct += freq1 * freq2;           // 累加点积
            magnitude1 += Math.pow(freq1, 2);      // 累加第一个向量模长的平方
            magnitude2 += Math.pow(freq2, 2);      // 累加第二个向量模长的平方
        }

        // 避免除零错误（如果某个向量模长为0）
        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0; // 模长为0，相似度为0
        }

        // 计算余弦相似度：点积 / (模长1 * 模长2)
        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }

    /**
     * 将结果写入文件
     * @param outputPath 输出文件路径
     * @param similarity 相似度值
     * @throws IOException 当文件写入失败时抛出
     */
    private static void writeResult(String outputPath, double similarity) throws IOException {
        // 确保输出目录存在：获取输出文件的父目录路径
        Path outputDir = Paths.get(outputPath).getParent();
        if (outputDir != null && !Files.exists(outputDir)) {
            Files.createDirectories(outputDir); // 创建不存在的目录
        }

        // 格式化相似度为两位小数
        String result = String.format("%.2f", similarity);

        // 将结果写入文件（UTF-8编码）
        Files.write(Paths.get(outputPath), result.getBytes("UTF-8"));
    }
}