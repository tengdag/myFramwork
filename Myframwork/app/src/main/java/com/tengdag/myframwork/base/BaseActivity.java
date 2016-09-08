package com.tengdag.myframwork.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.tengdag.myframwork.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected Dialog mProgressDialog;

    protected void showProgress(String title) {
        showProgress(title, getString(R.string.messageLoading));
    }

    protected void showWaitingProgress() {
        showProgress(getString(R.string.titleLoading));
    }

    protected void showProgress(String title, String message) {
        if (isFinishing()) {
            return;
        }
        if (null != mProgressDialog) {
            closeProgress();
        }
        mProgressDialog = ProgressDialog.show(this, title, message);
    }

    protected void closeProgress() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
