package com.github.cstettler.cebolla.plugin;

import com.github.cstettler.cebolla.stereotype.ValueObject;
import com.sun.source.tree.ClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static com.sun.source.util.TaskEvent.Kind.PARSE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.TypeTag.BOOLEAN;
import static com.sun.tools.javac.code.TypeTag.BOT;
import static com.sun.tools.javac.code.TypeTag.INT;
import static com.sun.tools.javac.tree.JCTree.Tag.EQ;
import static com.sun.tools.javac.tree.JCTree.Tag.NE;
import static com.sun.tools.javac.tree.JCTree.Tag.NOT;
import static com.sun.tools.javac.tree.JCTree.Tag.OR;
import static com.sun.tools.javac.util.List.from;
import static com.sun.tools.javac.util.List.nil;
import static com.sun.tools.javac.util.List.of;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;

public class CebollaStereotypesPlugin implements Plugin {

    private static final ThreadLocal<Context> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

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
                            JCTree.JCClassDecl classDeclaration = (JCTree.JCClassDecl) classTree;

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

                    private void addEquals(JCTree.JCClassDecl classDeclaration) {
                        JCTree.JCBlock equalsBody = block(of(
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
                                cast("_other", "other", classDeclaration),
                                block(from(fieldsOf(classDeclaration).map((fieldDeclaration) -> {
                                            if (isPrimitive(fieldDeclaration)) {
                                                return iif(
                                                        isNotEqual(
                                                                identifier(fieldDeclaration.name),
                                                                fieldOrMethod("_other", fieldDeclaration.name)
                                                        ),
                                                        retuurn(falze())
                                                );
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
                                of(parameter(classDeclaration, "other", Symtab.instance(context()).objectType)),
                                equalsBody
                        ));
                    }

                    private void addHashCode(JCTree.JCClassDecl classDeclaration) {
                        JCTree.JCBlock hashCodeBody = block(of(
                                retuurn(
                                        callTo(
                                                fieldOrMethod("java.util.Objects", "hash"),
                                                arguments(fieldsOf(classDeclaration).map((fieldDeclaration) -> identifier(fieldDeclaration.name)))
                                        )
                                )
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

    private static void with(Context context, Runnable runnable) {
        try {
            CONTEXT_THREAD_LOCAL.set(context);

            runnable.run();
        } finally {
            CONTEXT_THREAD_LOCAL.remove();
        }
    }

    private static void addMethod(JCTree.JCClassDecl classDecl, JCTree.JCMethodDecl equalsMethodDecl) {
        classDecl.defs = classDecl.defs.append(equalsMethodDecl);
    }

    private static List<JCTree.JCExpression> arguments(Stream<JCTree.JCExpression> stream) {
        return from(stream.collect(toList()));
    }

    private static JCTree.JCMethodDecl method(int modifier, TypeTag returnType, String name, List<JCTree.JCVariableDecl> parameters, JCTree.JCBlock equalsBody) {
        return TreeMaker.instance(context()).MethodDef(
                TreeMaker.instance(context()).Modifiers(modifier),
                Names.instance(context()).fromString(name),
                TreeMaker.instance(context()).TypeIdent(returnType),
                nil(),
                parameters,
                nil(),
                equalsBody,
                null
        );
    }

    private static Stream<JCTree.JCVariableDecl> fieldsOf(JCTree.JCClassDecl classDeclaration) {
        return classDeclaration.getMembers().stream()
                .filter((member) -> member instanceof JCTree.JCVariableDecl)
                .map((member) -> (JCTree.JCVariableDecl) member);
    }

    private static JCTree.JCVariableDecl parameter(JCTree.JCClassDecl classDeclaration, String parameterName, Type parameterType) {
        return TreeMaker.instance(context()).at(classDeclaration.pos).Param(Names.instance(context()).fromString(parameterName), parameterType, null);
    }

    private static JCTree.JCBlock block(List<JCTree.JCStatement> statements) {
        return TreeMaker.instance(context()).Block(0, statements);
    }

    private static JCTree.JCUnary not(JCTree.JCMethodInvocation invocation) {
        return TreeMaker.instance(context()).Unary(NOT,
                invocation
        );
    }

    private static boolean isArray(JCTree.JCVariableDecl fieldDeclaration) {
        return fieldDeclaration.vartype instanceof JCTree.JCArrayTypeTree;
    }

    private static JCTree.JCIdent identifier(Name name) {
        return TreeMaker.instance(context()).Ident(name);
    }

    private static boolean isPrimitive(JCTree.JCVariableDecl fieldDeclaration) {
        return fieldDeclaration.vartype instanceof JCTree.JCPrimitiveTypeTree;
    }

    private static JCTree.JCLiteral falze() {
        return TreeMaker.instance(context()).Literal(FALSE);
    }

    private static JCTree.JCLiteral nuull() {
        return TreeMaker.instance(context()).Literal(BOT, null);
    }

    private static JCTree.JCBinary or(JCTree.JCExpression left, JCTree.JCExpression right) {
        return TreeMaker.instance(context()).Binary(OR,
                left,
                right
        );
    }

    private static JCTree.JCBinary isNotEqual(JCTree.JCExpression left, JCTree.JCExpression right) {
        return TreeMaker.instance(context()).Binary(
                NE,
                left,
                right
        );
    }

    private static JCTree.JCMethodInvocation callTo(JCTree.JCExpression method, JCTree.JCExpression... arguments) {
        return callTo(method, from(arguments));
    }

    private static JCTree.JCMethodInvocation callTo(JCTree.JCExpression method, List<JCTree.JCExpression> arguments) {
        return TreeMaker.instance(context()).Apply(nil(), method, arguments);
    }

    private static JCTree.JCFieldAccess fieldOrMethod(String fieldOrMethod) {
        return TreeMaker.instance(context()).Select(thiz(), Names.instance(context()).fromString(fieldOrMethod));
    }

    private static JCTree.JCFieldAccess fieldOrMethod(String target, String fieldOrMethod) {
        return fieldOrMethod(target, Names.instance(context()).fromString(fieldOrMethod));
    }

    private static JCTree.JCFieldAccess fieldOrMethod(String target, Name fieldOrMethod) {
        if (target.contains(".")) {
            String[] classNameParts = target.split("\\.");
            JCTree.JCExpression classIdentifier = identifier(classNameParts[0]);

            for (int i = 1; i < classNameParts.length; i++) {
                classIdentifier = TreeMaker.instance(context()).Select(classIdentifier, Names.instance(context()).fromString(classNameParts[i]));
            }

            return TreeMaker.instance(context()).Select(
                    classIdentifier,
                    fieldOrMethod
            );
        } else {
            return TreeMaker.instance(context()).Select(identifier(target), fieldOrMethod);
        }
    }

    private static JCTree.JCVariableDecl cast(String targetVariableName, String sourceVariableName, JCTree.JCClassDecl targetType) {
        return TreeMaker.instance(context()).VarDef(
                TreeMaker.instance(context()).Modifiers(0),
                Names.instance(context()).fromString(targetVariableName),
                identifier(targetType.name),
                TreeMaker.instance(context()).TypeCast(identifier(targetType.name), identifier(sourceVariableName))
        );
    }

    private static JCTree.JCIf iif(JCTree.JCExpression condition, JCTree.JCStatement body) {
        return TreeMaker.instance(context()).If(
                condition,
                body,
                null
        );
    }

    private static JCTree.JCReturn retuurn(JCTree.JCExpression value) {
        return TreeMaker.instance(context()).Return(value);
    }

    private static JCTree.JCLiteral truu() {
        return TreeMaker.instance(context()).Literal(TRUE);
    }

    private static JCTree.JCExpression thiz() {
        TreeMaker factory = TreeMaker.instance(context());
        Symtab symtab = Symtab.instance(context());

        return factory.This(symtab.objectType);
    }

    private static JCTree.JCIdent identifier(String name) {
        TreeMaker factory = TreeMaker.instance(context());
        Names names = Names.instance(context());

        return factory.Ident(names.fromString(name));
    }

    private static JCTree.JCBinary isEqual(JCTree.JCExpression left, JCTree.JCExpression right) {
        TreeMaker factory = TreeMaker.instance(context());

        return factory.Binary(EQ, left, right);
    }

    private static Context context() {
        return CONTEXT_THREAD_LOCAL.get();
    }

}
