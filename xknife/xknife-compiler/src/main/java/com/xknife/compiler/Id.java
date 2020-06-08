package com.xknife.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.sun.tools.javac.code.Symbol;

public class Id {
    private static final ClassName ANDROID_R = ClassName.get("android", "R");

    final int value;
    final CodeBlock codeBlock;

    public Id(int value) {
        this(value, null);
    }

    public Id(int value, Symbol rSymbol) {
        this.value = value;
        if (rSymbol != null) {
            ClassName className = ClassName.get(rSymbol.packge().getQualifiedName().toString(), "R", rSymbol.enclClass().name.toString());
            String resourceName = rSymbol.name.toString();
            this.codeBlock = className.topLevelClassName().equals(ANDROID_R) ? CodeBlock.of("$L.$N", className, resourceName) : CodeBlock.of("$T.$N", className, resourceName);
        }else {
            this.codeBlock = CodeBlock.of("$L", value);
        }
    }
}
