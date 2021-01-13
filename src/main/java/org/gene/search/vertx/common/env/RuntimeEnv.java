package org.gene.search.vertx.common.env;

/**
 * Utility interface used to query for runtime information
 */
public interface RuntimeEnv {

    static RuntimeEnv get() {
        return new EC2Environment();
    }

    /**
     * Returns true if the environment is local (dev)
     *
     * @return true if the running environment is dev
     */
    boolean dev();

    /**
     * Returns the application name
     *
     * @return
     */
    String application();

    /**
     * Returns application scope
     *
     * @return
     */
    String scope();

}