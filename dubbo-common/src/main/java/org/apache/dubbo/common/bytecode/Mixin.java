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
package org.apache.dubbo.common.bytecode;

import org.apache.dubbo.common.utils.ClassUtils;
import org.apache.dubbo.common.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Mixin
 */
public abstract class Mixin {
    private static final String PACKAGE_NAME = Mixin.class.getPackage().getName();
    private static final AtomicLong MIXIN_CLASS_COUNTER = new AtomicLong(0);

    private static String pkg;

    private static ClassGenerator ccp;

    private static Class<?> neighbor;

    private static final Set<String> worked = new HashSet<>();

    protected Mixin() {}

    public static Mixin getInstance(Class<?>[] ics, Class<?>[] dcs){
        return mixin(ics,dcs);
    }

    /**
     * mixin interface and delegates.
     * all class must be public.
     *
     * @param ics interface class array.
     * @param dc  delegate class.
     * @return Mixin instance.
     */
    public static Mixin mixin(Class<?>[] ics, Class<?> dc) {
        return mixin(ics, new Class[] {dc});
    }

    /**
     * mixin interface and delegates.
     * all class must be public.
     *
     * @param ics interface class array.
     * @param dc  delegate class.
     * @param cl  class loader.
     * @return Mixin instance.
     */
    public static Mixin mixin(Class<?>[] ics, Class<?> dc, ClassLoader cl) {
        return mixin(ics, new Class[] {dc}, cl);
    }

    /**
     * mixin interface and delegates.
     * all class must be public.
     *
     * @param ics interface class array.
     * @param dcs delegate class array.
     * @return Mixin instance.
     */
    public static Mixin mixin(Class<?>[] ics, Class<?>[] dcs) {
        return mixin(ics, dcs, ClassUtils.getCallerClassLoader(Mixin.class));
    }

    /**
     * mixin interface and delegates.
     * all class must be public.
     *
     * @param ics interface class array.
     * @param dcs delegate class array.
     * @param cl  class loader.
     * @return Mixin instance.
     */
    public static Mixin mixin(Class<?>[] ics, Class<?>[] dcs, ClassLoader cl) {
        assertInterfaceArray(ics);
        long id = MIXIN_CLASS_COUNTER.getAndIncrement();
        pkg = null;
        ccp = null;
        neighbor = null;

        try {
            ccp = ClassGenerator.newInstance(cl);

            // impl constructor
            StringBuilder code = implementConstructor(dcs);
            ccp.addConstructor(Modifier.PUBLIC, new Class<?>[] {Object[].class}, code.toString());

            // impl methods.
            for (Class<?> ic : ics) {
                handleInterface(ic);
                handleInterfaceMethod(ic.getMethods(), dcs);
            }

            if (pkg == null) {
                setPackage(PACKAGE_NAME);
                setNeighbor(Mixin.class);
            }

            // create MixinInstance class.
            String micn = pkg + ".mixin" + id;
            ccp.setClassName(micn);
            ccp.toClass(neighbor);

            // create Mixin class.
            Class<?> mixin = createClassGenerator(id, cl, micn).toClass(Mixin.class);
            return (Mixin) mixin.getDeclaredConstructor().newInstance();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            // release ClassGenerator
            if (ccp != null) {
                ccp.release();
            }
        }
    }

    private static void setNeighbor(Class<?> neighbor1){
        neighbor = neighbor1;
    }

    private static void setPkg(String pkg1){
        pkg = pkg1;
    }

    private static int findMethod(Class<?>[] dcs, String desc) throws NoSuchMethodException {
        Class<?> cl;
        Method[] methods;
        for (int i = 0; i < dcs.length; i++) {
            cl = dcs[i];
            methods = cl.getMethods();
            for (Method method : methods) {
                if (desc.equals(ReflectUtils.getDesc(method))) {
                    return i;
                }
            }
        }
        throw new NoSuchMethodException("Missing method [" + desc + "] implement.");
    }

    private static void assertInterfaceArray(Class<?>[] ics) {
        for (Class<?> ic : ics) {
            if (!ic.isInterface()) {
                throw new RuntimeException("Class " + ic.getName() + " is not a interface.");
            }
        }
    }

    private static StringBuilder implementConstructor(Class<?>[] dcs) throws IllegalArgumentException{
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < dcs.length; i++) {
            if (!Modifier.isPublic(dcs[i].getModifiers())) {
                String npkg = dcs[i].getPackage().getName();
                setPackage(npkg);
            }

            ccp.addField("private " + dcs[i].getName() + " d" + i + ";");

            code.append(createConstructorBody(dcs[i], i));
        }

        return code ;
    }

    private static StringBuilder createConstructorBody(Class<?> cls, int cpt){
        StringBuilder body = new StringBuilder();
        body.append('d')
                .append(cpt)
                .append(" = (")
                .append(cls.getName())
                .append(")$1[")
                .append(cpt)
                .append("];\n");
        if (MixinAware.class.isAssignableFrom(cls)) {
            body.append('d').append(cpt).append(".setMixinInstance(this);\n");
        }
        return body;
    }

    private static void setPackage(String npkg) throws IllegalArgumentException {
        if (pkg == null) {
            setPkg(npkg);
        } else {
            if (!pkg.equals(npkg)) {
                throw new IllegalArgumentException("non-public interfaces class from different packages");
            }
        }
    }

    private static void addMethod(Class<?>[] dcs, String desc, Method method){
        int ix = findMethod(dcs, desc);

        Class<?> rt = method.getReturnType();
        String mn = method.getName();
        String body = "";
        if (Void.TYPE.equals(rt)) {
            body = "d" + ix + "." + mn + "($$);";
        } else {
            body = "return ($r)d" + ix + "." + mn + "($$);";
        }

        ccp.addMethod(
                mn,
                method.getModifiers(),
                rt,
                method.getParameterTypes(),
                method.getExceptionTypes(),
                body);
    }

    private static void handleInterface(Class<?> ic){
        if (!Modifier.isPublic(ic.getModifiers())) {
            String npkg = ic.getPackage().getName();
            setPackage(npkg);
            setNeighbor(ic);
        }

        ccp.addInterface(ic);
    }

    private static void handleInterfaceMethod(Method[] methods, Class<?>[] dcs){
        for (Method method : methods) {
            String desc = ReflectUtils.getDesc(method);
            if (!"java.lang.Object".equals(method.getDeclaringClass().getName())
                    && !worked.contains(desc)) {
                worked.add(desc);
                addMethod(dcs, desc, method);
            }
        }
    }

    private static ClassGenerator createClassGenerator(long id, ClassLoader cl, String micn){
        String fcn = Mixin.class.getName() + id;
        ClassGenerator ccm = ClassGenerator.newInstance(cl);
        ccm.setClassName(fcn);
        ccm.addDefaultConstructor();
        ccm.setSuperClass(Mixin.class.getName());
        ccm.addMethod("public Object newInstance(Object[] delegates){ return new " + micn + "($1); }");
        return ccm;
    }

    /**
     * new Mixin instance.
     *
     * @param ds delegates instance.
     * @return instance.
     */
    public abstract Object newInstance(Object[] ds);

    public interface MixinAware {
        void setMixinInstance(Object instance);
    }
}
