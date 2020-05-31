package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xknife.annotation.OnClick;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class OnClickGenerator implements CodeGenerator {

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        Set<? extends Element> elements = annotations.get(OnClick.class.getSimpleName());
        if (elements != null) {
            elements.forEach(element -> {
                OnClick annotation = element.getAnnotation(OnClick.class);
                int[] ids = annotation.value();
                for (int id : ids) {
                    String viewName = "view_" + Integer.toHexString(id);
                    classBuilder.addField(FieldSpec.builder(ClassName.get("android.view", "View"), viewName, Modifier.PRIVATE).build());
                    methodBuilder.addStatement("$N = $T.findRequiredView(source, $L, $S)",
                            viewName, ClassName.get("com.xknife", "Utils"), id, "method '" + element.getSimpleName() + "'");
                    methodBuilder.addStatement("$N.setOnClickListener(new View.OnClickListener(){\n" +
                            "   @Override\n" +
                            "   public void onClick(View v) {\n" +
                            "       target.$N(v);\n" +
                            "   }\n" +
                            "})", viewName, element.getSimpleName())
                            .addCode("\n");
                }
            });
        }
    }
}
