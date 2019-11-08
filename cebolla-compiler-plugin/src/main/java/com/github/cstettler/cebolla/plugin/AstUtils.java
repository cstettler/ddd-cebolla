package com.github.cstettler.cebolla.plugin;

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

import static com.sun.tools.javac.code.Flags.FINAL;
import static com.sun.tools.javac.code.TypeTag.BOT;
import static com.sun.tools.javac.code.TypeTag.DOUBLE;
import static com.sun.tools.javac.code.TypeTag.FLOAT;
import static com.sun.tools.javac.tree.JCTree.JCArrayTypeTree;
import static com.sun.tools.javac.tree.JCTree.JCBinary;
import static com.sun.tools.javac.tree.JCTree.JCBlock;
import static com.sun.tools.javac.tree.JCTree.JCClassDecl;
import static com.sun.tools.javac.tree.JCTree.JCExpression;
import static com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import static com.sun.tools.javac.tree.JCTree.JCIdent;
import static com.sun.tools.javac.tree.JCTree.JCIf;
import static com.sun.tools.javac.tree.JCTree.JCLiteral;
import static com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import static com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import static com.sun.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import static com.sun.tools.javac.tree.JCTree.JCReturn;
import static com.sun.tools.javac.tree.JCTree.JCStatement;
import static com.sun.tools.javac.tree.JCTree.JCUnary;
import static com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import static com.sun.tools.javac.tree.JCTree.Tag.EQ;
import static com.sun.tools.javac.tree.JCTree.Tag.MUL;
import static com.sun.tools.javac.tree.JCTree.Tag.NE;
import static com.sun.tools.javac.tree.JCTree.Tag.NOT;
import static com.sun.tools.javac.tree.JCTree.Tag.OR;
import static com.sun.tools.javac.tree.JCTree.Tag.PLUS;
import static com.sun.tools.javac.util.List.from;
import static com.sun.tools.javac.util.List.nil;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess")
class AstUtils {

    private static final ThreadLocal<Context> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private AstUtils() {
    }

    static void with(Context context, HandlerRunnable runnable) throws CebollaStereotypePluginException {
        try {
            CONTEXT_THREAD_LOCAL.set(context);

            runnable.run();
        } finally {
            CONTEXT_THREAD_LOCAL.remove();
        }
    }

    static void addMethod(JCClassDecl classDecl, JCMethodDecl equalsMethodDecl) {
        classDecl.defs = classDecl.defs.append(equalsMethodDecl);
    }

    static List<JCExpression> arguments(Stream<JCExpression> stream) {
        return from(stream.collect(toList()));
    }

    static List<JCExpression> arguments(JCExpression... expressions) {
        return from(asList(expressions));
    }

    static JCMethodDecl method(int modifier, TypeTag returnType, String name, List<JCVariableDecl> parameters, JCBlock equalsBody) {
        return factory().MethodDef(
                factory().Modifiers(modifier),
                names().fromString(name),
                factory().TypeIdent(returnType),
                nil(),
                parameters,
                nil(),
                equalsBody,
                null
        );
    }

    static Stream<JCVariableDecl> fieldsOf(JCClassDecl classDeclaration) {
        return classDeclaration.getMembers().stream()
                .filter((member) -> member instanceof JCVariableDecl)
                .map((member) -> (JCVariableDecl) member);
    }

    static Stream<JCMethodDecl> methodsOf(JCClassDecl classDeclaration) {
        return classDeclaration.getMembers().stream()
                .filter((member) -> member instanceof JCMethodDecl)
                .map((member) -> (JCMethodDecl) member);
    }

    static JCVariableDecl parameter(JCClassDecl classDeclaration, String parameterName, Type parameterType) {
        return factory().at(classDeclaration.pos).Param(names().fromString(parameterName), parameterType, null);
    }

    static JCBlock block(List<JCStatement> statements) {
        return factory().Block(0, statements);
    }

    static JCUnary not(JCExpression expression) {
        return factory().Unary(NOT, expression);
    }

    static boolean isArray(JCExpression propertyType) {
        return propertyType instanceof JCArrayTypeTree;
    }

    static JCExpression identifier(Name name) {
        return factory().Ident(name);
    }

    static boolean isPrimitive(JCExpression propertyType) {
        return propertyType instanceof JCPrimitiveTypeTree;
    }

    static boolean isPrimitiveOfType(JCExpression propertyType, TypeTag typeTag) {
        return isPrimitive(propertyType) && ((JCPrimitiveTypeTree) propertyType).typetag == typeTag;
    }

    static JCLiteral falze() {
        return factory().Literal(FALSE);
    }

    static JCLiteral nuull() {
        return factory().Literal(BOT, null);
    }

    static JCBinary or(JCExpression left, JCExpression right) {
        return factory().Binary(OR, left, right);
    }

    static JCBinary isNotEqual(JCExpression left, JCExpression right) {
        return factory().Binary(NE, left, right);
    }

