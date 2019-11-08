package com.github.cstettler.cebolla.plugin;

import com.google.testing.compile.Compilation;

import javax.tools.JavaFileObject;

import static com.google.testing.compile.Compiler.javac;

class CompileUtils {

    private CompileUtils() {
    }

    static Compilation compile(JavaFileObject sourceFile) {
        return javac()
                .withOptions("-Xplugin:CebollaStereotype")
                .compile(sourceFile);
    }

}
