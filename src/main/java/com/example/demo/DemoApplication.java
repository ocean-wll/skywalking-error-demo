package com.example.demo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.nameContains;
import static net.bytebuddy.matcher.ElementMatchers.nameStartsWith;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws InterruptedException, UnmodifiableClassException {
        SpringApplication.run(DemoApplication.class, args);
        test();
    }

    public static void test() throws InterruptedException, UnmodifiableClassException {
        Instrumentation instrumentation = ByteBuddyAgent.install();

        System.err.println("before =============");
        printAllTestControllerClasses(instrumentation);

//        final ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.of(false));
//        AgentBuilder agentBuilder = new AgentBuilder.Default(byteBuddy).ignore(
//                        nameStartsWith("net.bytebuddy.")
//                                .or(nameStartsWith("org.slf4j."))
//                                .or(nameStartsWith("org.groovy."))
//                                .or(nameContains("javassist"))
//                                .or(nameContains(".asm."))
//                                .or(nameContains(".reflectasm."))
//                                .or(nameStartsWith("sun.reflect"))
//                                .or(ElementMatchers.isSynthetic()))
//                .with(new CacheableTransformerDecorator())
//                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION);
//
//        agentBuilder.installOn(instrumentation);

//        AgentBuilder.Transformer transformer = (builder, typeDescription, classLoader, javaModule) -> builder
//                // 拦截任意方法
//                .method(ElementMatchers.any())
//                // 委托
//                .intercept(MethodDelegation.to(OceanMonitor.class));
//
//        new AgentBuilder
//                .Default()
//                // 指定需要拦截的类
//                .type(ElementMatchers.nameStartsWith("com.example.demo.TestController"))
//                .transform(transformer)
//                .installOn(instrumentation);
//
//
//        for (int i = 0; i < 3; i++) {
//            try {
//                instrumentation.retransformClasses(TestController.class);
//            } catch (Throwable e) {
//                e.printStackTrace();
//                System.out.println("end error");
//            }
//        }
        reTransform(instrumentation);
        reTransform(instrumentation);
        reTransform(instrumentation);

        System.err.println("after =============");
        printAllTestControllerClasses(instrumentation);
    }

    public static void reTransform(Instrumentation instrumentation) throws UnmodifiableClassException {
        ClassFileTransformer transformer = new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                return null;
            }
        };
        try {
            instrumentation.addTransformer(transformer, true);
            try {
                instrumentation.retransformClasses(TestController.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } finally {
            instrumentation.removeTransformer(transformer);
        }

    }

    public static void printAllTestControllerClasses(Instrumentation instrumentation) {
        Class<?>[] classes = instrumentation.getAllLoadedClasses();
        for (Class<?> clazz : classes) {
            if (clazz.getName().startsWith(TestController.class.getName())) {
                System.out.println(clazz.getName());
            }
        }
    }

}
