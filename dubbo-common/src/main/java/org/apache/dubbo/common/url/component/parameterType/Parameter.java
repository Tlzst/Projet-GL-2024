package org.apache.dubbo.common.url.component.parameterType;

import org.apache.dubbo.common.url.component.ServiceConfigURLParameter;
import org.apache.dubbo.common.url.component.parameterStrategy.ParameterStrategy;
import org.apache.dubbo.common.url.component.parameterVisitor.ByteParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.DoubleParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.FloatParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.IntParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.LongParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.ShortParameterVisitor;

public class Parameter extends ServiceConfigURLParameter {

    public Parameter() {
        super();
        this.strategy = new ParameterStrategy();
    }

    @Override
    public byte getParameter(String key, byte defaultValue) {
        return (byte) super.getParameterTemplate(new ByteParameterVisitor(),
                null, null, key, defaultValue);
    }

    @Override
    public double getParameter(String key, double defaultValue) {
        return (double) super.getParameterTemplate(new DoubleParameterVisitor(),
                null, null, key, defaultValue);
    }

    @Override
    public float getParameter(String key, float defaultValue) {
        return (float) super.getParameterTemplate(new FloatParameterVisitor(),
                null, null, key, defaultValue);
    }

    @Override
    public int getParameter(String key, int defaultValue) {
        return (int) super.getParameterTemplate(new IntParameterVisitor(),
                null, null, key, defaultValue);
    }

    @Override
    public long getParameter(String key, long defaultValue) {
        return (long) super.getParameterTemplate(new LongParameterVisitor(),
                null, null, key, defaultValue);
    }

    @Override
    public short getParameter(String key, short defaultValue) {
        return (short) super.getParameterTemplate(new ShortParameterVisitor(),
                null, null, key, defaultValue);
    }
}
