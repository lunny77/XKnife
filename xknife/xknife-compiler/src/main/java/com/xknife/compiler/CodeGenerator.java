package com.xknife.compiler;

import java.util.Collections;
import java.util.Set;

import javax.lang.model.element.Element;

public abstract class CodeGenerator implements Generator {
    Generator nextGenerator;

    public Set<? extends Element> getSupportedTypes() {
        return Collections.EMPTY_SET;
    }
}
