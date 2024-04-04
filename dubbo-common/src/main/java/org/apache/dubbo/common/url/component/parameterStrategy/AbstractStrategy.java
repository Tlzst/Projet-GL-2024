package org.apache.dubbo.common.url.component.parameterStrategy;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.url.component.AbstractServiceConfigURL;

public abstract class AbstractStrategy extends AbstractServiceConfigURL {
    public abstract String getValue(String method, String service, String key);

    public abstract Number getNumber(String key, Number defaultValue, String method, String service);

    public abstract void updateNumber(String method, String key, Number n);
}
