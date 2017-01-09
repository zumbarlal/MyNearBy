package com.ys.mynearby;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by ss on 014-14-12-2016.
 */

public class MainClassApplication extends Application {
    @Override
    public void onCreate() {
        MultiDex.install(this);
    }
}
