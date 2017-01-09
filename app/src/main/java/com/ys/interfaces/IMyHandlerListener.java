package com.ys.interfaces;

/**
 * Created by ss on 021-21-12-2016.
 */

public interface IMyHandlerListener {
    void onHandlerListener(Object obj);

    interface MyHandlerMethods{
        void setMyHandlerListener(IMyHandlerListener iMyHandlerListener);
    }
}
