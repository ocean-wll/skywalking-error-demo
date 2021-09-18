package com.example.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 缓存解析器
 * @Author ocean_wll
 * @Date 2021/8/5 11:48 上午
 */
public class CacheResolver {

    /**
     * key为 classloader+className，value为 字节码
     */
    public static final Map<String, byte[]> classCacheMap = new ConcurrentHashMap<>();

    /**
     * 获取缓存key
     *
     * @param loader    ClassLoader
     * @param className 类名
     * @return 缓存key
     */
    public static String getCacheKey(ClassLoader loader, String className) {
        return getClassLoaderHash(loader) + "@" + className;
    }

    /**
     * 获取classLoader的hash值
     *
     * @param loader ClassLoader
     * @return classLoad的hash值
     */
    public static String getClassLoaderHash(ClassLoader loader) {
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
