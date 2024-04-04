package org.apache.dubbo.common.url.component.parameterStrategy;

public class ParameterStrategy  extends AbstractStrategy {
    @Override
    public String getValue(String method, String service, String key) {
        return super.getParameter(key);
    }

    @Override
    public Number getNumber(String key, Number defaultValue, String method, String service) {
        return super.getNumbers().get(key);
    }

    @Override
    public void updateNumber(String method, String key, Number n) {
        super.getNumbers().put(key,n);
    }
}
