package com.github.cstettler.cebolla.plugin;

import com.github.cstettler.cebolla.stereotype.Aggregate;
import com.github.cstettler.cebolla.stereotype.ValueObject;
import com.sun.source.tree.ClassTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;
import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.JavacMessages;
import com.sun.tools.javac.util.Log;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.github.cstettler.cebolla.plugin.AstUtils.containsAnnotation;
import static com.sun.source.util.TaskEvent.Kind.PARSE;
import static java.util.stream.Collectors.toList;

public class CebollaStereotypesPlugin implements Plugin {

    @Override
    public String getName() {
        return "CebollaStereotype";
    }

    @Override
    public void init(JavacTask task, String... args) {
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

                Context context = ((BasicJavacTask) task).getContext();

                JavacProcessingEnvironment javacProcessingEnvironment = context.get(JavacProcessingEnvironment.class);
                JavacMessages javacMessages = context.get(JavacMessages.messagesKey);
                javacMessages.add((locale) -> ResourceBundle.getBundle(CebollaStereotypesPluginMessages.class.getName(), locale));

                Log log = javacProcessingEnvironment.getContext().get(Log.logKey);

                StereotypeScanner stereotypeScanner = new StereotypeScanner(context, log);
                stereotypeScanner.registerStereotypeHandler(ValueObject.class, new ValueObjectStereotypeHandler());
                stereotypeScanner.registerStereotypeHandler(Aggregate.class, new AggregateStereotypeHandler());

                e.getCompilationUnit().accept(stereotypeScanner, null);
            }
        });
    }


    private static class StereotypeScanner extends TreeScanner<Void, Void> {

        private final Context context;
        private final Log log;
        private final Map<Class<? extends Annotation>, StereotypeHandler> stereotypeHandlerRegistry;

        StereotypeScanner(Context context, Log log) {
            this.context = context;
            this.log = log;
            this.stereotypeHandlerRegistry = new HashMap<>();
        }

        @Override
        public Void visitClass(ClassTree classTree, Void data) {
            JCClassDecl classDeclaration = (JCClassDecl) classTree;

            try {
                Class<? extends Annotation> stereotype = findStereotype(classDeclaration);

                if (stereotype != null) {
                    this.stereotypeHandlerRegistry.get(stereotype).handle(context, classDeclaration);
                }
            } catch (CebollaStereotypePluginException e) {
                this.log.error(classDeclaration.pos, e.key(), e.parameters());
            } catch (Exception e) {
                this.log.rawError(classDeclaration.pos, e.getMessage());
            }

            return super.visitClass(classTree, data);
        }

        void registerStereotypeHandler(Class<? extends Annotation> stereotype, StereotypeHandler stereotypeHandler) {
            this.stereotypeHandlerRegistry.put(stereotype, stereotypeHandler);
        }

        private Class<? extends Annotation> findStereotype(JCClassDecl classDeclaration) {
            List<Class<? extends Annotation>> foundStereotypes = this.stereotypeHandlerRegistry.keySet().stream()
                    .filter((stereotype) -> isAnnotatedWith(classDeclaration, stereotype))
                    .collect(toList());

            if (foundStereotypes.size() > 1) {
                throw new IllegalStateException("class '" + classDeclaration.name.toString() + "' is annotated with multiple stereotypes: " + foundStereotypes);
            }

            return foundStereotypes.size() == 1 ? foundStereotypes.get(0) : null;
        }

        private static boolean isAnnotatedWith(JCClassDecl classDeclaration, Class<? extends Annotation> stereotype) {
            return containsAnnotation(classDeclaration.getModifiers().getAnnotations(), stereotype);
        }

    }

}
