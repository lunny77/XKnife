package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.xknife.annotation.BindColor;
import com.xknife.annotation.BindString;
import com.xknife.annotation.BindView;
import com.xknife.annotation.OnClick;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class XKnifeProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Filer mFiler;

    //该注解处理器需要处理的Annotation集合
    Set<String> mSupportedAnnotationTypes = new HashSet<>(Arrays.asList(
            BindView.class.getName(),
            BindString.class.getName(),
            BindColor.class.getName(),
            OnClick.class.getName()
    ));

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] init()");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] process()");

        Element classElement = null;
        Map<String, Set<? extends Element>> annotationMap = new HashMap<>();
        for (TypeElement annotation : annotations) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] annotation: " + annotation.getSimpleName());
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            annotationMap.put(annotation.getSimpleName().toString(), elements);

            //获取注解所在类的Element
            if (classElement == null) {
                for (Element element : elements) {
                    classElement = element.getEnclosingElement();
                    break;
                }
            }
        }

        if(classElement == null) {
            return false;
        }

        ClassGenerator classGenerator = new ClassGenerator(mFiler, classElement);
        ConstructorGenerator constructorGenerator = new ConstructorGenerator(classElement);
        BindViewGenerator bindViewGenerator = new BindViewGenerator();
        BindStringGenerator bindStringGenerator = new BindStringGenerator();
        BindColorGenerator bindColorGenerator = new BindColorGenerator();
        OnClickGenerator onClickGenerator = new OnClickGenerator();

        bindColorGenerator.nextGenerator = onClickGenerator;
        bindStringGenerator.nextGenerator = bindColorGenerator;
        bindViewGenerator.nextGenerator = bindStringGenerator;
        constructorGenerator.nextGenerator = bindViewGenerator;
        classGenerator.nextGenerator = constructorGenerator;
        classGenerator.generate(annotationMap);

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] getSupportedAnnotationTypes()");
        return mSupportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] getSupportedSourceVersion()");
        return SourceVersion.latestSupported();
    }
}
