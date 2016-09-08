package com.tengdag.myframwork.ui.adapter.listener;

import android.view.View;

/**
 * Created by tengdag on 16/9/8 17:16.
 */
public interface OnItemClickListener<T> {
    void onClick(View view, T item);
}
