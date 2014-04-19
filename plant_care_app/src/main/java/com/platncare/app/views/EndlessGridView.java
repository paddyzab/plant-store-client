package com.platncare.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.platncare.app.fragments.listeners.EndlessListener;

import java.util.List;

public class EndlessGridView extends GridView implements AbsListView.OnScrollListener {

    private boolean isLoading;
    private EndlessListener listener;
    private ArrayAdapter adapter;

    public EndlessGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EndlessGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EndlessGridView(Context context) {
        this(context, null);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null) {
            return;
        }

        if (getAdapter().getCount() == 0) {
            return;
        }

        int l = visibleItemCount + firstVisibleItem;
        if (l >= totalItemCount - visibleItemCount && !isLoading) {
            if (listener != null && listener.hasMore()) {
                isLoading = true;
                listener.loadData();
            }
        }
    }

    public void setAdapter(ArrayAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    public void setListener(EndlessListener listener) {
        this.listener = listener;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void addNewData(List data) {
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }
}
