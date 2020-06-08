package com.xknife.compiler;

import com.sun.source.util.Trees;
import com.xknife.annotation.BindColor;
import com.xknife.annotation.BindString;
import com.xknife.annotation.BindView;
import com.xknife.annotation.OnClick;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class XKnifeProcessor extends AbstractProcessor {
    private Messager mMessager;
    private Filer mFiler;

    //该注解处理器需要处理的Annotation集合
    private final Set<String> mSupportedAnnotationTypes = new HashSet<>(Arrays.asList(
            BindView.class.getName(),
            BindString.class.getName(),
            BindColor.class.getName(),
            OnClick.class.getName()
    ));

    private Trees mTrees;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        printMessage("init()");

        mTrees = Trees.instance(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        printMessage("process()");

        TypeElement classElement = null;
        Map<String, Set<? extends Element>> annotationMap = new HashMap<>();
        for (TypeElement annotation : annotations) {
            printMessage("annotation: " + annotation.getSimpleName());
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            annotationMap.put(annotation.getSimpleName().toString(), elements);

            //获取注解所在类的Element
            if (classElement == null) {
                for (Element element : elements) {
                    classElement = (TypeElement) element.getEnclosingElement();
                    break;
                }
            }
        }

        if (classElement == null) {
            return false;
        }

        ClassGenerator classGenerator = new ClassGenerator(mTrees, mFiler, classElement);
        ConstructorGenerator constructorGenerator = new ConstructorGenerator(mTrees, classElement);
        BindViewGenerator bindViewGenerator = new BindViewGenerator(mTrees);
        bindViewGenerator.rScanner.messager = mMessager;

        BindStringGenerator bindStringGenerator = new BindStringGenerator(mTrees);
        BindColorGenerator bindColorGenerator = new BindColorGenerator(mTrees);
        OnClickGenerator onClickGenerator = new OnClickGenerator(mTrees);

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
        printMessage("getSupportedAnnotationTypes()");
        return mSupportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        printMessage("getSupportedSourceVersion()");
        return SourceVersion.latestSupported();
    }

    private void printMessage(String message) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "[XKnifeProcessor] " + message);
    }
}
