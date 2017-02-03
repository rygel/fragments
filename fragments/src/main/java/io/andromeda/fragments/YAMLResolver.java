package io.andromeda.fragments;

import org.yaml.snakeyaml.resolver.Resolver;

/**
 * Created by Alexander on 29.01.2017.
 */
public class YAMLResolver extends Resolver {

    /**
     * do not resolve any Tag.
     */
    @Override
    protected void addImplicitResolvers() {
        //addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
        // addImplicitResolver(Tags.FLOAT, FLOAT, "-+0123456789.");
        //addImplicitResolver(Tag.INT, INT, "-+0123456789");
        //addImplicitResolver(Tag.MERGE, MERGE, "<");
        //addImplicitResolver(Tag.NULL, NULL, "~nN\0");
        //addImplicitResolver(Tag.NULL, EMPTY, null);
        // addImplicitResolver(Tags.TIMESTAMP, TIMESTAMP, "0123456789");
        //addImplicitResolver(Tag.VALUE, VALUE, "=");
    }
}
