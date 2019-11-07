package com.github.cstettler.cebolla.plugin;

import com.google.common.collect.ImmutableList;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.tools.JavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.testing.compile.Compilation.Status.FAILURE;
import static com.google.testing.compile.Compiler.javac;
import static org.assertj.core.api.Assertions.assertThat;

class CebollaStereotypesPluginTests {

    @Test
    void compile_valueObjectWithSameState_areEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.cebolla.test\n;" +
                "\n" +
                "import com.github.cstettler.cebolla.stereotype.ValueObject;\n" +
                "\n" +
                "@ValueObject\n" +
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
        compileAndRunTest("com.github.cstettler.cebolla.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith("one", "two");
            Object instanceTwo = testValueObjectWith("one", "two");

            assertThat(instanceOne).isEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithDifferentState_areNotEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.cebolla.test\n;" +
                "\n" +
                "import com.github.cstettler.cebolla.stereotype.ValueObject;\n" +
                "\n" +
                "@ValueObject\n" +
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
        compileAndRunTest("com.github.cstettler.cebolla.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith("one", "two");
            Object instanceTwo = testValueObjectWith("one", "three");

            assertThat(instanceOne).isNotEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithDifferentNullState_areNotEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.cebolla.test\n;" +
                "\n" +
                "import com.github.cstettler.cebolla.stereotype.ValueObject;\n" +
                "\n" +
                "@ValueObject\n" +
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
        compileAndRunTest("com.github.cstettler.cebolla.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith("one", "two");
            Object instanceTwo = testValueObjectWith("one", null);

            assertThat(instanceOne).isNotEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithSamePrimitiveTypeState_areEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.cebolla.test\n;" +
                "\n" +
                "import com.github.cstettler.cebolla.stereotype.*;\n" +
                "\n" +
                "@ValueObject\n" +
                "\n" +
                "public class TestValueObject {\n" +
                "   private final int valueOne;\n" +
                "   private final boolean valueTwo;\n" +
                "   private final double valueThree;\n" +
                "\n" +
                "  public TestValueObject(int valueOne, boolean valueTwo, double valueThree) {\n" +
                "    this.valueOne = valueOne;\n" +
                "    this.valueTwo = valueTwo;\n" +
                "    this.valueThree = valueThree;\n" +
                "  }\n" +
                "}\n";

        // act + assert
        compileAndRunTest("com.github.cstettler.cebolla.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith(1, true, 0.05);
            Object instanceTwo = testValueObjectWith(1, true, 0.05);

            assertThat(instanceOne).isEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithSameArrayTypeState_areEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.cebolla.test\n;" +
                "\n" +
                "import com.github.cstettler.cebolla.stereotype.ValueObject;\n" +
                "\n" +
                "@ValueObject\n" +
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
        compileAndRunTest("com.github.cstettler.cebolla.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith(new String[]{"one", "two"}, new String[]{"three"});
            Object instanceTwo = testValueObjectWith(new String[]{"one", "two"}, new String[]{"three"});

            assertThat(instanceOne).isEqualTo(instanceTwo);
        });
    }

    @Test
    void compile_valueObjectWithSameArrayAndObjectTypeState_areEqual() {
        // arrange
        String sourceCode = "" +
                "package com.github.cstettler.cebolla.test\n;" +
                "\n" +
                "import com.github.cstettler.cebolla.stereotype.ValueObject;\n" +
                "\n" +
                "@ValueObject\n" +
                "\n" +
                "public class TestValueObject {\n" +
                "   private final String[] valueOne;\n" +
                "   private final String valueTwo;\n" +
                "\n" +
                "  public TestValueObject(String[] valueOne, String valueTwo) {\n" +
                "    this.valueOne = valueOne;\n" +
                "    this.valueTwo = valueTwo;\n" +
                "  }\n" +
                "}\n";

        // act + assert
        compileAndRunTest("com.github.cstettler.cebolla.test.TestValueObject", sourceCode, () -> {
            Object instanceOne = testValueObjectWith(new String[]{"one", "two"}, "three");
            Object instanceTwo = testValueObjectWith(new String[]{"one", "two"}, "three");

            assertThat(instanceOne).isEqualTo(instanceTwo);
        });
    }

    private static Object testValueObjectWith(Object... values) throws Exception {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("com.github.cstettler.cebolla.test.TestValueObject");
        Object instance = clazz.getConstructors()[0].newInstance(values);

        return instance;
    }

    private static void compileAndRunTest(String className, String sourceCode, Executable test) {
        Compilation compilation = javac()
                .withOptions("-Xplugin:CebollaStereotype")
                .compile(JavaFileObjects.forSourceString(className, sourceCode));

        if (compilation.status() == FAILURE) {
            throw new IllegalStateException("compilation of source code failed: " + compilation.errors());
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
            ClassLoader parent = CebollaStereotypesPluginTests.class.getClassLoader();
            thread.setContextClassLoader(classLoaderFor(compilation.generatedFiles(), parent));
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

    private static ClassLoader classLoaderFor(ImmutableList<JavaFileObject> generatedFiles, ClassLoader parent) {
        return new ClassLoader(parent) {
            @Override
            protected Class<?> findClass(String className) throws ClassNotFoundException {
                try {
                    return super.findClass(className);
                } catch (ClassNotFoundException e) {
                    byte[] classFileData = generatedFiles.stream()
                            .filter((generatedFile) -> generatedFile.getName().endsWith(className.replaceAll("\\.", "/") + ".class"))
                            .map((generatedFile) -> readBytes(generatedFile))
                            .findFirst()
                            .orElseThrow(() -> e);

                    return defineClass(className, classFileData, 0, classFileData.length);
                }
            }
        };
    }

    private static byte[] readBytes(JavaFileObject javaFileObject) {
        try {
            InputStream in = javaFileObject.openInputStream();

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                result.write(buffer, 0, read);
            }

            return result.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("unable to read bytes from class file '" + javaFileObject + "'", e);
        }
    }

}