    static JCBinary add(JCExpression left, JCExpression right) {
        return factory().Binary(PLUS, left, right);
    }

    static JCBinary multiply(JCExpression left, JCExpression right) {
        return factory().Binary(MUL, left, right);
    }

    static JCStatement reAssignVariable(JCIdent variable, JCBinary expression) {
        return factory().Exec(factory().Assign(variable, expression));
    }

    static JCMethodInvocation callTo(JCExpression method, JCExpression... arguments) {
        return callTo(method, from(arguments));
    }

    static JCMethodInvocation callTo(JCExpression method, List<JCExpression> arguments) {
        return factory().Apply(nil(), method, arguments);
    }

    static JCFieldAccess fieldOrMethod(JCExpression target, String fieldOrMethod) {
        return fieldOrMethod(target, names().fromString(fieldOrMethod));
    }

    static JCFieldAccess fieldOrMethod(JCExpression target, Name fieldOrMethod) {
        return factory().Select(target, fieldOrMethod);
    }

    static JCFieldAccess fieldOrMethod(String target, String fieldOrMethod) {
        return fieldOrMethod(target, names().fromString(fieldOrMethod));
    }

    static JCFieldAccess fieldOrMethod(String target, Name fieldOrMethod) {
        if (target.contains(".")) {
            String[] classNameParts = target.split("\\.");
            JCExpression classIdentifier = identifier(classNameParts[0]);

            for (int i = 1; i < classNameParts.length; i++) {
                classIdentifier = factory().Select(classIdentifier, names().fromString(classNameParts[i]));
            }

            return factory().Select(
                    classIdentifier,
                    fieldOrMethod
            );
        } else {
            return factory().Select(identifier(target), fieldOrMethod);
        }
    }

    static JCVariableDecl cast(boolean makeFinal, String targetVariableName, String sourceVariableName, JCClassDecl targetType) {
        return variable(
                makeFinal,
                identifier(targetType.name),
                targetVariableName,
                factory().TypeCast(identifier(targetType.name), identifier(sourceVariableName))
        );
    }

    static JCVariableDecl variable(boolean makeFinal, Type type, String name, JCExpression initialization) {
        return variable(makeFinal, factory().Type(type), name, initialization);
    }

    static JCVariableDecl variable(boolean makeFinal, JCExpression type, String name, JCExpression initialization) {
        return factory().VarDef(
                factory().Modifiers(makeFinal ? FINAL : 0),
                names().fromString(name),
                type,
                initialization
        );
    }

    static JCIf iif(JCExpression condition, JCStatement body) {
        return factory().If(condition, body, null);
    }

    static JCReturn retuurn(JCExpression value) {
        return factory().Return(value);
    }

    static JCLiteral truu() {
        return factory().Literal(TRUE);
    }

    static JCExpression thiz() {
        return factory().This(objectType());
    }

    static JCIdent identifier(String name) {
        return factory().Ident(names().fromString(name));
    }

    static JCLiteral literal(Object literal) {
        return factory().Literal(literal);
    }

    static JCBinary isEqual(JCExpression left, JCExpression right) {
        return factory().Binary(EQ, left, right);
    }

    static Type objectType() {
        return symtab().objectType;
    }

    static Type intType() {
        return symtab().intType;
    }

    static JCExpression propertyEqual(JCExpression propertyType, JCExpression ownValue, JCExpression otherValue) {
        if (isPrimitive(propertyType)) {
            if (isPrimitiveOfType(propertyType, DOUBLE)) {
                return isEqual(
                        callTo(
                                fieldOrMethod("Double", "compare"),
                                ownValue, otherValue
                        ), literal(0)
                );
            } else if (isPrimitiveOfType(propertyType, FLOAT)) {
                return isEqual(
                        callTo(
                                fieldOrMethod("Float", "compare"),
                                ownValue, otherValue
                        ), literal(0)
                );
            } else {
                return isEqual(
                        ownValue,
                        otherValue
                );
            }
        } else if (isArray(propertyType)) {
            return callTo(
                    fieldOrMethod("java.util.Arrays", "equals"),
                    ownValue, otherValue
            );
        } else {
            return
                    callTo(
                            fieldOrMethod("java.util.Objects", "equals"),
                            ownValue, otherValue
                    );
        }
    }

    private static TreeMaker factory() {
        return TreeMaker.instance(context());
    }

    private static Names names() {
        return Names.instance(context());
    }

    private static Symtab symtab() {
        return Symtab.instance(context());
    }

    private static Context context() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    static boolean containsAnnotation(java.util.List<JCTree.JCAnnotation> annotations, Class<? extends Annotation> annotationType) {
        // TODO improve stereotype annotation type matching (in case of import, only simple name is returned)
        return annotations.stream()
                .anyMatch((annotation) -> annotation.getAnnotationType().toString().equals(annotationType.getSimpleName()));
    }


    @FunctionalInterface
    interface HandlerRunnable {

        void run() throws CebollaStereotypePluginException;

    }

}
