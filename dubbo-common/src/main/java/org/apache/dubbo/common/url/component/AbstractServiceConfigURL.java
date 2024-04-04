/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.common.url.component;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.ConcurrentHashMapUtils;
import org.apache.dubbo.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractServiceConfigURL extends URL {

    protected transient volatile ConcurrentMap<String, URL> urls;
    protected transient volatile ConcurrentMap<String, Number> numbers;
    protected transient volatile ConcurrentMap<String, Map<String, Number>> methodNumbers;
    protected transient volatile String full;
    protected transient volatile String string;
    protected transient volatile String identity;
    protected transient volatile String parameter;

    public AbstractServiceConfigURL() {
        super();
    }

    public AbstractServiceConfigURL(URLAddress urlAddress, URLParam urlParam, Map<String, Object> attributes) {
        super(urlAddress, urlParam, attributes);
    }

    public AbstractServiceConfigURL(String protocol, String host, int port) {
        this(protocol, null, null, host, port, null, (Map<String, String>) null);
    }

    public AbstractServiceConfigURL(
            String protocol,
            String host,
            int port,
            String[] pairs) { // varargs ... conflict with the following path argument, use array instead.
        this(protocol, null, null, host, port, null, CollectionUtils.toStringMap(pairs));
    }

    public AbstractServiceConfigURL(String protocol, String host, int port, Map<String, String> parameters) {
        this(protocol, null, null, host, port, null, parameters);
    }

    public AbstractServiceConfigURL(String protocol, String host, int port, String path) {
        this(protocol, null, null, host, port, path, (Map<String, String>) null);
    }

    public AbstractServiceConfigURL(String protocol, String host, int port, String path, String... pairs) {
        this(protocol, null, null, host, port, path, CollectionUtils.toStringMap(pairs));
    }

    public AbstractServiceConfigURL(String protocol, String host, int port, String path, Map<String, String> parameters) {
        this(protocol, null, null, host, port, path, parameters);
    }

    public AbstractServiceConfigURL(String protocol, String username, String password, String host, int port, String path) {
        this(protocol, username, password, host, port, path, (Map<String, String>) null);
    }

    public AbstractServiceConfigURL(
            String protocol, String username, String password, String host, int port, String path, String... pairs) {
        this(protocol, username, password, host, port, path, CollectionUtils.toStringMap(pairs));
    }

    public AbstractServiceConfigURL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters) {
        this(new PathURLAddress(protocol, username, password, path, host, port), URLParam.parse(parameters), null);
    }

    public AbstractServiceConfigURL(
            String protocol,
            String username,
            String password,
            String host,
            int port,
            String path,
            Map<String, String> parameters,
            Map<String, Object> attributes) {
        this(
                new PathURLAddress(protocol, username, password, path, host, port),
                URLParam.parse(parameters),
                attributes);
    }

    protected Map<String, URL> getUrls() {
        // concurrent initialization is tolerant
        if (urls == null) {
            urls = new ConcurrentHashMap<>();
        }
        return urls;
    }

    protected Map<String, Number> getNumbers() {
        // concurrent initialization is tolerant
        if (numbers == null) {
            numbers = new ConcurrentHashMap<>();
        }
        return numbers;
    }

    protected Number getCachedNumber(String method, String key) {
        Map<String, Number> keyNumber = getMethodNumbers().get(method);
        if (keyNumber != null) {
            return keyNumber.get(key);
        }
        return null;
    }

    protected void updateCachedNumber(String method, String key, Number n) {
        Map<String, Number> keyNumber =
                ConcurrentHashMapUtils.computeIfAbsent(getMethodNumbers(), method, m -> new HashMap<>());
        keyNumber.put(key, n);
    }

    protected ConcurrentMap<String, Map<String, Number>> getMethodNumbers() {
        if (methodNumbers == null) { // concurrent initialization is tolerant
            methodNumbers = new ConcurrentHashMap<>();
        }
        return methodNumbers;
    }

    protected Map<String, Number> getServiceNumbers(String service) {
        return getNumbers();
    }

    protected Map<String, Map<String, Number>> getServiceMethodNumbers(String service) {
        return getMethodNumbers();
    }
}
