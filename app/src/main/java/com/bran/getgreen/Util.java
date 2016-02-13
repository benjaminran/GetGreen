package com.bran.getgreen;

/**
 * Created by benjaminran on 12/19/15.
 */
public class Util {

    /**
     * Checks that a condition is true and throws an exception otherwise in debug builds. In release
     * builds this code will be optimized away by the compiler.
     * @param condition If this is false, e is thrown
     * @param e The exception to be thrown
     */
    public static void debugCheck(boolean condition, RuntimeException e){
        if(BuildConfig.DEBUG && !condition) throw e;
    }
}
