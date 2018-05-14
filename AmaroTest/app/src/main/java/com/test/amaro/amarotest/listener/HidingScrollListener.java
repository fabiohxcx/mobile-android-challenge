package com.test.amaro.amarotest.listener;

/**
 * Created by fabio.lagoa on 23/08/2017.
 */

import android.support.v7.widget.RecyclerView;

/*
 * This class is a ScrollListener for RecyclerView that allows to show/hide
 * views when list is scrolled. It assumes that you have added a header
 * to your list. @see pl.michalz.hideonscrollexample.adapter.partone.RecyclerAdapter
 * */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }

    public abstract void onHide();

    public abstract void onShow();

}