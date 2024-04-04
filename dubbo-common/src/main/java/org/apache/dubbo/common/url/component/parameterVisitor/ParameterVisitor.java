package org.apache.dubbo.common.url.component.parameterVisitor;

import javassist.compiler.ast.Visitor;

public abstract class ParameterVisitor {

    public abstract Number getValue(Number n);

    public abstract Number parseString(String value);

}
