package com.xy.im.base.core;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * ast处理器
 *
 * @author Created by gold on 2019-09-05 11:52
 */
public abstract class AstProcessor extends AptProcessor {

    protected JavacProcessingEnvironment javacProcessingEnv;
    protected Context context;
    protected Trees treesEnv;
    protected TreeMaker makeEnv;
    protected Names namesEnv;

    public AstProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);

        this.javacProcessingEnv = ((JavacProcessingEnvironment) processingEnv);
        this.context = javacProcessingEnv.getContext();

        this.treesEnv = Trees.instance(processingEnv);
        this.makeEnv = TreeMaker.instance(context);
        this.namesEnv = Names.instance(context);
    }

}
