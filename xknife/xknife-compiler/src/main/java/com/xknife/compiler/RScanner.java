package com.xknife.compiler;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeScanner;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import static java.util.Objects.requireNonNull;

public class RScanner extends TreeScanner {
    Messager messager;
    Map<Integer, Id> resourceIds = new LinkedHashMap<>();

    @Override
    public void visitIdent(JCTree.JCIdent jcIdent) {
        super.visitIdent(jcIdent);
        messager.printMessage(Diagnostic.Kind.NOTE, "----> visitIdent()");
        Symbol symbol = jcIdent.sym;
        if (symbol.type instanceof Type.JCPrimitiveType) {
            messager.printMessage(Diagnostic.Kind.NOTE, symbol.toString());
            Id id = parseId(symbol);
            if (id != null) {
                resourceIds.put(id.value, id);
            }
        }
    }

    @Override
    public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
        super.visitSelect(jcFieldAccess);
        messager.printMessage(Diagnostic.Kind.NOTE, "----> visitSelect()");
        Symbol symbol = jcFieldAccess.sym;
        messager.printMessage(Diagnostic.Kind.NOTE, symbol.toString());
        Id id = parseId(symbol);
        if (id != null) {
            resourceIds.put(id.value, id);
        }
    }

    @Override
    public void visitLiteral(JCTree.JCLiteral jcLiteral) {
        super.visitLiteral(jcLiteral);
        messager.printMessage(Diagnostic.Kind.NOTE, "----> visitLiteral()");
        messager.printMessage(Diagnostic.Kind.NOTE, jcLiteral.toString());
        try {
            int value = (Integer) jcLiteral.value;
            resourceIds.put(value, new Id(value));
        } catch (Exception ignored) {
        }
    }

    public Id parseId(Symbol symbol) {
        Id id = null;
        if (symbol.getEnclosingElement() != null
                && symbol.getEnclosingElement().getEnclosingElement() != null
                && symbol.getEnclosingElement().getEnclosingElement().enclClass() != null) {
            try {
                int value = (Integer) requireNonNull(((Symbol.VarSymbol) symbol).getConstantValue());

                messager.printMessage(Diagnostic.Kind.NOTE, "----> parseId:" + symbol.packge().getQualifiedName().toString() +
                        "  ==  " + symbol.enclClass().name.toString());

                id = new Id(value, symbol);
            } catch (Exception ignored) {
            }
        }
        return id;
    }

    public void reset() {
        resourceIds.clear();
    }
}
