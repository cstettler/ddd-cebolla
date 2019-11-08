package com.github.cstettler.cebolla.plugin;

class CebollaStereotypePluginException extends Exception {

    private final String key;
    private final Object[] parameters;

    CebollaStereotypePluginException(String key, Object... parameters) {
        super(key);

        this.key = key;
        this.parameters = parameters;
    }

    String key() {
        return key;
    }

    Object[] parameters() {
        return parameters;
    }

}
