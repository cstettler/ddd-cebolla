package com.github.cstettler.dscp;

import com.github.cstettler.dscp.stereotype.ValueObject;
import com.sun.source.tree.ClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import java.util.Collection;

import static com.sun.source.util.TaskEvent.Kind.PARSE;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.TypeTag.BOOLEAN;
import static com.sun.tools.javac.code.TypeTag.BOT;
import static com.sun.tools.javac.tree.JCTree.Tag.EQ;
import static com.sun.tools.javac.tree.JCTree.Tag.NE;
import static com.sun.tools.javac.tree.JCTree.Tag.NOT;
import static com.sun.tools.javac.tree.JCTree.Tag.OR;
import static com.sun.tools.javac.util.List.nil;
import static com.sun.tools.javac.util.List.of;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;

public class CebollaStereotypesPlugin implements Plugin {

    @Override
    public String getName() {
        return "CebollaStereotypes";
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
                        if (annotationsOf(classTree).contains(ValueObject.class.getName())) {
                            addEqualsAndHashCode(classTree);
                        }

                        return super.visitClass(classTree, data);
                    }

                    private Collection<String> annotationsOf(ClassTree classTree) {
                        return classTree.getModifiers().getAnnotations().stream()
                                .map((annotation) -> annotation.getAnnotationType().toString())
                                .collect(toList());
                    }

                    private void addEqualsAndHashCode(ClassTree classTree) {
                        JCTree.JCClassDecl classDeclaration = (JCTree.JCClassDecl) classTree;

                        TreeMaker factory = TreeMaker.instance(context);
                        Names names = Names.instance(context);
                        Symtab symtab = Symtab.instance(context);

                        List<JCTree.JCStatement> equalStatements = of(
                                factory.If(
                                        factory.Binary(EQ, factory.This(symtab.objectType), factory.Ident(names.fromString("other"))),
                                        factory.Return(factory.Literal(TRUE)),
                                        null
                                ),
                                factory.If(
                                        factory.Binary(OR,
                                                factory.Binary(
                                                        EQ,
                                                        factory.Ident(names.fromString("other")),
                                                        factory.Literal(BOT, null)
                                                ),
                                                factory.Binary(
                                                        NE,
                                                        factory.Apply(nil(), factory.Ident(names.fromString("getClass")), nil()),
                                                        factory.Apply(nil(), factory.Select(factory.Ident(names.fromString("other")), names.fromString("getClass")), nil())
                                                )
                                        ),
                                        factory.Return(factory.Literal(FALSE)),
                                        null
                                ),
                                factory.VarDef(
                                        factory.Modifiers(0),
                                        names.fromString("_other"),
                                        factory.Ident(classDeclaration.name),
                                        factory.TypeCast(factory.Ident(classDeclaration.name), factory.Ident(names.fromString("other")))
                                ),
                                factory.Block(
                                        0,
                                        List.from(classDeclaration.getMembers().stream()
                                                .filter((member) -> member instanceof JCTree.JCVariableDecl)
                                                .map((member) -> (JCTree.JCVariableDecl) member)
                                                .map((fieldDeclaration) -> {
                                                            if (fieldDeclaration.vartype instanceof JCTree.JCPrimitiveTypeTree) {
                                                                return factory.If(
                                                                        factory.Binary(
                                                                                NE,
                                                                                factory.Ident(fieldDeclaration.name),
                                                                                factory.Select(factory.Ident(names.fromString("_other")), fieldDeclaration.name)
                                                                        ),
                                                                        factory.Return(factory.Literal(FALSE)),
                                                                        null);
                                                            } else if (fieldDeclaration.vartype instanceof JCTree.JCArrayTypeTree) {
                                                                return factory.If(
                                                                        factory.Unary(NOT,
                                                                                factory.Apply(
                                                                                        nil(),
                                                                                        factory.Select(
                                                                                                factory.Select(factory.Select(factory.Ident(names.fromString("java")), names.fromString("util")), names.fromString("Arrays")),
                                                                                                names.fromString("equals")
                                                                                        ),
                                                                                        of(factory.Ident(fieldDeclaration.name), factory.Select(factory.Ident(names.fromString("_other")), fieldDeclaration.name))
                                                                                )
                                                                        ),
                                                                        factory.Return(factory.Literal(FALSE)),
                                                                        null);
                                                            } else {
                                                                return factory.If(
                                                                        factory.Unary(NOT,
                                                                                factory.Apply(
                                                                                        nil(),
                                                                                        factory.Select(
                                                                                                factory.Select(factory.Select(factory.Ident(names.fromString("java")), names.fromString("util")), names.fromString("Objects")),
                                                                                                names.fromString("equals")
                                                                                        ),
                                                                                        of(factory.Ident(fieldDeclaration.name), factory.Select(factory.Ident(names.fromString("_other")), fieldDeclaration.name))
                                                                                )
                                                                        ),
                                                                        factory.Return(factory.Literal(FALSE)),
                                                                        null);
                                                            }
                                                        }
                                                ).collect(toList())
                                        )
                                ),
                                factory.Return(factory.Literal(TRUE))
                        );


                        JCTree.JCMethodDecl equalsMethodDeclaration = factory.at(classDeclaration.pos).MethodDef(
                                factory.Modifiers(PUBLIC),
                                names.fromString("equals"),
                                factory.TypeIdent(BOOLEAN),
                                nil(),
                                of(factory.Param(names.fromString("other"), symtab.objectType, null)),
                                nil(),
                                factory.Block(0, equalStatements),
                                null
                        );

                        addMethod(classDeclaration, equalsMethodDeclaration);

                        System.out.println(classTree);
                    }

                    private void addMethod(JCTree.JCClassDecl classDecl, JCTree.JCMethodDecl equalsMethodDecl) {
                        classDecl.defs = classDecl.defs.append(equalsMethodDecl);
                    }
                }, null);
            }
        });
    }

}
