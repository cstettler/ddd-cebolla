package com.github.cstettler.cebolla.plugin;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.github.cstettler.cebolla.plugin.CompileUtils.compile;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;

class GeneralCompilationTests {

    @Test
    void compile_classWithMultipleStereotypes_fails() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestClass",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.Aggregate;",
                "import com.github.cstettler.cebolla.stereotype.ValueObject;",
                "",
                "@Aggregate",
                "@ValueObject",
                "public class TestClass {",
                "",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("class 'TestClass' is annotated by multiple stereotypes: [" +
                "'com.github.cstettler.cebolla.stereotype.Aggregate', " +
                "'com.github.cstettler.cebolla.stereotype.ValueObject'" +
                "]");
    }

}
