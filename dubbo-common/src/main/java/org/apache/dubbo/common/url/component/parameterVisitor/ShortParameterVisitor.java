package org.apache.dubbo.common.url.component.parameterVisitor;

public class ShortParameterVisitor extends ParameterVisitor{
    @Override
    public Number getValue(Number n) {
        return n.shortValue();
    }

    @Override
    public Number parseString(String value) {
        return Short.parseShort(value);
    }
}
