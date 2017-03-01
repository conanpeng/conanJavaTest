package com.conan.javaTest.netty;

/**
 * @author huangjinsheng on 2017/2/14.
 */
public interface Listener {
    void onSuccess(Object... args);

    void onFailure(Throwable cause);
}
