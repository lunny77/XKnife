package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;

public class ClassGenerator implements CodeGenerator {
    CodeGenerator nextGenerator;

    private Element classElement;
    private Filer filer;

    public ClassGenerator(Filer filer, Element element) {
        this.filer = filer;
        this.classElement = element;
    }

    public void generate(Map<String, Set<? extends Element>> annotations) {
        handle(null, null, annotations);
    }

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        String fullName = classElement.asType().toString();
        String packageName = fullName.substring(0, fullName.lastIndexOf("."));
        TypeSpec.Builder builder = TypeSpec.classBuilder(ClassName.get(packageName, classElement.getSimpleName() + "_ViewBinding"))
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        builder.addField(FieldSpec.builder(TypeName.get(classElement.asType()), "target", Modifier.PRIVATE).build());

        if (nextGenerator != null) {
            nextGenerator.handle(builder, null, annotations);
        }

        try {
            //生成Java文件
            JavaFile.builder(packageName, builder.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
