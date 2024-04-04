package org.apache.dubbo.common.url.component.parameterStrategy;

public class ServiceMethodParameterStrategy extends AbstractStrategy  {
    @Override
    public String getValue(String method, String service, String key) {
        return super.getServiceMethodParameter(service,method,key);
    }

    @Override
    public Number getNumber(String key, Number defaultValue, String method, String service) {
        return super.getCachedNumber(method,key);
    }

    @Override
    public void updateNumber(String method, String key, Number n) {
        super.updateCachedNumber(method,key,n);
    }
}
