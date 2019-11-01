package com.github.cstettler.cebolla.plugin;

import com.github.cstettler.cebolla.stereotype.ValueObject;
import com.sun.source.tree.ClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;

import java.lang.annotation.Annotation;

import static com.sun.source.util.TaskEvent.Kind.PARSE;

public class CebollaStereotypesPlugin implements Plugin {

    @Override
    public String getName() {
        return "CebollaStereotype";
    }

    @Override
    public void init(JavacTask task, String... args) {
        Context context = ((BasicJavacTask) task).getContext();

        task.addTaskListener(new TaskListener() {
            @Override
            public void started(TaskEvent e) {
                // do nothing
            }

            @Override
            public void finished(TaskEvent e) {
                if (e.getKind() != PARSE) {
                    return;
                }

                e.getCompilationUnit().accept(new TreeScanner<Void, Void>() {

                    @Override
                    public Void visitClass(ClassTree classTree, Void data) {
                        if (isAnnotatedBy(classTree, ValueObject.class)) {
                            JCClassDecl classDeclaration = (JCClassDecl) classTree;

                            new ValueObjectStereotypeHandler().handleClass(context, classDeclaration);
                        }

                        return super.visitClass(classTree, data);
                    }

                    private boolean isAnnotatedBy(ClassTree classTree, Class<? extends Annotation> annotationType) {
                        // TODO improve stereotype annotation type matching (in case of import, only simple name is returned)
                        return classTree.getModifiers().getAnnotations().stream()
                                .anyMatch((annotation) -> annotation.getAnnotationType().toString().equals(annotationType.getSimpleName()));
                    }
                }, null);
            }
        });
    }

}
