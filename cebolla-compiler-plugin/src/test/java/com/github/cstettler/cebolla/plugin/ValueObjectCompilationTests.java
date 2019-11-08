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
                "   private final int valueOne;",
                "   private final boolean valueTwo;",
                "   private final double valueThree;",
                "",
                "  public TestValueObject(int valueOne, boolean valueTwo, double valueThree) {",
                "    this.valueOne = valueOne;",
                "    this.valueTwo = valueTwo;",
                "    this.valueThree = valueThree;",
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
                "   private final String[] valueOne;",
                "   private final String[] valueTwo;",
                "",
                "  public TestValueObject(String[] valueOne, String[] valueTwo) {",
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

}
