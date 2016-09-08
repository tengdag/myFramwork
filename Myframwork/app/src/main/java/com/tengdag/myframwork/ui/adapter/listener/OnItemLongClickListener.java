package com.tengdag.myframwork.ui.adapter.listener;

import android.view.View;

/**
 * Created by tengdag on 16/9/8 17:17.
 */
public interface OnItemLongClickListener<T> {
    void onLongClick(View view, T item);
}
