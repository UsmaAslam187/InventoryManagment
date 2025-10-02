package com.techfoot.stockspree.InboundAdaptors.Configurations;

public class RequestContext {
    private static final ThreadLocal<CustomHttpRequestWrapper> currentRequest = new ThreadLocal<>();
    private static final ThreadLocal<String> currentAction = new ThreadLocal<>();
    private static final ThreadLocal<String> currentMethod = new ThreadLocal<>();

    public static void setCurrentRequest(CustomHttpRequestWrapper request) {
        currentRequest.set(request);
    }

    public static CustomHttpRequestWrapper getCurrentRequest() {
        return currentRequest.get();
    }

    public static void setCurrentAction(String action) {
        currentAction.set(action);
    }

    public static String getCurrentAction() {
        return currentAction.get();
    }

    public static void clear() {
        currentRequest.remove();
        currentAction.remove();
        currentMethod.remove();
    }

    public static void setCurrentMethod(String method) {
        currentMethod.set(method);
    }

    public static String getCurrentMethod() {
        return currentMethod.get();
    }
}