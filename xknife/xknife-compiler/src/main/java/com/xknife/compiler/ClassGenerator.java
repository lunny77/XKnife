package com.xknife.compiler;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class ClassGenerator extends CodeGenerator {
    private TypeElement classElement;
    private Filer filer;

    public ClassGenerator(Trees trees, Filer filer, TypeElement classElement) {
        super(trees);
        this.filer = filer;
        this.classElement = classElement;
    }

    public void generate(Map<String, Set<? extends Element>> annotations) {
        handle(null, null, annotations);
    }

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        String fullName = classElement.getQualifiedName().toString();
        String packageName = fullName.substring(0, fullName.lastIndexOf("."));

        //构造以_ViewBinding结尾的类
        TypeSpec.Builder builder = TypeSpec.classBuilder(classElement.getSimpleName() + "_ViewBinding")
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        builder.addField(FieldSpec.builder(TypeName.get(classElement.asType()), "target", Modifier.PRIVATE).build());

        if (nextGenerator != null) {
            nextGenerator.handle(builder, null, annotations);
        }

        try {
            //生成Java文件
            JavaFile.builder(packageName, builder.build())
                    .addFileComment("Generated code from XKnife. Do not modify!").build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
