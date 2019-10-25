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

    @Override
    public String getName() {
        return "CebollaStereotype";
    }

    @Override
    public void init(JavacTask task, String... args) {
        Context c = ((BasicJavacTask) task).getContext();

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
                            
                            addEquals(classDeclaration);
                            addHashCode(classDeclaration);
                        }

                        return super.visitClass(classTree, data);
                    }

                    private boolean isAnnotatedBy(ClassTree classTree, Class<? extends Annotation> annotationType) {
                        // TODO improve stereotype annotation type matching (in case of import, only simple name is returned)
                        return classTree.getModifiers().getAnnotations().stream()
                                .anyMatch((annotation) -> annotation.getAnnotationType().toString().equals(annotationType.getSimpleName()));
                    }

                    private void addEquals(JCTree.JCClassDecl classDeclaration) {
                        JCTree.JCBlock equalsBody = block(c, of(
                                iif(c,
                                        isEqual(c, thiz(c), identifier(c, "other")),
                                        retuurn(c, truu(c))
                                ),
                                iif(c, or(c,
                                        isEqual(c,
                                                identifier(c, "other"),
                                                nuull(c)
                                        ),
                                        isNotEqual(c,
                                                callTo(c, fieldOrMethod(c, "getClass")),
                                                callTo(c, fieldOrMethod(c, "other", "getClass"))
                                        )),
                                        retuurn(c, falze(c))
                                ),
                                cast(c, "_other", "other", classDeclaration),
                                block(c, from(fieldsOf(classDeclaration).map((fieldDeclaration) -> {
                                            if (isPrimitive(fieldDeclaration)) {
                                                return iif(c,
                                                        isNotEqual(c,
                                                                identifier(c, fieldDeclaration.name),
                                                                fieldOrMethod(c, "_other", fieldDeclaration.name)
                                                        ),
                                                        retuurn(c, falze(c))
                                                );
                                            } else if (isArray(fieldDeclaration)) {
                                                return iif(c,
                                                        not(c,
                                                                callTo(c,
                                                                        fieldOrMethod(c, "java.util.Arrays", "equals"),
                                                                        identifier(c, fieldDeclaration.name), fieldOrMethod(c, "_other", fieldDeclaration.name)
                                                                )
                                                        ),
                                                        retuurn(c, falze(c))
                                                );
                                            } else {
                                                return iif(c,
                                                        not(c,
                                                                callTo(c,
                                                                        fieldOrMethod(c, "java.util.Objects", "equals"),
                                                                        identifier(c, fieldDeclaration.name), fieldOrMethod(c, "_other", fieldDeclaration.name)
                                                                )
                                                        ),
                                                        retuurn(c, falze(c))
                                                );
                                            }
                                        }
                                        ).collect(toList())
                                )),
                                retuurn(c, truu(c))
                        ));

                        addMethod(classDeclaration, method(c,
                                PUBLIC,
                                BOOLEAN,
                                "equals",
                                of(parameter(c, classDeclaration, "other", Symtab.instance(c).objectType)),
                                equalsBody
                        ));
                    }

                    private void addHashCode(JCTree.JCClassDecl classDeclaration) {
                        JCTree.JCBlock hashCodeBody = block(c, of(
                                retuurn(c,
                                        callTo(c,
                                                fieldOrMethod(c, "java.util.Objects", "hash"),
                                                arguments(fieldsOf(classDeclaration).map((fieldDeclaration) -> identifier(c, fieldDeclaration.name)))
                                        )
                                )
                        ));

                        addMethod(classDeclaration, method(c,
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

    private static void addMethod(JCTree.JCClassDecl classDecl, JCTree.JCMethodDecl equalsMethodDecl) {
        classDecl.defs = classDecl.defs.append(equalsMethodDecl);
    }

    private static List<JCTree.JCExpression> arguments(Stream<JCTree.JCExpression> stream) {
        return from(stream.collect(toList()));
    }

    private JCTree.JCMethodDecl method(Context c, int modifier, TypeTag returnType, String name, List<JCTree.JCVariableDecl> parameters, JCTree.JCBlock equalsBody) {
        return TreeMaker.instance(c).MethodDef(
                TreeMaker.instance(c).Modifiers(modifier),
                Names.instance(c).fromString(name),
                TreeMaker.instance(c).TypeIdent(returnType),
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

    private JCTree.JCVariableDecl parameter(Context c, JCTree.JCClassDecl classDeclaration, String parameterName, Type parameterType) {
        return TreeMaker.instance(c).at(classDeclaration.pos).Param(Names.instance(c).fromString(parameterName), parameterType, null);
    }

    private static JCTree.JCBlock block(Context c, List<JCTree.JCStatement> statements) {
        return TreeMaker.instance(c).Block(0, statements);
    }

    private static JCTree.JCUnary not(Context c, JCTree.JCMethodInvocation invocation) {
        return TreeMaker.instance(c).Unary(NOT,
                invocation
        );
    }

    private static boolean isArray(JCTree.JCVariableDecl fieldDeclaration) {
        return fieldDeclaration.vartype instanceof JCTree.JCArrayTypeTree;
    }

    private static JCTree.JCIdent identifier(Context c, Name name) {
        return TreeMaker.instance(c).Ident(name);
    }

    private static boolean isPrimitive(JCTree.JCVariableDecl fieldDeclaration) {
        return fieldDeclaration.vartype instanceof JCTree.JCPrimitiveTypeTree;
    }

    private static JCTree.JCLiteral falze(Context context) {
        return TreeMaker.instance(context).Literal(FALSE);
    }

    private static JCTree.JCLiteral nuull(Context context) {
        return TreeMaker.instance(context).Literal(BOT, null);
    }

    private JCTree.JCBinary or(Context context, JCTree.JCExpression left, JCTree.JCExpression right) {
        return TreeMaker.instance(context).Binary(OR,
                left,
                right
        );
    }

    private JCTree.JCBinary isNotEqual(Context context, JCTree.JCExpression left, JCTree.JCExpression right) {
        return TreeMaker.instance(context).Binary(
                NE,
                left,
                right
        );
    }

    private JCTree.JCMethodInvocation callTo(Context context, JCTree.JCExpression method, JCTree.JCExpression... arguments) {
        return callTo(context, method, from(arguments));
    }

    private JCTree.JCMethodInvocation callTo(Context context, JCTree.JCExpression method, List<JCTree.JCExpression> arguments) {
        return TreeMaker.instance(context).Apply(nil(), method, arguments);
    }

    private static JCTree.JCFieldAccess fieldOrMethod(Context context, String fieldOrMethod) {
        return TreeMaker.instance(context).Select(thiz(context), Names.instance(context).fromString(fieldOrMethod));
    }

    private static JCTree.JCFieldAccess fieldOrMethod(Context context, String target, String fieldOrMethod) {
        return fieldOrMethod(context, target, Names.instance(context).fromString(fieldOrMethod));
    }

    private static JCTree.JCFieldAccess fieldOrMethod(Context context, String target, Name fieldOrMethod) {
        if (target.contains(".")) {
            String[] classNameParts = target.split("\\.");
            JCTree.JCExpression classIdentifier = identifier(context, classNameParts[0]);

            for (int i = 1; i < classNameParts.length; i++) {
                classIdentifier = TreeMaker.instance(context).Select(classIdentifier, Names.instance(context).fromString(classNameParts[i]));
            }

            return TreeMaker.instance(context).Select(
                    classIdentifier,
                    fieldOrMethod
            );
        } else {
            return TreeMaker.instance(context).Select(identifier(context, target), fieldOrMethod);
        }
    }

    private static JCTree.JCVariableDecl cast(Context context, String targetVariableName, String sourceVariableName, JCTree.JCClassDecl targetType) {
        return TreeMaker.instance(context).VarDef(
                TreeMaker.instance(context).Modifiers(0),
                Names.instance(context).fromString(targetVariableName),
                identifier(context, targetType.name),
                TreeMaker.instance(context).TypeCast(identifier(context, targetType.name), identifier(context, sourceVariableName))
        );
    }

    private static JCTree.JCIf iif(Context context, JCTree.JCExpression condition, JCTree.JCStatement body) {
        return TreeMaker.instance(context).If(
                condition,
                body,
                null
        );
    }

    private JCTree.JCReturn retuurn(Context context, JCTree.JCExpression value) {
        return TreeMaker.instance(context).Return(value);
    }

    private JCTree.JCLiteral truu(Context context) {
        return TreeMaker.instance(context).Literal(TRUE);
    }

    private static JCTree.JCExpression thiz(Context context) {
        TreeMaker factory = TreeMaker.instance(context);
        Symtab symtab = Symtab.instance(context);

        return factory.This(symtab.objectType);
    }

    private static JCTree.JCIdent identifier(Context context, String name) {
        TreeMaker factory = TreeMaker.instance(context);
        Names names = Names.instance(context);

        return factory.Ident(names.fromString(name));
    }

    private JCTree.JCBinary isEqual(Context context, JCTree.JCExpression left, JCTree.JCExpression right) {
        TreeMaker factory = TreeMaker.instance(context);

        return factory.Binary(EQ, left, right);
    }

}
