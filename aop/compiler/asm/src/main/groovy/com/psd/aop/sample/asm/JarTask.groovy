package com.psd.aop.sample.asm

import com.psd.aop.sample.asm.utils.TextUtils
import jdk.internal.org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.ClassWriter
import jdk.internal.org.objectweb.asm.Opcodes
import jdk.internal.org.objectweb.asm.tree.AnnotationNode
import jdk.internal.org.objectweb.asm.tree.ClassNode
import jdk.internal.org.objectweb.asm.tree.MethodNode
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.bundling.Jar

/**
 * JarTask
 *
 * @author Created by gold on 2021/3/15 16:49
 */
class JarTask extends Jar {

    private static final String CLASS_ANNOTATION_NAME = 'Lcom/psd/aop/sample/annotation/AsmClassAnnotation;'
    private static final String METHOD_ANNOTATION_NAME = 'Lcom/psd/aop/sample/annotation/MethodAnnotation;'

    private static final LIBS_PATH = 'build/libs'
    private static final CLASS_PATH = 'build/tmp/asm/classes'

    JarTask() {
        group = 'build'

        doFirst {
            //执行命令
            handleClassesDirs(project)

            //处理依赖包
            handleRuntimeDirs(project)
        }

        //复制资源文件
        from project.sourceSets.main.output.resourcesDir
        //源代码
        from CLASS_PATH

        dependsOn 'classes'
    }

    private static void handleRuntimeDirs(Project project) {
        FileCollection collection = project.sourceSets.main.runtimeClasspath

        String path = '/libs'
        File newFileDir = project.file(LIBS_PATH + path)
        project.copy {
            from collection
            into newFileDir
        }

        StringBuilder builder = new StringBuilder('java -cp ')

        collection.forEach {
            String name = it.name
            if (!name.endsWith('.jar')) {
                return
            }

            builder.append(newFileDir.getPath())
                    .append("/")
                    .append(name)
                    .append(':')
        }

        builder.append(newFileDir.getParent()).append('/app.jar ')
        builder.append('com.psd.aop.sample.MainApplication')

        File file = project.file(LIBS_PATH + "/script.sh")

        file.write(builder.toString())
    }

    private static void handleClassesDirs(Project project) {
        project.file(CLASS_PATH).deleteDir()

        //获取class文件
        FileCollection collection = project.sourceSets.main.output.classesDirs

        collection.collect()
                .forEach { file ->
                    if (file.isDirectory()) {
                        file.listFiles()
                                .toList()
                                .forEach {
                                    handleFile(project, CLASS_PATH, it)
                                }
                    } else {
                        handleClass(project, CLASS_PATH, file)
                    }
                }
    }

    private static void handleFile(Project project, String path, File file) {
        String dirPath = path + File.separator + file.name
        if (file.isDirectory()) {
            File dirFile = project.file(dirPath)
            dirFile.mkdirs()

            file.listFiles()
                    .toList()
                    .forEach {
                        handleFile(project, dirPath, it)
                    }
        } else {
            //开始处理类文件
            handleClass(project, dirPath, file)
        }
    }

    private static void handleClass(Project project, String path, File file) {
        //将文件读取未classNode
        ClassReader reader = file.withInputStream { new ClassReader(it) } as ClassReader

        ClassNode classNode = new ClassNode()
        reader.accept(classNode, 0)

        //新代码文件地址
        File newFile = project.file(path)

        //查找是否存在注解
        Optional<AnnotationNode> classAnnotationNode = searchClassAnnotation(classNode, CLASS_ANNOTATION_NAME)
        if (!classAnnotationNode.isPresent()) {
            //不存在则原样复制
            file.withInputStream { input ->
                newFile.withOutputStream { output ->
                    output.write(input.bytes)
                }
            }
        } else {
            //否则修改方法
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS)

            handleMethod(classNode)

            classNode.accept(writer)

            newFile.withOutputStream { output ->
                output.write(writer.toByteArray())
            }
        }
    }

    private static void handleMethod(ClassNode classNode) {
        //扫描所有方法
        classNode.methods
                .forEach {
                    Optional<AnnotationNode> optional = searchMethodAnnotation(it, METHOD_ANNOTATION_NAME)
                    if (!optional.isPresent()) {
                        //不存在注解则直接退出
                        return
                    }

                    AnnotationNode methodAnnotationNode = optional.get()

                    String value = methodAnnotationNode.values[1]

                    //清除原方法体
                    it.instructions.clear()
                    it.localVariables.clear()

                    //添加表达式
                    it.visitLdcInsn(String.format('asm value=%s', value))
                    it.visitInsn(Opcodes.ARETURN)
                }
    }

    private static Optional<AnnotationNode> searchClassAnnotation(ClassNode node, String name) {
        List<AnnotationNode> annotationNodes = node.visibleAnnotations
        if (TextUtils.isEmpty(annotationNodes)) {
            return Optional.empty()
        }

        return annotationNodes
                .stream()
                .filter { filterAnnotation(it, name) }
                .findFirst()
    }

    private static Optional<AnnotationNode> searchMethodAnnotation(MethodNode node, String name) {
        List<AnnotationNode> annotationNodes = node.visibleAnnotations
        if (TextUtils.isEmpty(annotationNodes)) {
            return Optional.empty()
        }

        return annotationNodes
                .stream()
                .filter { filterAnnotation(it, name) }
                .findFirst()
    }

    private static boolean filterAnnotation(AnnotationNode node, String name) {
        String desc = node.desc
        if (TextUtils.isEmpty(desc)) {
            return false
        }

        return desc.endsWith(name)
    }
}