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
import com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
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
import static com.sun.tools.javac.code.TypeTag.DOUBLE;
import static com.sun.tools.javac.code.TypeTag.FLOAT;
import static com.sun.tools.javac.code.TypeTag.INT;
import static com.sun.tools.javac.tree.JCTree.Tag.EQ;
import static com.sun.tools.javac.tree.JCTree.Tag.MUL;
import static com.sun.tools.javac.tree.JCTree.Tag.NE;
import static com.sun.tools.javac.tree.JCTree.Tag.NOT;
import static com.sun.tools.javac.tree.JCTree.Tag.OR;
import static com.sun.tools.javac.tree.JCTree.Tag.PLUS;
import static com.sun.tools.javac.util.List.from;
import static com.sun.tools.javac.util.List.nil;
import static com.sun.tools.javac.util.List.of;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
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
                                cast("_other", "other", classDeclaration),
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
                                variable(intType(),
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

    private static void with(Context context, Runnable runnable) {
        try {
            CONTEXT_THREAD_LOCAL.set(context);

            runnable.run();
        } finally {
            CONTEXT_THREAD_LOCAL.remove();
        }
    }

    private static void addMethod(JCClassDecl classDecl, JCMethodDecl equalsMethodDecl) {
        classDecl.defs = classDecl.defs.append(equalsMethodDecl);
    }

    private static List<JCExpression> arguments(Stream<JCExpression> stream) {
        return from(stream.collect(toList()));
    }

    private static List<JCExpression> arguments(JCExpression... expressions) {
        return from(asList(expressions));
    }

    private static JCMethodDecl method(int modifier, TypeTag returnType, String name, List<JCVariableDecl> parameters, JCBlock equalsBody) {
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

    private static Stream<JCVariableDecl> fieldsOf(JCClassDecl classDeclaration) {
        return classDeclaration.getMembers().stream()
                .filter((member) -> member instanceof JCVariableDecl)
                .map((member) -> (JCVariableDecl) member);
    }

    private static JCVariableDecl parameter(JCClassDecl classDeclaration, String parameterName, Type parameterType) {
        return TreeMaker.instance(context()).at(classDeclaration.pos).Param(Names.instance(context()).fromString(parameterName), parameterType, null);
    }

    private static JCBlock block(List<JCStatement> statements) {
        return TreeMaker.instance(context()).Block(0, statements);
    }

    private static JCUnary not(JCMethodInvocation invocation) {
        return TreeMaker.instance(context()).Unary(NOT,
                invocation
        );
    }

    private static boolean isArray(JCVariableDecl fieldDeclaration) {
        return fieldDeclaration.vartype instanceof JCArrayTypeTree;
    }

    private static JCExpression identifier(Name name) {
        return TreeMaker.instance(context()).Ident(name);
    }

    private static boolean isPrimitive(JCVariableDecl fieldDeclaration) {
        return fieldDeclaration.vartype instanceof JCPrimitiveTypeTree;
    }

    private static boolean isPrimitiveOfType(JCVariableDecl fieldDeclaration, TypeTag typeTag) {
        return isPrimitive(fieldDeclaration)
                && fieldDeclaration.vartype instanceof JCPrimitiveTypeTree
                && ((JCPrimitiveTypeTree) fieldDeclaration.vartype).typetag == typeTag;
    }

    private static JCLiteral falze() {
        return TreeMaker.instance(context()).Literal(FALSE);
    }

    private static JCLiteral nuull() {
        return TreeMaker.instance(context()).Literal(BOT, null);
    }

    private static JCBinary or(JCExpression left, JCExpression right) {
        return TreeMaker.instance(context()).Binary(OR, left, right);
    }

    private static JCBinary isNotEqual(JCExpression left, JCExpression right) {
        return TreeMaker.instance(context()).Binary(NE, left, right);
    }

    private static JCBinary add(JCExpression left, JCExpression right) {
        return TreeMaker.instance(context()).Binary(PLUS, left, right);
    }

    private static JCBinary multiply(JCExpression left, JCExpression right) {
        return TreeMaker.instance(context()).Binary(MUL, left, right);
    }

    private static JCStatement reAssignVariable(JCIdent variable, JCBinary expression) {
        return TreeMaker.instance(context()).Exec(TreeMaker.instance(context()).Assign(variable, expression));
    }

    private static JCMethodInvocation callTo(JCExpression method, JCExpression... arguments) {
        return callTo(method, from(arguments));
    }

    private static JCMethodInvocation callTo(JCExpression method, List<JCExpression> arguments) {
        return TreeMaker.instance(context()).Apply(nil(), method, arguments);
    }

    private static JCFieldAccess fieldOrMethod(String fieldOrMethod) {
        return TreeMaker.instance(context()).Select(thiz(), Names.instance(context()).fromString(fieldOrMethod));
    }

    private static JCFieldAccess fieldOrMethod(String target, String fieldOrMethod) {
        return fieldOrMethod(target, Names.instance(context()).fromString(fieldOrMethod));
    }

    private static JCFieldAccess fieldOrMethod(String target, Name fieldOrMethod) {
        if (target.contains(".")) {
            String[] classNameParts = target.split("\\.");
            JCExpression classIdentifier = identifier(classNameParts[0]);

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

    private static JCVariableDecl cast(String targetVariableName, String sourceVariableName, JCClassDecl targetType) {
        return variable(
                identifier(targetType.name),
                targetVariableName,
                TreeMaker.instance(context()).TypeCast(identifier(targetType.name), identifier(sourceVariableName))
        );
    }

    private static JCVariableDecl variable(Type type, String name, JCExpression initialization) {
        return variable(TreeMaker.instance(context()).Type(type), name, initialization);
    }

    private static JCVariableDecl variable(JCExpression type, String name, JCExpression initialization) {
        return TreeMaker.instance(context()).VarDef(
                TreeMaker.instance(context()).Modifiers(0),
                Names.instance(context()).fromString(name),
                type,
                initialization
        );
    }

    private static JCIf iif(JCExpression condition, JCStatement body) {
        return TreeMaker.instance(context()).If(
                condition,
                body,
                null
        );
    }

    private static JCReturn retuurn(JCExpression value) {
        return TreeMaker.instance(context()).Return(value);
    }

    private static JCLiteral truu() {
        return TreeMaker.instance(context()).Literal(TRUE);
    }

    private static JCExpression thiz() {
        return TreeMaker.instance(context()).This(objectType());
    }

    private static JCIdent identifier(String name) {
        TreeMaker factory = TreeMaker.instance(context());
        Names names = Names.instance(context());

        return factory.Ident(names.fromString(name));
    }


    private static JCLiteral literal(Object literal) {
        return TreeMaker.instance(context()).Literal(literal);
    }

    private static JCBinary isEqual(JCExpression left, JCExpression right) {
        TreeMaker factory = TreeMaker.instance(context());

        return factory.Binary(EQ, left, right);
    }

    private static Type objectType() {
        return Symtab.instance(context()).objectType;
    }

    private static Type intType() {
        return Symtab.instance(context()).intType;
    }

    private static Context context() {
        return CONTEXT_THREAD_LOCAL.get();
    }

}
