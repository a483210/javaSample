package com.psd.aop.sample.asm

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 插件
 *
 * @author Created by gold on 2021/3/15 09:52
 */
class AsmPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        //添加任务
        project.tasks
                .register("asmJar", JarTask)
    }
}