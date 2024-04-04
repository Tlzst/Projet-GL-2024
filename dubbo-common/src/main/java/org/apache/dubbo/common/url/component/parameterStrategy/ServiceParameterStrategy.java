package org.apache.dubbo.common.url.component.parameterStrategy;

public class ServiceParameterStrategy extends AbstractStrategy {
    @Override
    public String getValue(String method, String service, String key) {
        return super.getServiceParameter(service,key);
    }

    @Override
    public Number getNumber(String key, Number defaultValue, String method, String service) {
        return super.getServiceNumbers(service).get(key);
    }

    @Override
    public void updateNumber(String method, String key, Number n) {
        super.getNumbers().put(key,n);
    }
}
