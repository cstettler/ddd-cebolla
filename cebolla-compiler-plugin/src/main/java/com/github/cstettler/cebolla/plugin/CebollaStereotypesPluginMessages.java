package com.github.cstettler.cebolla.plugin;

import java.util.ListResourceBundle;

public class CebollaStereotypesPluginMessages extends ListResourceBundle {

    static final String AGGREGATE_WITHOUT_AGGREGATE_ID = "aggregateWithoutAggregateId";

    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {errorKey(AGGREGATE_WITHOUT_AGGREGATE_ID), "aggregate ''{0}'' has no aggregate id accessor method annotated with ''{1}''"}
        };
    }

    private static String errorKey(String messageKey) {
        return "compiler.err." + messageKey;
    }

}
