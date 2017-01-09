package com.ys.interfaces;

import android.content.Context;

import com.ys.mynearby.PlaceLocation;

/**
 * Created by ss on 021-21-12-2016.
 */

public interface IURLFetcher{
    String buildURL(Context context, PlaceLocation placeLocation, String keyword);
    void startFetchURLBackground(String url, WSFetchListener wsFetchListener);
    String getURLResponse(String url);
    void removeAllTask();
}