package org.apache.dubbo.common.url.component;

import org.apache.dubbo.common.URL;

import java.util.HashMap;
import java.util.Map;

public class ServiceConfigURLFactory extends AbstractServiceConfigURL{

    @Override
    protected <T extends URL> T newURL(URLAddress urlAddress, URLParam urlParam) {
        return (T) new ServiceConfigURL(urlAddress, urlParam, attributes);
    }

    @Override
    public URL addAttributes(Map<String, Object> attributeMap) {
        Map<String, Object> newAttributes = new HashMap<>();
        if (this.attributes != null) {
            newAttributes.putAll(this.attributes);
        }
        newAttributes.putAll(attributeMap);
        return new ServiceConfigURL(getUrlAddress(), getUrlParam(), newAttributes);
    }

    @Override
    public ServiceConfigURL putAttribute(String key, Object obj) {
        Map<String, Object> newAttributes = new HashMap<>();
        if (attributes != null) {
            newAttributes.putAll(attributes);
        }
        newAttributes.put(key, obj);
        return new ServiceConfigURL(getUrlAddress(), getUrlParam(), newAttributes);
    }

    @Override
    public URL removeAttribute(String key) {
        Map<String, Object> newAttributes = new HashMap<>();
        if (attributes != null) {
            newAttributes.putAll(attributes);
        }
        newAttributes.remove(key);
        return new ServiceConfigURL(getUrlAddress(), getUrlParam(), newAttributes);
    }
}
