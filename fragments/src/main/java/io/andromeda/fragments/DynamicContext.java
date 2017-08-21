package io.andromeda.fragments;

import java.util.Map;

public interface DynamicContext {
    Map<String, Object> getContext(Map<String, Object> previousContext);
}
