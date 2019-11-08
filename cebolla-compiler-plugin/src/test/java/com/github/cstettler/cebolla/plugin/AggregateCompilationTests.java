package com.github.cstettler.cebolla.plugin;

import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static com.github.cstettler.cebolla.plugin.CompileUtils.compile;
import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.JavaFileObjects.forSourceLines;

class AggregateCompilationTests {

    @Test
    void compile_aggregateWithStringTypeAggregateId_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestAggregate",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.Aggregate;",
                "import com.github.cstettler.cebolla.stereotype.AggregateId;",
                "",
                "@Aggregate",
                "public class TestAggregate {",
                "   private final String id;",
                "   private final String value;",
                "",
                "  public TestAggregate(String id, String value) {",
                "    this.id = id;",
                "    this.value = value;",
                "  }",
                "",
                "  @AggregateId",
                "  String id() {",
                "    return this.id;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_aggregateWithPrimitiveTypeAggregateId_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestAggregate",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.Aggregate;",
                "import com.github.cstettler.cebolla.stereotype.AggregateId;",
                "",
                "@Aggregate",
                "public class TestAggregate {",
                "   private final int id;",
                "   private final String value;",
                "",
                "  public TestAggregate(int id, String value) {",
                "    this.id = id;",
                "    this.value = value;",
                "  }",
                "",
                "  @AggregateId",
                "  int id() {",
                "    return this.id;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_aggregateWithArrayTypeAggregateId_succeeds() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestAggregate",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.Aggregate;",
                "import com.github.cstettler.cebolla.stereotype.AggregateId;",
                "",
                "@Aggregate",
                "public class TestAggregate {",
                "   private final Object[] id;",
                "   private final String value;",
                "",
                "  public TestAggregate(Object[] id, String value) {",
                "    this.id = id;",
                "    this.value = value;",
                "  }",
                "",
                "  @AggregateId",
                "  Object[] id() {",
                "    return this.id;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).succeededWithoutWarnings();
    }

    @Test
    void compile_aggregateWithoutAggregateIdAccessor_fails() {
        // arrange
        JavaFileObject sourceFile = forSourceLines("com.github.cstettler.cebolla.test.TestAggregate",
                "package com.github.cstettler.cebolla.test;",
                "",
                "import com.github.cstettler.cebolla.stereotype.Aggregate;",
                "import com.github.cstettler.cebolla.stereotype.AggregateId;",
                "",
                "@Aggregate",
                "public class TestAggregate {",
                "   private final String id;",
                "   private final String value;",
                "",
                "  public TestAggregate(String id, String value) {",
                "    this.id = id;",
                "    this.value = value;",
                "  }",
                "}"
        );

        // act
        Compilation compilation = compile(sourceFile);

        // assert
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("aggregate 'TestAggregate' has no aggregate id accessor method annotated with 'com.github.cstettler.cebolla.stereotype.AggregateId'");
    }

}
