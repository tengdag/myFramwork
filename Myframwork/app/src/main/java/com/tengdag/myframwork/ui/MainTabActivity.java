package com.tengdag.myframwork.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.tengdag.myframwork.R;
import com.tengdag.myframwork.base.BaseActivity;
import com.tengdag.myframwork.base.BaseFragment;
import com.tengdag.myframwork.util.DevUtil;

import java.util.List;

public class MainTabActivity extends BaseActivity {

    private FragmentTabHost tabHost;
    private TabWidget tabwidget;
    private int currentTab;
    private List<BaseFragment> mBaseFragmentList;
    private List<String> labels;
    private static String TAG = MainTabActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //获得屏幕分辨率相关数据
        DevUtil.init(MainTabActivity.this);

        setContentView(R.layout.activity_main_tab);
        initView();
    }



    private void initView() {
        // 获取TabHost对象
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabwidget = tabHost.getTabWidget();

        for (int i=0;i<mBaseFragmentList.size();i++){
            tabHost.addTab(tabHost.newTabSpec(labels.get(i)).setIndicator(labels.get(i)),mBaseFragmentList.get(i).getClass(),null);
        }

        // 初始化标签
        initTabWidget();
        // 标签切换处理，用setOnTabChangedListener
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @SuppressLint("ResourceAsColor")

            public void onTabChanged(String tabId) {
                int index = labels.indexOf(tabId);
                initTabWidgetBg(index);
                setCurrentTabWithAnim(currentTab, index, tabId);
                currentTab = tabHost.getCurrentTab();

            }
        });
    }

    private void initTabWidgetBg(int index) {
        for (int i = 0; i < labels.size(); i++) {
            tabwidget.getChildAt(i).setBackgroundResource(android.R.color.white);
        }
        tabwidget.getChildAt(index).setBackgroundResource(android.R.color.darker_gray);
        tabwidget.getChildAt(index).setPressed(true);
    }

    private void initTabWidget() {
        for (int i = 0; i < labels.size(); i++) {
            View child = tabwidget.getChildAt(i);
            child.getLayoutParams().width = DevUtil.getWidth() / labels.size();
            final TextView tv = (TextView) child.findViewById(android.R.id.title);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0); //取消文字底边对齐
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE); //设置文字居中对齐
        }
        initTabWidgetBg(0);
        currentTab = 0;
    }


    private void setCurrentTabWithAnim(int now, int next, String tag) {
        //这个方法是关键，用来判断动画滑动的方向
        if (now > next) {
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
            tabHost.setCurrentTabByTag(tag);
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
        } else if (now < next) {
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
            tabHost.setCurrentTabByTag(tag);
            tabHost.getCurrentView().startAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
        }
    }

    private long exitTime = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;

        }
        return super.dispatchKeyEvent(event);
    }


}
