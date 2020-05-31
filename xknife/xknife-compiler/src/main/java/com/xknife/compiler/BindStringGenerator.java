package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xknife.annotation.BindString;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;

public class BindStringGenerator extends CodeGenerator {

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        Set<? extends Element> elements = annotations.get(BindString.class.getSimpleName());
        if (elements != null) {
            methodBuilder.addStatement("$T context = source.getContext()", ClassName.get("android.content", "Context"))
                    .addStatement("$T res = context.getResources()", ClassName.get("android.content.res", "Resources"));
            elements.forEach(element -> {
                methodBuilder.addStatement("target.$N = res.getString($L)", element.getSimpleName(), element.getAnnotation(BindString.class).value());
            });
            methodBuilder.addCode("\n");
        }

        if (nextGenerator != null) {
            nextGenerator.handle(classBuilder, methodBuilder, annotations);
        }
    }
}
