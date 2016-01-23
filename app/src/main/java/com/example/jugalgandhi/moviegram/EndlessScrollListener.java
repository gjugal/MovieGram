package com.example.jugalgandhi.moviegram;

import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by jugal gandhi on 1/23/2016.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    int visibleThreshold = -1;
    MovieDataFetchAndDisplay mdFetch = null;
    int previousTotal = -1;

    public EndlessScrollListener() {

    }

    public EndlessScrollListener(int visibleThreshold, MovieDataFetchAndDisplay value) {
        this.visibleThreshold = visibleThreshold;
        this.mdFetch = value;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if(visibleItemCount <= visibleThreshold)
        {
            visibleThreshold = visibleItemCount;
        }
        if ( mdFetch.getLoading()) {
            if (totalItemCount > previousTotal) {
                mdFetch.setLoading(false);
                previousTotal = totalItemCount;
            }
        }
        Log.d("SCROLL_DATA","totalItemCount - visibleItemCount : " + (totalItemCount - visibleItemCount) + " firstVisibleItem: " + firstVisibleItem);
        if ((!mdFetch.getLoading()) && ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) && ((totalItemCount - visibleItemCount) != 0)) {
            mdFetch.startLoadingData();
            mdFetch.setLoading(true);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
