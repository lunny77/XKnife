package com.xknife.compiler;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;
import com.xknife.annotation.ListenerClass;
import com.xknife.annotation.OnClick;

import java.util.Map;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

public class OnClickGenerator extends CodeGenerator {

    public OnClickGenerator(Trees trees) {
        super(trees);
    }

    @Override
    public void handle(TypeSpec.Builder classBuilder, MethodSpec.Builder methodBuilder, Map<String, Set<? extends Element>> annotations) {
        Set<? extends Element> elements = annotations.get(OnClick.class.getSimpleName());
        if (elements != null) {
            elements.forEach(element -> {
                OnClick annotation = element.getAnnotation(OnClick.class);
                //通过反射获取该注解，因此其生命周期RetentionPolicy为RUNTIME
                ListenerClass listenerClassAnnotation = annotation.annotationType().getDeclaredAnnotation(ListenerClass.class);
                int[] ids = annotation.value();
                for (int id : ids) {
                    String viewName = "view_ox" + Integer.toHexString(id);
                    classBuilder.addField(FieldSpec.builder(Constants.ANDROID_VIEW, viewName, Modifier.PRIVATE).build());
                    methodBuilder.addStatement("this.$N = $T.findRequiredView(source, $L, $S)", viewName, Constants.XKNIFE_UTILS, id, "method '" + element.getSimpleName() + "'");

                    TypeSpec debouncingAnnoymousType = TypeSpec.anonymousClassBuilder("")
                            .addSuperinterface(Constants.XKNIFE_DEBOUNCING_LISTENER)
                            .addMethod(MethodSpec.methodBuilder("doClick")
                                    .addAnnotation(Override.class)
                                    .addModifiers(Modifier.PUBLIC)
                                    .addParameter(Constants.ANDROID_VIEW, "view")
                                    .returns(TypeName.VOID)
                                    .addStatement("target.$N($N)", element.getSimpleName(), "view")
                                    .build())
                            .build();

                    methodBuilder.addStatement("$N.$N($L)", viewName, listenerClassAnnotation.setter(), debouncingAnnoymousType).addCode("\n");
                }
            });
        }
    }
}
