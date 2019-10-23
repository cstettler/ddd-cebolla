package com.github.cstettler.dscp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static javax.tools.JavaFileObject.Kind.CLASS;
import static javax.tools.JavaFileObject.Kind.SOURCE;
import static org.assertj.core.api.Assertions.assertThat;

class CebollaStereotypesPluginTests {

    @Test
    void compile_valueObjectWithSameState_areEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.dscp.test\n;" +
                "\n" +
                "@com.github.cstettler.dscp.stereotype.ValueObject\n" +
                "\n" +
                "public class TestValueObject {\n" +
                "   private final String valueOne;\n" +
                "   private final String valueTwo;\n" +
                "\n" +
                "  public TestValueObject(String valueOne, String valueTwo) {\n" +
                "    this.valueOne = valueOne;\n" +
                "    this.valueTwo = valueTwo;\n" +
                "  }\n" +
                "}\n";

        // act + assert
        compileAndRunTest("com.github.cstettler.dscp.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith("one", "two");
            Object instanceTwo = testValueObjectWith("one", "two");

            assertThat(instanceOne).isEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithDifferentState_areNotEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.dscp.test\n;" +
                "\n" +
                "@com.github.cstettler.dscp.stereotype.ValueObject\n" +
                "\n" +
                "public class TestValueObject {\n" +
                "   private final String valueOne;\n" +
                "   private final String valueTwo;\n" +
                "\n" +
                "  public TestValueObject(String valueOne, String valueTwo) {\n" +
                "    this.valueOne = valueOne;\n" +
                "    this.valueTwo = valueTwo;\n" +
                "  }\n" +
                "}\n";

        // act + assert
        compileAndRunTest("com.github.cstettler.dscp.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith("one", "two");
            Object instanceTwo = testValueObjectWith("one", "three");

            assertThat(instanceOne).isNotEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithDifferentNullState_areNotEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.dscp.test\n;" +
                "\n" +
                "@com.github.cstettler.dscp.stereotype.ValueObject\n" +
                "\n" +
                "public class TestValueObject {\n" +
                "   private final String valueOne;\n" +
                "   private final String valueTwo;\n" +
                "\n" +
                "  public TestValueObject(String valueOne, String valueTwo) {\n" +
                "    this.valueOne = valueOne;\n" +
                "    this.valueTwo = valueTwo;\n" +
                "  }\n" +
                "}\n";

        // act + assert
        compileAndRunTest("com.github.cstettler.dscp.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith("one", "two");
            Object instanceTwo = testValueObjectWith("one", null);

            assertThat(instanceOne).isNotEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithSamePrimitiveTypeState_areEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.dscp.test\n;" +
                "\n" +
                "@com.github.cstettler.dscp.stereotype.ValueObject\n" +
                "\n" +
                "public class TestValueObject {\n" +
                "   private final int valueOne;\n" +
                "   private final boolean valueTwo;\n" +
                "\n" +
                "  public TestValueObject(int valueOne, boolean valueTwo) {\n" +
                "    this.valueOne = valueOne;\n" +
                "    this.valueTwo = valueTwo;\n" +
                "  }\n" +
                "}\n";

        // act + assert
        compileAndRunTest("com.github.cstettler.dscp.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith(1, true);
            Object instanceTwo = testValueObjectWith(1, true);

            assertThat(instanceOne).isEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithSameArrayTypeState_areEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.dscp.test\n;" +
                "\n" +
                "@com.github.cstettler.dscp.stereotype.ValueObject\n" +
                "\n" +
                "public class TestValueObject {\n" +
                "   private final String[] valueOne;\n" +
                "   private final String[] valueTwo;\n" +
                "\n" +
                "  public TestValueObject(String[] valueOne, String[] valueTwo) {\n" +
                "    this.valueOne = valueOne;\n" +
                "    this.valueTwo = valueTwo;\n" +
                "  }\n" +
                "}\n";

        // act + assert
        compileAndRunTest("com.github.cstettler.dscp.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith(new String[]{"one", "two"}, new String[]{"three"});
            Object instanceTwo = testValueObjectWith(new String[]{"one", "two"}, new String[]{"three"});

            assertThat(instanceOne).isEqualTo(instanceTwo);
        });
    }

    private static Object testValueObjectWith(Object... values) throws Exception {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("com.github.cstettler.dscp.test.TestValueObject");
        Object instance = clazz.getConstructors()[0].newInstance(values);

        return instance;
    }

    private static void compileAndRunTest(String className, String sourceCode, Executable test) {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        List<String> options = asList(
                "-classpath", System.getProperty("java.class.path"),
                "-Xplugin:CebollaStereotypes"
        );

        ClassFileRepository classFileRepository = new ClassFileRepository();
        TestFileManager fileManager = new TestFileManager(classFileRepository);
        StringBasedSourceFile sourceFile = new StringBasedSourceFile(className, sourceCode);

        CompilationTask compilationTask = javaCompiler.getTask(writer, fileManager, null, options, null, singletonList(sourceFile));
        Boolean success = compilationTask.call();

        if (!success) {
            throw new IllegalStateException("compilation of source code failed: " + out.toString());
        }

        AtomicReference<Throwable> throwableReference = new AtomicReference<>();

        try {
            Thread thread = new Thread(() -> {
                try {
                    test.execute();
                } catch (Throwable t) {
                    throwableReference.set(t);
                }
            });
            thread.setContextClassLoader(classFileRepository.createClassLoader(CebollaStereotypesPlugin.class.getClassLoader()));
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException("unable to execute test code", e);
        }

        Throwable throwable = throwableReference.get();

        if (throwable != null) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else if (throwable instanceof Error) {
                throw (Error) throwable;
            }
            throw new IllegalStateException("test execution failed", throwable);
        }
    }

    private static class TestFileManager extends ForwardingJavaFileManager<JavaFileManager> {

        private final ClassFileRepository classFileRepository;

        TestFileManager(ClassFileRepository classFileRepository) {
            super(ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null));

            this.classFileRepository = classFileRepository;
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
            return new ByteArrayClassFile(className, (classFileData) -> this.classFileRepository.register(className, classFileData));
        }

    }

