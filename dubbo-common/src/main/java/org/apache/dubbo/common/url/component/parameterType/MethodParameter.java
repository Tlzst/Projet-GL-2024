package org.apache.dubbo.common.url.component.parameterType;

import org.apache.dubbo.common.url.component.ServiceConfigURLParameter;
import org.apache.dubbo.common.url.component.parameterStrategy.MethodParameterStrategy;
import org.apache.dubbo.common.url.component.parameterVisitor.ByteParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.DoubleParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.FloatParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.IntParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.LongParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.ShortParameterVisitor;

public class MethodParameter extends ServiceConfigURLParameter {

    public MethodParameter(){
        super();
        this.strategy = new MethodParameterStrategy();
    }

    @Override
    public byte getMethodParameter(String method, String key, byte defaultValue) {
        return (byte) super.getParameterTemplate(new ByteParameterVisitor(),
                method, null, key, defaultValue);
    }

    @Override
    public double getMethodParameter(String method, String key, double defaultValue) {
        return (double) super.getParameterTemplate(new DoubleParameterVisitor(),
                method, null, key, defaultValue);
    }

    @Override
    public float getMethodParameter(String method, String key, float defaultValue) {
        return (float) super.getParameterTemplate(new FloatParameterVisitor(),
                method, null, key, defaultValue);
    }

    @Override
    public int getMethodParameter(String method, String key, int defaultValue) {
        return (int) super.getParameterTemplate(new IntParameterVisitor(),
                method, null, key, defaultValue);
    }

    @Override
    public long getMethodParameter(String method, String key, long defaultValue) {
        return (long) super.getParameterTemplate(new LongParameterVisitor(),
                method, null, key, defaultValue);
    }

    @Override
    public short getMethodParameter(String method, String key, short defaultValue) {
        return (short) super.getParameterTemplate(new ShortParameterVisitor(),
                method, null, key, defaultValue);
    }
}
