package com.github.cstettler.cebolla.plugin;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import static com.github.cstettler.cebolla.plugin.AstUtils.add;
import static com.github.cstettler.cebolla.plugin.AstUtils.addMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.arguments;
import static com.github.cstettler.cebolla.plugin.AstUtils.block;
import static com.github.cstettler.cebolla.plugin.AstUtils.callTo;
import static com.github.cstettler.cebolla.plugin.AstUtils.cast;
import static com.github.cstettler.cebolla.plugin.AstUtils.constructor;
import static com.github.cstettler.cebolla.plugin.AstUtils.falze;
import static com.github.cstettler.cebolla.plugin.AstUtils.fieldOrMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.fieldsOf;
import static com.github.cstettler.cebolla.plugin.AstUtils.identifier;
import static com.github.cstettler.cebolla.plugin.AstUtils.iif;
import static com.github.cstettler.cebolla.plugin.AstUtils.intType;
import static com.github.cstettler.cebolla.plugin.AstUtils.isArray;
import static com.github.cstettler.cebolla.plugin.AstUtils.isEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.isNotEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.literal;
import static com.github.cstettler.cebolla.plugin.AstUtils.method;
import static com.github.cstettler.cebolla.plugin.AstUtils.methodsOf;
import static com.github.cstettler.cebolla.plugin.AstUtils.multiply;
import static com.github.cstettler.cebolla.plugin.AstUtils.not;
import static com.github.cstettler.cebolla.plugin.AstUtils.nullValueFor;
import static com.github.cstettler.cebolla.plugin.AstUtils.nuull;
import static com.github.cstettler.cebolla.plugin.AstUtils.objectType;
import static com.github.cstettler.cebolla.plugin.AstUtils.or;
import static com.github.cstettler.cebolla.plugin.AstUtils.parameter;
import static com.github.cstettler.cebolla.plugin.AstUtils.propertyEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.assignExistingVariable;
import static com.github.cstettler.cebolla.plugin.AstUtils.retuurn;
import static com.github.cstettler.cebolla.plugin.AstUtils.thiz;
import static com.github.cstettler.cebolla.plugin.AstUtils.truu;
import static com.github.cstettler.cebolla.plugin.AstUtils.variable;
import static com.github.cstettler.cebolla.plugin.AstUtils.with;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.TypeTag.BOOLEAN;
import static com.sun.tools.javac.code.TypeTag.INT;
import static com.sun.tools.javac.util.List.from;
import static com.sun.tools.javac.util.List.nil;
import static com.sun.tools.javac.util.List.of;
import static java.util.stream.Collectors.toList;

class ValueObjectStereotypeHandler implements StereotypeHandler {

    @Override
    public void handle(Context context, JCClassDecl classDeclaration) throws CebollaStereotypePluginException {
        with(context, () -> {
            if (hasNoDefaultConstructor(classDeclaration)) {
                addDefaultConstructor(classDeclaration);
            }
            addEqualsMethod(classDeclaration);
            addHashCodeMethod(classDeclaration);
        });
    }

    private static boolean hasNoDefaultConstructor(JCClassDecl classDeclaration) {
        return methodsOf(classDeclaration)
                .filter((member) -> member.name.toString().equals("<init>"))
                .noneMatch((member) -> member.params.size() == 0);
    }

    private static void addDefaultConstructor(JCClassDecl classDeclaration) {
        addMethod(classDeclaration, constructor(
                PUBLIC,
                nil(),
                block(from(fieldsOf(classDeclaration)
                        .map((fieldDeclaration) -> assignExistingVariable(fieldOrMethod(thiz(), fieldDeclaration.name), nullValueFor(fieldDeclaration.vartype)))
                        .collect(toList())
                ))
        ));
    }

    private static void addEqualsMethod(JCClassDecl classDeclaration) {
        JCTree.JCBlock equalsBody = block(List.of(
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
                                callTo(fieldOrMethod(thiz(), "getClass")),
                                callTo(fieldOrMethod("other", "getClass"))
                        )),
                        retuurn(falze())
                ),
                cast(true, "_other", "other", classDeclaration),
                block(from(fieldsOf(classDeclaration).map((fieldDeclaration) ->
                        iif(
                                not(
                                        propertyEqual(
                                                fieldDeclaration.vartype,
                                                identifier(fieldDeclaration.name),
                                                fieldOrMethod("_other", fieldDeclaration.name)
                                        )),
                                retuurn(falze())
                        )).collect(toList())
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

    private static void addHashCodeMethod(JCClassDecl classDeclaration) {
        JCTree.JCBlock hashCodeBody = block(of(
                variable(false,
                        intType(),
                        "result",
                        callTo(
                                fieldOrMethod("java.util.Objects", "hash"),
                                arguments(fieldsOf(classDeclaration)
                                        .filter((fieldDeclaration) -> !isArray(fieldDeclaration.vartype))
                                        .map((fieldDeclaration) -> identifier(fieldDeclaration.name))
                                )
                        )
                ),
                block(from(fieldsOf(classDeclaration)
                        .filter((fieldDeclaration) -> isArray(fieldDeclaration.vartype))
                        .map((fieldDeclaration) -> assignExistingVariable(
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

}
