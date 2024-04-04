package org.apache.dubbo.common.url.component.parameterType;

import org.apache.dubbo.common.url.component.ServiceConfigURLParameter;
import org.apache.dubbo.common.url.component.parameterStrategy.ServiceMethodParameterStrategy;
import org.apache.dubbo.common.url.component.parameterVisitor.ByteParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.DoubleParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.FloatParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.IntParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.LongParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.ShortParameterVisitor;

public class ServiceMethodParameter extends ServiceConfigURLParameter {

    public ServiceMethodParameter(){
        super();
        this.strategy = new ServiceMethodParameterStrategy();
    }

    @Override
    public byte getServiceMethodParameter(String service, String method, String key, byte defaultValue) {
        return (byte) super.getParameterTemplate(new ByteParameterVisitor(),
                                                method, service, key, defaultValue);
    }

    @Override
    public double getServiceMethodParameter(String service, String method, String key, double defaultValue) {
        return (double) super.getParameterTemplate(new DoubleParameterVisitor(),
                method, service, key, defaultValue);
    }

    @Override
    public float getServiceMethodParameter(String service, String method, String key, float defaultValue) {
        return (float) super.getParameterTemplate(new FloatParameterVisitor(),
                method, service, key, defaultValue);
    }

    @Override
    public int getServiceMethodParameter(String service, String method, String key, int defaultValue) {
        return (int) super.getParameterTemplate(new IntParameterVisitor(),
                method, service, key, defaultValue);
    }

    @Override
    public long getServiceMethodParameter(String service, String method, String key, long defaultValue) {
        return (long) super.getParameterTemplate(new LongParameterVisitor(),
                method, service, key, defaultValue);
    }

    @Override
    public short getServiceMethodParameter(String service, String method, String key, short defaultValue) {
        return (short) super.getParameterTemplate(new ShortParameterVisitor(),
                method, service, key, defaultValue);
    }

}
