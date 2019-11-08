package com.github.cstettler.cebolla.plugin;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;

interface StereotypeHandler {

    void handle(Context context, JCClassDecl classDeclaration) throws CebollaStereotypePluginException;

}
