package org.apache.dubbo.common.url.component.parameterVisitor;

public class FloatParameterVisitor extends ParameterVisitor {
    @Override
    public Number getValue(Number n) {
        return n.floatValue();
    }

    @Override
    public Number parseString(String value) {
        return Float.parseFloat(value);
    }
}
