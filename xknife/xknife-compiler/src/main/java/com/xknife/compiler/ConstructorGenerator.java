package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class ConstructorGenerator extends CodeGenerator {
    private Element classElement;

    public ConstructorGenerator(Element classElement) {
        this.classElement = classElement;
    }

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(classElement.asType()), "target")
                .addStatement("this(target, target.getWindow().getDecorView())")
                .addModifiers(Modifier.PUBLIC)
                .build());

        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addParameter(TypeName.get(classElement.asType()), "target", Modifier.FINAL)
                .addParameter(ClassName.get("android.view", "View"), "source")
                .addStatement("this.target = target")
                .addCode("\n")
                .addModifiers(Modifier.PUBLIC);

        if (nextGenerator != null) {
            nextGenerator.handle(classBuilder, builder, annotations);
        }

        classBuilder.addMethod(builder.build());
    }
}
