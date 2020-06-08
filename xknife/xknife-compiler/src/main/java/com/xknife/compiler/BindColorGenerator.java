package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;
import com.xknife.annotation.BindColor;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;

public class BindColorGenerator extends CodeGenerator {

    public BindColorGenerator(Trees trees) {
        super(trees);
    }

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        Set<? extends Element> elements = annotations.get(BindColor.class.getSimpleName());
        if (elements != null) {
            elements.forEach(element -> methodBuilder.addStatement("target.$N = $T.valueOf(res.getColor($L))",
                    element.getSimpleName(), ClassName.get("android.graphics", "Color"),
                    element.getAnnotation(BindColor.class).value()));
            methodBuilder.addCode("\n");
        }

        if (nextGenerator != null) {
            nextGenerator.handle(classBuilder, methodBuilder, annotations);
        }
    }
}
