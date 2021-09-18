

## 本repo中文 Only



## 说明

代码里只触发了`retransformClasses`：

```java
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
```


## 无agent时运行结果

```
before =============
com.example.demo.TestController
after =============
com.example.demo.TestController
```

## 配置 apache-skywalking-apm-7.0.0.tar.gz  agent时

```
-javaagent:/apache-skywalking-apm-bin/agent/skywalking-agent.jar
```


```
========
com.example.demo.TestController$auxiliary$64xF7MuP
com.example.demo.TestController
com.example.demo.TestController$auxiliary$siehubRl
java.lang.ClassFormatError
	at sun.instrument.InstrumentationImpl.retransformClasses0(Native Method)
	at sun.instrument.InstrumentationImpl.retransformClasses(InstrumentationImpl.java:144)
	at com.example.demo.DemoApplication.reTransform(DemoApplication.java:44)
	at com.example.demo.DemoApplication.main(DemoApplication.java:24)
java.lang.ClassFormatError
	at sun.instrument.InstrumentationImpl.retransformClasses0(Native Method)
	at sun.instrument.InstrumentationImpl.retransformClasses(InstrumentationImpl.java:144)
	at com.example.demo.DemoApplication.reTransform(DemoApplication.java:44)
	at com.example.demo.DemoApplication.main(DemoApplication.java:25)
java.lang.ClassFormatError
	at sun.instrument.InstrumentationImpl.retransformClasses0(Native Method)
	at sun.instrument.InstrumentationImpl.retransformClasses(InstrumentationImpl.java:144)
	at com.example.demo.DemoApplication.reTransform(DemoApplication.java:44)
	at com.example.demo.DemoApplication.main(DemoApplication.java:26)
after =============
com.example.demo.TestController$auxiliary$ffk01nYV
com.example.demo.TestController$auxiliary$6JMnANVu
com.example.demo.TestController$auxiliary$Yxrhu7QP
com.example.demo.TestController$auxiliary$64xF7MuP
com.example.demo.TestController
com.example.demo.TestController$auxiliary$siehubRl
```
