package org.apache.dubbo.common.url.component.parameterType;

import org.apache.dubbo.common.url.component.ServiceConfigURLParameter;
import org.apache.dubbo.common.url.component.parameterStrategy.ServiceParameterStrategy;
import org.apache.dubbo.common.url.component.parameterVisitor.ByteParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.DoubleParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.FloatParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.LongParameterVisitor;
import org.apache.dubbo.common.url.component.parameterVisitor.ShortParameterVisitor;

public class ServiceParameter extends ServiceConfigURLParameter {

    public ServiceParameter() {
        super();
        this.strategy = new ServiceParameterStrategy();
    }

    @Override
    public byte getServiceParameter(String service, String key, byte defaultValue) {
        return (byte) super.getParameterTemplate(new ByteParameterVisitor(),
                null, service, key, defaultValue);
    }

    @Override
    public double getServiceParameter(String service, String key, double defaultValue) {
        return (double) super.getParameterTemplate(new DoubleParameterVisitor(),
                null, service, key, defaultValue);
    }

    @Override
    public float getServiceParameter(String service, String key, float defaultValue) {
        return (float) super.getParameterTemplate(new FloatParameterVisitor(),
                null, service, key, defaultValue);
    }

    @Override
    public long getServiceParameter(String service, String key, long defaultValue) {
        return (long) super.getParameterTemplate(new LongParameterVisitor(),
                null, service, key, defaultValue);
    }

    @Override
    public short getServiceParameter(String service, String key, short defaultValue) {
        return (short) super.getParameterTemplate(new ShortParameterVisitor(),
                null, service, key, defaultValue);
    }
}
