package com.techfoot.stockspree.InboundAdaptors.Configurations;


public class WorkspaceContext {

    private static ThreadLocal<String> currentWorkspace = new ThreadLocal<>();
    public static void setCurrentWorkspace(String workspaceId) {
        currentWorkspace.set(workspaceId);
    }

    public static String getCurrentWorkspace() {
        return currentWorkspace.get();
    }

    public static String getCurrentDatabase() {
        if(currentWorkspace.get()!=null){
            return "techfoot_stockspree_db_" + currentWorkspace.get();
        } else {
            return "techfoot_stockspree_db_test";
        }
    }

    public static void clear() {
        currentWorkspace.remove();
    }
}
