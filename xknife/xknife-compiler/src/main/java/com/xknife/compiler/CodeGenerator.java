package com.xknife.compiler;

import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public abstract class CodeGenerator implements Generator {
    RScanner rScanner = new RScanner();
    Trees mTrees;
    Generator nextGenerator;

    public CodeGenerator(Trees trees) {
        this.mTrees = trees;
    }

    public Id elementToId(Element element, Class<? extends Annotation> annotation, int value) {
        JCTree tree = (JCTree) mTrees.getTree(element, getMirror(element, annotation));
        if (tree != null) { // tree can be null if the references are compiled types and not source
            rScanner.reset();
            tree.accept(rScanner);
            if (!rScanner.resourceIds.isEmpty()) {
                return rScanner.resourceIds.values().iterator().next();
            }
        }
        return new Id(value);
    }

    private AnnotationMirror getMirror(Element element, Class<? extends Annotation> clazz) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(clazz.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }

    Messager messager;
}
