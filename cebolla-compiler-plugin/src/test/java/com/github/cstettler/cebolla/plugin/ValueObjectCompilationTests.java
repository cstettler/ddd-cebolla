package com.github.cstettler.cebolla.plugin;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.github.cstettler.cebolla.plugin.CompileUtils.compile;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;

class ValueObjectCompilationTests {

    @Test
    void compile_valueObjectWithStringState_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final String value;",
                "",
                "  public TestValueObject(String value) {",
                "    this.value = value;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_valueObjectWithPrimitiveState_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final boolean booleanValue;",
                "   private final byte byteValue;",
                "   private final short shortValue;",
                "   private final int intValue;",
                "   private final long longValue;",
                "   private final char charValue;",
                "   private final float floatValue;",
                "   private final double doubleValue;",
                "",
                "  public TestValueObject(boolean booleanValue, byte byteValue, short shortValue, int intValue, long longValue, char charValue, float floatValue, double doubleValue) {",
                "    this.booleanValue = booleanValue;",
                "    this.byteValue = byteValue;",
                "    this.shortValue = shortValue;",
                "    this.intValue = intValue;",
                "    this.longValue = longValue;",
                "    this.charValue = charValue;",
                "    this.floatValue = floatValue;",
                "    this.doubleValue = doubleValue;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_valueObjectWithArrayState_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final Object[] valueOne;",
                "   private final String[] valueTwo;",
                "",
                "  public TestValueObject(Object[] valueOne, String[] valueTwo) {",
                "    this.valueOne = valueOne;",
                "    this.valueTwo = valueTwo;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_valueObjectWithArrayAndObjectState_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final String[] valueOne;",
                "   private final String valueTwo;",
                "",
                "  public TestValueObject(String[] valueOne, String valueTwo) {",
                "    this.valueOne = valueOne;",
                "    this.valueTwo = valueTwo;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_valueObjectWithExistingNormalEqualsMethod_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final String value;",
                "",
                "  public TestValueObject(String value) {",
                "    this.value = value;",
                "  }",
                "",
                "  public boolean equals(Object other) {",
                "    return true;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_valueObjectWithExistingFullyQualifiedEqualsMethod_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final String value;",
                "",
                "  public TestValueObject(String value) {",
                "    this.value = value;",
                "  }",
                "",
                "  public boolean equals(java.lang.Object other) {",
                "    return true;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_valueObjectWithExistingHashCodeMethod_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final String value;",
                "",
                "  public TestValueObject(String value) {",
                "    this.value = value;",
                "  }",
                "",
                "  public int hashCode() {",
                "    return 0;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_valueObjectWithExistingDefaultConstructor_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestValueObject",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@ValueObject",
                "public class TestValueObject {",
                "   private final String value;",
                "",
                "  public TestValueObject() {",
                "    this.value = null;",
                "  }",
                "",
                "  public TestValueObject(String value) {",
                "    this.value = value;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

}
