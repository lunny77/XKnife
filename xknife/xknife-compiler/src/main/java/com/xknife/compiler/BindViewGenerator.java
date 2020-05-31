package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xknife.annotation.BindView;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;

public class BindViewGenerator extends CodeGenerator {

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        Set<? extends Element> elements = annotations.get(BindView.class.getSimpleName());
        if (elements != null) {
            elements.forEach(element -> {
                methodBuilder.addStatement("target.$N = $T.findRequiredViewAsType($L, $L, $S)",
                        element.getSimpleName(), ClassName.get("com.xknife", "Utils"),
                        "source", element.getAnnotation(BindView.class).value(), "field '" + element.getSimpleName() + "'");
            });
            methodBuilder.addCode("\n");
        }

        if (nextGenerator != null) {
            nextGenerator.handle(classBuilder, methodBuilder, annotations);
        }
    }
}
