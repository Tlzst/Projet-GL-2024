package org.apache.dubbo.common.url.component;

import org.apache.dubbo.common.utils.CollectionUtils;

import java.util.Map;

public class ServiceConfigURL extends AbstractServiceConfigURL {

    public ServiceConfigURL() {
        super();
    }

    public ServiceConfigURL(URLAddress urlAddress, URLParam urlParam, Map<String, Object> attributes) {
        super(urlAddress, urlParam, attributes);
    }

    public ServiceConfigURL(String protocol, String host, int port) {
        super(protocol,host,port);
    }

    public ServiceConfigURL(
            String protocol,
            String host,
            int port,
            String[] pairs) { // varargs ... conflict with the following path argument, use array instead.
        super(protocol,host,port,pairs);
    }

    public ServiceConfigURL(String protocol, String host, int port, Map<String, String> parameters) {
        super(protocol,host,port,parameters);
    }

    public ServiceConfigURL(String protocol, String host, int port, String path) {
        super(protocol, host, port, path);
    }

    public ServiceConfigURL(String protocol, String host, int port, String path, String... pairs) {
        super(protocol, host, port, path, pairs);
    }

    public ServiceConfigURL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        super(protocol, host, port, path, parameters);
    }

    public ServiceConfigURL(String protocol, String username, String password, String host, int port, String path) {
        super(protocol, username, password, host, port, path);
    }

    public ServiceConfigURL(
            String protocol, String username, String password, String host, int port, String path, String... pairs) {
        super(protocol, username, password, host, port, path, pairs);
    }

    public ServiceConfigURL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters) {
        super(protocol, username, password, host, port, path, parameters);
    }

    public ServiceConfigURL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters,
            Map<String, Object> attributes) {
        super(protocol, username, password, host, port, path,
                parameters, attributes);
    }
}
