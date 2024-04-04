package org.apache.dubbo.common.url.component.parameterVisitor;

public class DoubleParameterVisitor extends ParameterVisitor{
    @Override
    public Number getValue(Number n) {
        return n.doubleValue();
    }

    @Override
    public Number parseString(String value) {
        return Double.parseDouble(value);
    }
}
