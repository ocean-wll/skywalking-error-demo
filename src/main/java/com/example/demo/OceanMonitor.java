package com.example.demo;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @Description
 * @Author ocean_wll
 * @Date 2021/8/5 11:53 上午
 */
public class OceanMonitor {

    @RuntimeType
    public static Object intercept(@Origin Method method, @AllArguments Object[] args,
                                   @SuperCall Callable<?> callable) {
        long start = System.currentTimeMillis();
        Object returnObj = null;
        try {
            try {
                returnObj = callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return returnObj;
        } finally {
            System.out.println("方法名称：" + method.getName());
            System.out.println("方法入参：" + Arrays.toString(args));
            System.out.println("返回结果：" + returnObj);
            System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
        }

    }
}
