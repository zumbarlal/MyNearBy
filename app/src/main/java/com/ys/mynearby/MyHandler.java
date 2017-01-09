package com.ys.mynearby;

import android.os.Handler;
import android.os.Message;

import com.ys.interfaces.IMyHandlerListener;

/**
 * Created by ss on 021-21-12-2016.
 */

public class MyHandler extends Handler implements IMyHandlerListener.MyHandlerMethods {
    static MyHandler myHandler = new MyHandler();
    IMyHandlerListener iMyHandlerListener;

    private MyHandler(){

    }

    public static MyHandler getHandler(){
        return myHandler;
    }

    @Override
    public void setMyHandlerListener(IMyHandlerListener iMyHandlerListener) {
        this.iMyHandlerListener = iMyHandlerListener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1:
                iMyHandlerListener.onHandlerListener(msg.obj);
                break;
        }


    }
}
