package com.github.cstettler.cebolla.plugin;

import com.github.cstettler.cebolla.stereotype.ValueObject;
import com.sun.source.tree.ClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;

import java.lang.annotation.Annotation;

import static com.github.cstettler.cebolla.plugin.AstUtils.add;
import static com.github.cstettler.cebolla.plugin.AstUtils.addMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.arguments;
import static com.github.cstettler.cebolla.plugin.AstUtils.block;
import static com.github.cstettler.cebolla.plugin.AstUtils.callTo;
import static com.github.cstettler.cebolla.plugin.AstUtils.cast;
import static com.github.cstettler.cebolla.plugin.AstUtils.falze;
import static com.github.cstettler.cebolla.plugin.AstUtils.fieldOrMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.fieldsOf;
import static com.github.cstettler.cebolla.plugin.AstUtils.identifier;
import static com.github.cstettler.cebolla.plugin.AstUtils.iif;
import static com.github.cstettler.cebolla.plugin.AstUtils.intType;
import static com.github.cstettler.cebolla.plugin.AstUtils.isArray;
import static com.github.cstettler.cebolla.plugin.AstUtils.isEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.isNotEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.isPrimitive;
import static com.github.cstettler.cebolla.plugin.AstUtils.isPrimitiveOfType;
import static com.github.cstettler.cebolla.plugin.AstUtils.literal;
import static com.github.cstettler.cebolla.plugin.AstUtils.method;
import static com.github.cstettler.cebolla.plugin.AstUtils.multiply;
import static com.github.cstettler.cebolla.plugin.AstUtils.not;
import static com.github.cstettler.cebolla.plugin.AstUtils.nuull;
import static com.github.cstettler.cebolla.plugin.AstUtils.objectType;
import static com.github.cstettler.cebolla.plugin.AstUtils.or;
import static com.github.cstettler.cebolla.plugin.AstUtils.parameter;
import static com.github.cstettler.cebolla.plugin.AstUtils.reAssignVariable;
import static com.github.cstettler.cebolla.plugin.AstUtils.retuurn;
import static com.github.cstettler.cebolla.plugin.AstUtils.thiz;
import static com.github.cstettler.cebolla.plugin.AstUtils.truu;
import static com.github.cstettler.cebolla.plugin.AstUtils.variable;
import static com.github.cstettler.cebolla.plugin.AstUtils.with;
import static com.sun.source.util.TaskEvent.Kind.PARSE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.TypeTag.BOOLEAN;
import static com.sun.tools.javac.code.TypeTag.DOUBLE;
import static com.sun.tools.javac.code.TypeTag.FLOAT;
import static com.sun.tools.javac.code.TypeTag.INT;
import static com.sun.tools.javac.util.List.from;
import static com.sun.tools.javac.util.List.nil;
import static com.sun.tools.javac.util.List.of;
import static java.util.stream.Collectors.toList;

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

                            with(context, () -> {
                                addEquals(classDeclaration);
                                addHashCode(classDeclaration);
                            });
                        }

                        return super.visitClass(classTree, data);
                    }

                    private boolean isAnnotatedBy(ClassTree classTree, Class<? extends Annotation> annotationType) {
                        // TODO improve stereotype annotation type matching (in case of import, only simple name is returned)
                        return classTree.getModifiers().getAnnotations().stream()
                                .anyMatch((annotation) -> annotation.getAnnotationType().toString().equals(annotationType.getSimpleName()));
                    }

                    private void addEquals(JCClassDecl classDeclaration) {
                        JCBlock equalsBody = block(of(
                                iif(
                                        isEqual(thiz(), identifier("other")),
                                        retuurn(truu())
                                ),
                                iif(or(
                                        isEqual(
                                                identifier("other"),
                                                nuull()
                                        ),
                                        isNotEqual(
                                                callTo(fieldOrMethod("getClass")),
                                                callTo(fieldOrMethod("other", "getClass"))
                                        )),
                                        retuurn(falze())
                                ),
                                cast(true, "_other", "other", classDeclaration),
                                block(from(fieldsOf(classDeclaration).map((fieldDeclaration) -> {
                                            if (isPrimitive(fieldDeclaration)) {
                                                if (isPrimitiveOfType(fieldDeclaration, DOUBLE)) {
                                                    return iif(
                                                            isNotEqual(
                                                                    callTo(
                                                                            fieldOrMethod("Double", "compare"),
                                                                            identifier(fieldDeclaration.name), fieldOrMethod("_other", fieldDeclaration.name)
                                                                    ), literal(0)
                                                            ),
                                                            retuurn(falze())
                                                    );
                                                } else if (isPrimitiveOfType(fieldDeclaration, FLOAT)) {
                                                    return iif(
                                                            isNotEqual(
                                                                    callTo(
                                                                            fieldOrMethod("Float", "compare"),
                                                                            identifier(fieldDeclaration.name), fieldOrMethod("_other", fieldDeclaration.name)
                                                                    ), literal(0)
                                                            ),
                                                            retuurn(falze())
                                                    );
                                                } else {
                                                    return iif(
                                                            isNotEqual(
                                                                    identifier(fieldDeclaration.name),
                                                                    fieldOrMethod("_other", fieldDeclaration.name)
                                                            ),
                                                            retuurn(falze())
                                                    );
                                                }
                                            } else if (isArray(fieldDeclaration)) {
                                                return iif(
                                                        not(
                                                                callTo(
                                                                        fieldOrMethod("java.util.Arrays", "equals"),
                                                                        identifier(fieldDeclaration.name), fieldOrMethod("_other", fieldDeclaration.name)
                                                                )
                                                        ),
                                                        retuurn(falze())
                                                );
                                            } else {
                                                return iif(
                                                        not(
                                                                callTo(
                                                                        fieldOrMethod("java.util.Objects", "equals"),
                                                                        identifier(fieldDeclaration.name), fieldOrMethod("_other", fieldDeclaration.name)
                                                                )
                                                        ),
                                                        retuurn(falze())
                                                );
                                            }
                                        }
                                        ).collect(toList())
                                )),
                                retuurn(truu())
                        ));

                        addMethod(classDeclaration, method(
                                PUBLIC,
                                BOOLEAN,
                                "equals",
                                of(parameter(classDeclaration, "other", objectType())),
                                equalsBody
                        ));
                    }

                    private void addHashCode(JCClassDecl classDeclaration) {
                        JCBlock hashCodeBody = block(of(
                                variable(false,
                                        intType(),
                                        "result",
                                        callTo(
                                                fieldOrMethod("java.util.Objects", "hash"),
                                                arguments(fieldsOf(classDeclaration)
                                                        .filter((fieldDeclaration) -> !isArray(fieldDeclaration))
                                                        .map((fieldDeclaration) -> identifier(fieldDeclaration.name))
                                                )
                                        )
                                ),
                                block(from(fieldsOf(classDeclaration)
                                        .filter((fieldDeclaration) -> isArray(fieldDeclaration))
                                        .map((fieldDeclaration) -> reAssignVariable(
                                                identifier("result"),
                                                add(
                                                        multiply(
                                                                literal(31),
                                                                identifier("result")
                                                        ),
                                                        callTo(
                                                                fieldOrMethod("java.util.Arrays", "hashCode"),
                                                                arguments(identifier(fieldDeclaration.name))
                                                        )
                                                )
                                        ))
                                        .collect(toList())
                                )),
                                retuurn(identifier("result"))
                        ));

                        addMethod(classDeclaration, method(
                                PUBLIC,
                                INT,
                                "hashCode",
                                nil(),
                                hashCodeBody
                        ));
                    }
                }, null);
            }
        });
    }

}
