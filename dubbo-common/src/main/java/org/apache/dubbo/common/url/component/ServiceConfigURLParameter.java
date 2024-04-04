package org.apache.dubbo.common.url.component;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.url.component.parameterStrategy.AbstractStrategy;
import org.apache.dubbo.common.url.component.parameterVisitor.ParameterVisitor;
import org.apache.dubbo.common.utils.StringUtils;

import java.lang.reflect.Parameter;

public class ServiceConfigURLParameter extends AbstractServiceConfigURL {

    protected AbstractStrategy strategy;

    @Override
    public URL getUrlParameter(String key) {
        URL u = getUrls().get(key);
        if (u != null) {
            return u;
        }
        String value = getParameterAndDecoded(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        u = URL.valueOf(value);
        getUrls().put(key, u);
        return u;
    }


    public Number getParameterTemplate(ParameterVisitor v, String method, String service, String key, Number defaultValue){
        Number n = strategy.getNumber(key, defaultValue, method, service);
        if (n != null) {
            return v.getValue(n);
        }

        String value = strategy.getValue(method, service, key);
        if (StringUtils.isEmpty(value)){
            return null;
        }

        n = v.parseString(value);
        strategy.updateNumber(method, key, n);
        return n;
    }


}
