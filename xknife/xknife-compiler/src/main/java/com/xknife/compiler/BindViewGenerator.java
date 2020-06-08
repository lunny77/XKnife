package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;
import com.xknife.annotation.BindView;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;

public class BindViewGenerator extends CodeGenerator {

    public BindViewGenerator(Trees trees) {
        super(trees);
    }

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        Set<? extends Element> elements = annotations.get(BindView.class.getSimpleName());
        if (elements != null) {
            elements.forEach(element -> {
                int id = element.getAnnotation(BindView.class).value();
                Id idNamedR = elementToId(element, BindView.class, id);
                methodBuilder.addCode("this.target.$N = $T.findRequiredViewAsType($L, ", element.getSimpleName(), Constants.XKNIFE_UTILS, "source")
                        .addCode(idNamedR.codeBlock)
                        .addCode(", $S);\n", "field '" + element.getSimpleName() + "'");


//                methodBuilder.addStatement("this.target.$N = $T.findRequiredViewAsType($L, $T.$N, $S)",
//                        element.getSimpleName(), Constants.XKNIFE_UTILS, "source",
//                        ClassName.get("com.lunny.xknife","R","id"),element.getSimpleName(), "field '" + element.getSimpleName() + "'");
            });
            methodBuilder.addCode("\n");
        }

        if (nextGenerator != null) {
            nextGenerator.handle(classBuilder, methodBuilder, annotations);
        }
    }

//    private Id elementToId(Element element, Class<? extends Annotation> annotation, int value) {
//        JCTree tree = (JCTree) trees.getTree(element, getMirror(element, annotation));
//        if (tree != null) { // tree can be null if the references are compiled types and not source
//            rScanner.reset();
//            tree.accept(rScanner);
//            if (!rScanner.resourceIds.isEmpty()) {
//                return rScanner.resourceIds.values().iterator().next();
//            }
//        }
//        return new Id(value);
//    }
}
