package org.apache.dubbo.common.url.component.parameterVisitor;

public class ByteParameterVisitor extends ParameterVisitor{
    @Override
    public Number getValue(Number n) {
        return n.byteValue();
    }

    @Override
    public Number parseString(String value) {
        return Byte.parseByte(value);
    }
}
