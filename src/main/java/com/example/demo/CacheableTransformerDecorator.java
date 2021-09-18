package com.example.demo;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 缓存转换器
 * @Author ocean_wll
 * @Date 2021/8/4 11:07 上午
 */
public class CacheableTransformerDecorator implements AgentBuilder.TransformerDecorator {

    /**
     * key为 classloader+className，value为 字节码
     */
    private final Map<String, byte[]> classCacheMap;

    public CacheableTransformerDecorator() {
        this.classCacheMap = new ConcurrentHashMap<>();
    }

    @Override
    public ResettableClassFileTransformer decorate(ResettableClassFileTransformer classFileTransformer) {
        return new ResettableClassFileTransformer.WithDelegation(classFileTransformer) {

            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                if (className != null && className.replaceAll("/", ".").startsWith(TestController.class.getName())) {
                    System.out.println("CacheableTransformerDecorator :" + className);
                }
                // load from cache
                byte[] classCache = classCacheMap.get(getCacheKey(loader, className));
                if (classCache != null) {
                    return classCache;
                }

                //transform class
                classfileBuffer = classFileTransformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);

                // save to cache
                if (classfileBuffer != null) {
                    classCacheMap.put(getCacheKey(loader, className), classfileBuffer);
                }

                return classfileBuffer;
            }
        };
    }

    /**
     * 获取缓存key
     *
     * @param loader    ClassLoader
     * @param className 类名
     * @return 缓存key
     */
    private String getCacheKey(ClassLoader loader, String className) {
        return getClassLoaderHash(loader) + "@" + className;
    }

    /**
     * 获取classLoader的hash值
     *
     * @param loader ClassLoader
     * @return classLoad的hash值
     */
    private static String getClassLoaderHash(ClassLoader loader) {
        String classloader;
        if (loader != null) {
            classloader = Integer.toHexString(loader.hashCode());
        } else {
            //classloader is null for BootstrapClassLoader
            classloader = "00000000";
        }
        return classloader;
    }
}