    private static class ClassFileRepository {

        private final Map<String, byte[]> classFileDataByClassName;

        private ClassFileRepository() {
            this.classFileDataByClassName = new HashMap<>();
        }

        void register(String className, byte[] classFileData) {
            this.classFileDataByClassName.put(className, classFileData);
        }

        ClassLoader createClassLoader(ClassLoader parent) {
            return new ClassLoader(parent) {
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    return super.loadClass(name);
                }

                @Override
                protected Class<?> findClass(String className) throws ClassNotFoundException {
                    try {
                        return super.findClass(className);
                    } catch (ClassNotFoundException e) {
                        byte[] classFileData = classFileDataByClassName.get(className);

                        if (classFileData != null) {
                            return defineClass(className, classFileData, 0, classFileData.length);
                        }

                        throw e;
                    }
                }
            };
        }

    }


    private static class StringBasedSourceFile extends SimpleJavaFileObject {

        private final String sourceCode;

        StringBasedSourceFile(String className, String sourceCode) {
            super(URI.create("code://" + className.replaceAll("\\.", "/") + ".java"), SOURCE);

            this.sourceCode = sourceCode;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return this.sourceCode;
        }

    }


    private static class ByteArrayClassFile extends SimpleJavaFileObject {

        private final Consumer<byte[]> classFileDataConsumer;

        ByteArrayClassFile(String className, Consumer<byte[]> classFileDataConsumer) {
            super(URI.create("code://" + className.replaceAll("\\.", "/") + ".class"), CLASS);

            this.classFileDataConsumer = classFileDataConsumer;
        }

        @Override
        public OutputStream openOutputStream() {
            return new ByteArrayOutputStream() {
                @Override
                public void close() throws IOException {
                    super.close();

                    onClose(toByteArray());
                }
            };
        }

        private void onClose(byte[] classFileData) {
            this.classFileDataConsumer.accept(classFileData);
        }

    }

}