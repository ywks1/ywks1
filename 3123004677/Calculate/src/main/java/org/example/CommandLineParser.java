package org.example;

public class CommandLineParser {
    private String[] args;
    private boolean generateMode = false;
    private boolean gradeMode = false;
    private int count = 0;
    private int range = 0;
    private String exerciseFile = "";
    private String answerFile = "";

    public CommandLineParser(String[] args) {
        this.args = args;
    }

    public void parse() throws Exception {
        if (args.length < 2) {
            throw new Exception("参数不足");
        }

        // 解析参数
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-n":
                    if (i + 1 < args.length) {
                        try {
                            count = Integer.parseInt(args[i + 1]);
                            generateMode = true;
                            i++;
                        } catch (NumberFormatException e) {
                            throw new Exception("-n参数必须为整数");
                        }
                    } else {
                        throw new Exception("-n参数缺少值");
                    }
                    break;
                case "-r":
                    if (i + 1 < args.length) {
                        try {
                            range = Integer.parseInt(args[i + 1]);
                            if (range <= 0) {
                                throw new Exception("-r参数必须大于0");
                            }
                            i++;
                        } catch (NumberFormatException e) {
                            throw new Exception("-r参数必须为整数");
                        }
                    } else {
                        throw new Exception("-r参数缺少值");
                    }
                    break;
                case "-e":
                    if (i + 1 < args.length) {
                        exerciseFile = args[i + 1];
                        gradeMode = true;
                        i++;
                    } else {
                        throw new Exception("-e参数缺少值");
                    }
                    break;
                case "-a":
                    if (i + 1 < args.length) {
                        answerFile = args[i + 1];
                        i++;
                    } else {
                        throw new Exception("-a参数缺少值");
                    }
                    break;
                default:
                    throw new Exception("未知参数: " + args[i]);
            }
        }

        // 验证参数组合的有效性
        if (generateMode && gradeMode) {
            throw new Exception("不能同时使用生成模式和判定模式");
        }

        if (generateMode) {
            if (count <= 0) {
                throw new Exception("题目数量必须大于0");
            }
            // 确保同时提供了-n和-r参数
            if (range == 0) {
                throw new Exception("必须指定-r参数");
            }
            if (range <= 0) {
                throw new Exception("数值范围必须大于0");
            }
        } else if (gradeMode) {
            if (exerciseFile.isEmpty()) {
                throw new Exception("必须指定题目文件");
            }
            if (answerFile.isEmpty()) {
                throw new Exception("必须指定答案文件");
            }
        } else {
            throw new Exception("必须指定-n和-r参数或-e和-a参数");
        }
    }

    public boolean isGenerateMode() {
        return generateMode;
    }

    public boolean isGradeMode() {
        return gradeMode;
    }

    public int getCount() {
        return count;
    }

    public int getRange() {
        return range;
    }

    public String getExerciseFile() {
        return exerciseFile;
    }

    public String getAnswerFile() {
        return answerFile;
    }
}