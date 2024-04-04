package org.apache.dubbo.common.url.component;

public class ServiceConfigURLToString extends ServiceConfigURL {


    @Override
    public String toString() {
        if (string != null) {
            return string;
        }
        return string = super.toString();
    }

    @Override
    public String toFullString() {
        if (full != null) {
            return full;
        }
        return full = super.toFullString();
    }

    @Override
    public String toIdentityString() {
        if (identity != null) {
            return identity;
        }
        return identity = super.toIdentityString();
    }

    @Override
    public String toParameterString() {
        if (parameter != null) {
            return parameter;
        }
        return parameter = super.toParameterString();
    }

}
