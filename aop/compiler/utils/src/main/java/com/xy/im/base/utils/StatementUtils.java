package com.xy.im.base.utils;

/**
 * StatementUtils
 *
 * @author Created by gold on 2020/5/18 14:53
 */
public final class StatementUtils {
    private StatementUtils() {
    }

    /**
     * 生成代码块
     * 支持 $S $N $T $C
     *
     * @param format 表达式
     * @param args   参数
     */
    public static String statement(String format, Object... args) {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        int position = 0;
        int count = format.length();
        for (int i = 0; i < count; i++) {
            char c = format.charAt(i);
            if (c != '$') {
                continue;
            }
            int end = i + 1;
            if (end >= count) {
                continue;
            }
            char endC = format.charAt(end);
            if (endC == 'S') {
                builder.append(format, index, i);
                builder.append("\"%s\"");
                index = end + 1;
                position++;
            } else if (endC == 'N') {
                builder.append(format, index, i);
                builder.append("%s");
                index = end + 1;
                position++;
            } else if (endC == 'T') {
                Object obj = args[position];
                if (!(obj instanceof Class)) {
                    throw new StatementException("使用$T必须是Class！");
                }
                args[position] = ((Class<?>) obj).getSimpleName();

                builder.append(format, index, i);
                builder.append("%s");
                index = end + 1;
                position++;
            } else if (endC == 'C') {
                Object obj = args[position];
                if (!(obj instanceof Class)) {
                    throw new StatementException("使用$C必须是Class！");
                }
                args[position] = ((Class<?>) obj).getName();

                builder.append(format, index, i);
                builder.append("%s");
                index = end + 1;
                position++;
            }
        }

        if (position != args.length) {
            throw new StatementException(String.format("占位符的数量是%s，参数是%s个！", position, args.length));
        }

        if (index < count) {
            builder.append(format, index, count);
        }

        return String.format(builder.toString(), args);
    }

    public static class StatementException extends RuntimeException {
        public StatementException(String message) {
            super(message);
        }
    }

    public static class CodeBuilder {

        public static CodeBuilder builder() {
            return new CodeBuilder();
        }

        private final StringBuilder builder;

        public CodeBuilder() {
            this.builder = new StringBuilder();
        }

        public CodeBuilder(String code) {
            this.builder = new StringBuilder(code);
        }

        public CodeBuilder insertCode(int offset, String code) {
            builder.insert(offset, code);
            return this;
        }

        public CodeBuilder addCode(String code) {
            builder.append(code);
            return this;
        }

        public CodeBuilder insertStatement(int offset, String format, Object... args) {
            builder.insert(offset, statement(format, args));
            return this;
        }

        public CodeBuilder addStatement(String format, Object... args) {
            builder.append(statement(format, args));
            return this;
        }

        public void setLength(int newLength) {
            builder.setLength(newLength);
        }

        public int length() {
            return builder.length();
        }

        public String build() {
            return builder.toString();
        }

        @Override
        public String toString() {
            return build();
        }
    }
}
