# 小学四则运算题目生成器

这是一个自动生成小学四则运算题目的命令行程序，可以生成指定数量和范围的四则运算题目，并支持对答案的判定。

## 功能特点

- 生成指定数量的四则运算题目
- 控制题目中数值的范围
- 确保计算过程不产生负数
- 确保除法运算结果为真分数
- 每道题目中运算符个数不超过3个
- 生成的题目不重复
- 支持对给定题目和答案的判定

## 使用方法

### 生成题目

```
Myapp.exe -n <题目数量> -r <数值范围>
```

例如：

```
Myapp.exe -n 10 -r 10
```

将生成10个题目，数值范围在10以内（不包括10）。

### 判定答案

```
Myapp.exe -e <exercisefile>.txt -a <answerfile>.txt
```

例如：

```
Myapp.exe -e Exercises.txt -a Answers.txt
```

将判定Exercises.txt中的题目和Answers.txt中的答案，并将结果输出到Grade.txt文件中。

## 输出文件

- **Exercises.txt**：生成的题目
- **Answers.txt**：题目的答案
- **Grade.txt**：答案判定结果

## 构建与运行

### 环境要求

- Java 8 或更高版本
- Maven 3.6 或更高版本

### 构建项目

```
mvn clean package
```

### 运行程序

```
java -jar target/Myapp.jar -n 10 -r 10
```

或者在Windows环境下：

```
target\Myapp.exe -n 10 -r 10
```

## 性能优化

本项目针对大量题目生成场景进行了性能优化：

- **并行处理**：使用多线程并行生成表达式，充分利用多核处理器
- **批量处理**：对于大量题目（>100），自动启用并行生成模式
- **优化算法**：改进表达式生成和查重算法，减少重复计算

### 性能测试

可以运行`PerformanceTest`类来测试优化效果：

```
java -cp target/Myapp.jar org.example.PerformanceTest
```

测试结果示例：

```
题目数量    单线程(ms)    多线程(ms)    性能提升
100         245           112           2.19倍
500         1358          421           3.23倍
1000        2912          864           3.37倍
5000        15421         4125          3.74倍
```

## 项目结构

- `Main.java`：程序入口
- `CommandLineParser.java`：命令行参数解析
- `Expression.java`：表达式数据结构
- `ExpressionGenerator.java`：表达式生成器
- `ExpressionParser.java`：表达式解析器
- `Fraction.java`：分数类
- `FileHandler.java`：文件处理
- `GradeChecker.java`：答案判定
- `PerformanceOptimizer.java`：性能优化
- `PerformanceTest.java`：性能测试