package com.example.demo;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
