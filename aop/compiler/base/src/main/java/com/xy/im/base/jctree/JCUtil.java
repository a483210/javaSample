package com.xy.im.base.jctree;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;

import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

/**
 * jc解析工具类
 *
 * @author Created by gold on 2019-09-09 16:09
 */
public final class JCUtil {
    private JCUtil() {
    }

    /**
     * 将新java代码替换旧节点
     *
     * @param context jc上下文
     * @param tree    节点
     * @param unit    新java树
     */
    public static void writeTo(Context context, JCTree.JCCompilationUnit tree, CompilationUnit unit) {
        JCTree.JCCompilationUnit jcUnit = parse(context, unit.toString());

        parsePos(jcUnit, tree.pos);

        tree.defs = jcUnit.defs;
    }

    /**
     * 将element解析为CompilationUnit
     *
     * @param element 类
     */
    @Deprecated
    public static CompilationUnit parseCompilationUnit(Element element) {
        try {
            if (!(element instanceof Symbol.ClassSymbol)) {
                return null;
            }
            Symbol.ClassSymbol classSymbol = ((Symbol.ClassSymbol) element);

            JavaFileObject fileObject = classSymbol.sourcefile;
            if (fileObject == null) {
                return null;
            }

            return StaticJavaParser.parse(fileObject.openInputStream());
        } catch (IOException ignore) {
            return null;
        }
    }

    /**
     * 将java代码解析为抽象语法树
     *
     * @param context javac上下文
     * @param str     java代码
     */
    private static JCTree.JCCompilationUnit parse(Context context, String str) {
        ParserFactory factory = ParserFactory.instance(context);

        Parser parser = factory.newParser(str, true, false, true);
        return parser.parseCompilationUnit();
    }

    /**
     * 将java文件解析为抽象语法树
     *
     * @param context javac上下文
     * @param path    java文件
     */
    private static JCTree.JCCompilationUnit parseFile(Context context, String path) {
        try {
            return parse(context, readString(path).toString());
        } catch (IOException ignore) {
            return null;
        }
    }

    private static CharSequence readString(String path) throws IOException {
        try (FileInputStream fin = new FileInputStream(path);
             FileChannel ch = fin.getChannel()) {
            ByteBuffer buffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size());
            return Charset.defaultCharset().decode(buffer);
        }
    }

    /**
     * 解析pos
     *
     * @param tree 树
     * @param pos  类节点位置
     */
    public static void parsePos(JCTree tree, int pos) {
        parsePos(tree, pos, false);
    }

    private static void parsePos(Object tree, int pos, boolean isParams) {
        if (tree == null) {
            return;
        }

        Class<?> clazz = tree.getClass();
        if (!isJavacClass(clazz.getName())) {
            return;
        }

        if (tree instanceof JCTree) {
            if (isParams) {
                ((JCTree) tree).pos = pos;
            } else {
                ((JCTree) tree).pos = -1;
            }
        }

        while (clazz != null && isJavacClass(clazz.getName())) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    continue;
                }

                Class<?> type = field.getType();

                if (JCTree.class.isAssignableFrom(type)) {
                    JCTree value = (JCTree) getValueFormField(tree, field);
                    if (value == null) {
                        continue;
                    }

                    parsePos(value, pos, isParams);
                } else if (Collection.class.isAssignableFrom(type)) {
                    Collection<?> values = (Collection<?>) getValueFormField(tree, field);
                    if (values == null || values.isEmpty()) {
                        continue;
                    }

                    for (Object value : values) {
                        boolean isParam = isParams;
                        if (!isParam && value instanceof JCTree.JCVariableDecl) {
                            isParam = true;
                        }
                        parsePos(value, pos, isParam);
                    }
                }
            }

            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 判断是否是javac类
     *
     * @param name 名称
     */
    private static boolean isJavacClass(String name) {
        return name.startsWith("com.sun.tools.javac");
    }

    //从变量获取值
    private static Object getValueFormField(Object object, Field field) {
        try {
            return field.get(object);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            return null;
        }
    }

}