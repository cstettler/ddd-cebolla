package com.github.cstettler.cebolla.plugin;

import com.github.cstettler.cebolla.stereotype.AggregateId;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import java.lang.annotation.Annotation;

import static com.github.cstettler.cebolla.plugin.AstUtils.addMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.arguments;
import static com.github.cstettler.cebolla.plugin.AstUtils.block;
import static com.github.cstettler.cebolla.plugin.AstUtils.callTo;
import static com.github.cstettler.cebolla.plugin.AstUtils.cast;
import static com.github.cstettler.cebolla.plugin.AstUtils.containsAnnotation;
import static com.github.cstettler.cebolla.plugin.AstUtils.falze;
import static com.github.cstettler.cebolla.plugin.AstUtils.fieldOrMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.hasNoDeclaredEqualsMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.hasNoDeclaredHashCodeMethod;
import static com.github.cstettler.cebolla.plugin.AstUtils.identifier;
import static com.github.cstettler.cebolla.plugin.AstUtils.iif;
import static com.github.cstettler.cebolla.plugin.AstUtils.isArray;
import static com.github.cstettler.cebolla.plugin.AstUtils.isEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.isNotEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.method;
import static com.github.cstettler.cebolla.plugin.AstUtils.methodsOf;
import static com.github.cstettler.cebolla.plugin.AstUtils.nuull;
import static com.github.cstettler.cebolla.plugin.AstUtils.objectType;
import static com.github.cstettler.cebolla.plugin.AstUtils.or;
import static com.github.cstettler.cebolla.plugin.AstUtils.parameter;
import static com.github.cstettler.cebolla.plugin.AstUtils.propertyEqual;
import static com.github.cstettler.cebolla.plugin.AstUtils.retuurn;
import static com.github.cstettler.cebolla.plugin.AstUtils.thiz;
import static com.github.cstettler.cebolla.plugin.AstUtils.truu;
import static com.github.cstettler.cebolla.plugin.AstUtils.with;
import static com.github.cstettler.cebolla.plugin.CebollaStereotypesPluginMessages.AGGREGATE_WITHOUT_AGGREGATE_ID;
import static com.sun.tools.javac.code.Flags.PUBLIC;
import static com.sun.tools.javac.code.TypeTag.BOOLEAN;
import static com.sun.tools.javac.code.TypeTag.INT;
import static com.sun.tools.javac.tree.JCTree.JCBlock;
import static com.sun.tools.javac.util.List.nil;
import static com.sun.tools.javac.util.List.of;

class AggregateStereotypeHandler implements StereotypeHandler {

    @Override
    public void handle(Context context, JCClassDecl classDeclaration) throws CebollaStereotypePluginException {
        with(context, () -> {
            if (hasNoDeclaredEqualsMethod(classDeclaration)) {
                addEqualsMethod(classDeclaration);
            }
            if (hasNoDeclaredHashCodeMethod(classDeclaration)) {
                addHashCodeMethod(classDeclaration);
            }
        });
    }

    private void addEqualsMethod(JCClassDecl classDeclaration) throws CebollaStereotypePluginException {
        JCMethodDecl aggregateIdAccessorMethod = findMethodAnnotatedWith(classDeclaration, AggregateId.class);

        JCBlock equalsBody = block(List.of(
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
                retuurn(
                        propertyEqual(
                                aggregateIdAccessorMethod.restype,
                                callTo(fieldOrMethod(thiz(), aggregateIdAccessorMethod.name)),
                                callTo(fieldOrMethod("_other", aggregateIdAccessorMethod.name))
                        )
                )
        ));

        addMethod(classDeclaration, method(
                PUBLIC,
                BOOLEAN,
                "equals",
                of(parameter(classDeclaration, "other", objectType())),
                equalsBody
        ));
    }

    private static void addHashCodeMethod(JCClassDecl classDeclaration) throws CebollaStereotypePluginException {
        JCMethodDecl aggregateIdAccessorMethod = findMethodAnnotatedWith(classDeclaration, AggregateId.class);

        JCBlock hashCodeBody;

        if (isArray(aggregateIdAccessorMethod.restype)) {
            hashCodeBody = block(of(
                    retuurn(
                            callTo(
                                    fieldOrMethod("java.util.Arrays", "hashCode"),
                                    arguments(callTo(fieldOrMethod(thiz(), aggregateIdAccessorMethod.name)))
                            )
                    )
            ));
        } else {
            hashCodeBody = block(of(
                    retuurn(
                            callTo(
                                    fieldOrMethod("java.util.Objects", "hash"),
                                    arguments(callTo(fieldOrMethod(thiz(), aggregateIdAccessorMethod.name)))
                            )
                    )
            ));
        }

        addMethod(classDeclaration, method(
                PUBLIC,
                INT,
                "hashCode",
                nil(),
                hashCodeBody
        ));
    }

    private static JCMethodDecl findMethodAnnotatedWith(JCClassDecl classDeclaration, Class<? extends Annotation> stereotype) throws CebollaStereotypePluginException {
        return methodsOf(classDeclaration)
                .filter((method) -> containsAnnotation(method.getModifiers().getAnnotations(), stereotype))
                .findFirst()
                .orElseThrow(() -> new CebollaStereotypePluginException(AGGREGATE_WITHOUT_AGGREGATE_ID, classDeclaration.name.toString(), AggregateId.class.getName()));
    }

}
