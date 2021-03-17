package com.xy.im.base.jctree;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.xy.im.base.utils.StatementUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 构建代码块
 *
 * @author Created by gold on 2019-09-10 12:14
 */
public class JCBlock {

    public static Builder create() {
        return new Builder(null);
    }

    public static Builder create(Optional<BlockStmt> optional) {
        return optional.map(Builder::new).orElseGet(() -> new Builder(null));
    }

    private List<String> statements;

    public BlockStmt generate() {
        StringBuilder builder = new StringBuilder("{");

        for (String str : statements) {
            builder.append(str);
        }

        builder.append("}");

        return StaticJavaParser.parseBlock(builder.toString());
    }

    public static class Builder {

        private final List<String> statements;
        private boolean beginControlFlow;

        public Builder(BlockStmt blockStmt) {
            this.statements = new ArrayList<>();

            if (blockStmt != null) {
                String code = blockStmt.toString();
                if (!isEmpty(code)) {
                    statements.add(code.substring(1, code.length() - 1));
                }
            }
        }

        public Builder beginControlFlow(String code) {
            statements.add(code + "{");

            beginControlFlow = true;

            return this;
        }

        public Builder beginControlFlow(String format, Object... args) {
            return beginControlFlow(StatementUtils.statement(format, args));
        }

        public Builder endControlFlow() {
            beginControlFlow = false;

            if (!statements.isEmpty()) {
                int index = statements.size() - 1;
                statements.set(index, statements.get(index) + "}");
            }

            return this;
        }

        public Builder addStatement(String code) {
            if (code.endsWith(";")) {
                statements.add(code);
            } else {
                statements.add(code + ";");
            }
            return this;
        }

        public Builder addStatement(String format, Object... args) {
            return addStatement(StatementUtils.statement(format, args));
        }

        public JCBlock build() {
            JCBlock block = new JCBlock();

            if (beginControlFlow) {
                endControlFlow();
            }

            block.statements = statements;

            return block;
        }

        public BlockStmt generate() {
            return build().generate();
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
