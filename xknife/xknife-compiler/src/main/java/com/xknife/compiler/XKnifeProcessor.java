package com.xknife.compiler;

import com.xknife.annotation.BindColor;
import com.xknife.annotation.BindDimen;
import com.xknife.annotation.BindDrawable;
import com.xknife.annotation.BindString;
import com.xknife.annotation.BindView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class XKnifeProcessor extends AbstractProcessor {
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();

        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] init()");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] process()");

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] getSupportedAnnotationTypes()");

        Set<String> supportedAnnotationTypes = new HashSet<>();
        supportedAnnotationTypes.addAll(Arrays.asList(
                BindView.class.getName(),
                BindString.class.getName(),
                BindColor.class.getName(),
                BindDimen.class.getName(),
                BindDrawable.class.getName()
        ));
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] getSupportedSourceVersion()");
        return SourceVersion.latestSupported();
    }
}
