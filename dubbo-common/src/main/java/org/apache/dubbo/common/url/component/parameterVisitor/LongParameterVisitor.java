package org.apache.dubbo.common.url.component.parameterVisitor;

public class LongParameterVisitor extends ParameterVisitor {
    @Override
    public Number getValue(Number n) {
        return n.longValue();
    }

    @Override
    public Number parseString(String value) {
        return Long.parseLong(value);
    }
}
