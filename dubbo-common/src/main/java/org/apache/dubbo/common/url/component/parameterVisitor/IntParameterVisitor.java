package org.apache.dubbo.common.url.component.parameterVisitor;

public class IntParameterVisitor extends ParameterVisitor{
    @Override
    public Number getValue(Number n) {
        return n.intValue();
    }

    @Override
    public Number parseString(String value) {
        return Integer.parseInt(value);
    }
}
