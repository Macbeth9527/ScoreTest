package com.example.android.scoretest.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.android.scoretest.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{


        /** 是否沉浸状态栏 **/
        private boolean isSetStatusBar = true;
        /** 是否允许全屏 **/
        private boolean mAllowFullScreen = true;
        /** 是否禁止旋转屏幕 **/
        private boolean isAllowScreenRoate = false;
        /** 当前Activity渲染的视图View **/
        private View mContextView = null;
        /** 日志输出标志 **/
        protected final String TAG = this.getClass().getSimpleName();

        /** View点击 **/
        public abstract void widgetClick(View v);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d(TAG, "BaseActivity-->onCreate()");
            Bundle bundle = getIntent().getExtras();
            initParms(bundle);
            View mView = bindView();
            if (null == mView) {
                mContextView = LayoutInflater.from(this)
                        .inflate(bindLayout(), null);
            } else
                mContextView = mView;
            if (mAllowFullScreen) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            if (isSetStatusBar) {
                steepStatusBar();
            }
            setContentView(mContextView);
            if (!isAllowScreenRoate) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            initView(mContextView);
            setListener();
            doBusiness(this);
        }

        /**
         * [沉浸状态栏]
         */

        private void steepStatusBar() {

            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Translucent status bar
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                    // 解决4.4-5.0版本之间，页面包含EditText无法适配的问题
                    {
                        // create our manager instance after the content view is set
                        SystemBarTintManager mTintManager = new SystemBarTintManager(this);

                        // enable status bar tint
                        mTintManager.setStatusBarTintEnabled(true);
                        // enable navigation bar tint
                        mTintManager.setNavigationBarTintEnabled(true);

                        // 自定义状态栏的颜色
                        mTintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimary));
                    }
                }
            }

            // 解决4.4-5.0版本之间，页面包含EditText无法适配的问题
       /* {
            // create our manager instance after the content view is set
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);

            // enable status bar tint
            mTintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
            mTintManager.setNavigationBarTintEnabled(true);

            // 自定义状态栏的颜色
            mTintManager.setStatusBarTintColor(getResources().getColor(R.color.colorPrimary));
        }*/

            // 解决[5.0-5.1.1]版本状态栏没有全透明的系统适配问题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 解决部分5.x系统使用状态栏透明属性后状态栏变黑色，不使用这句代码，在6.0设备上又出现半透明状态栏
                // 需要特殊处理
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }

            // 把状态栏标记为浅色，然后状态栏的字体颜色自动转换为深色。
            /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
             }*/


        }

        /**
         * [初始化参数]
         *
         * @param parms
         */
        public abstract void initParms(Bundle parms);

        /**
         * [绑定视图]
         *
         * @return
         */
        public abstract View bindView();

        /**
         * [绑定布局]
         *
         * @return
         */
        public abstract int bindLayout();

        /**
         * [初始化控件]
         *
         * @param view
         */
        public abstract void initView(final View view);

        /**
         * [绑定控件]
         *
         * @param resId
         *
         * @return
         */
        protected    <T extends View> T $(int resId) {
            return (T) super.findViewById(resId);
        }

        /**
         * [设置监听]
         */
        public abstract void setListener();

        @Override
        public void onClick(View v) {
            widgetClick(v);
        }

        /**
         * [业务操作]
         *
         * @param mContext
         */
        public abstract void doBusiness(Context mContext);



        /**
         * [页面跳转]
         *
         * @param clz
         */
        public void startActivity(Class<?> clz) {
            startActivity(new Intent(BaseActivity.this,clz));
        }

        /**
         * [携带数据的页面跳转]
         *
         * @param clz
         * @param bundle
         */
        public void startActivity(Class<?> clz, Bundle bundle) {
            Intent intent = new Intent();
            intent.setClass(this, clz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivity(intent);
        }

        /**
         * [含有Bundle通过Class打开编辑界面]
         *
         * @param cls
         * @param bundle
         * @param requestCode
         */
        public void startActivityForResult(Class<?> cls, Bundle bundle,
                                           int requestCode) {
            Intent intent = new Intent();
            intent.setClass(this, cls);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            startActivityForResult(intent, requestCode);
        }

        @Override
        protected void onRestart() {
            super.onRestart();
            Log.d(TAG, "onRestart()");
        }

        @Override
        protected void onStart() {
            super.onStart();
            Log.d(TAG, "onStart()");
        }

        @Override
        protected void onResume() {
            super.onResume();
            Log.d(TAG, "onResume()");
        }

        @Override
        protected void onPause() {
            super.onPause();
            Log.d(TAG, "onPause()");
        }

        @Override
        protected void onStop() {
            super.onStop();
            Log.d(TAG, "onStop()");
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            Log.d(TAG, "onDestroy()");
        }

        /**
         * [简化Toast]
         * @param msg
         */
        protected void showToast(String msg){
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        }

        /**
         * [是否允许全屏]
         *
         * @param allowFullScreen
         */
        public void setAllowFullScreen(boolean allowFullScreen) {
            this.mAllowFullScreen = allowFullScreen;
        }

        /**
         * [是否设置沉浸状态栏]
         *
         * @param isSetStatusBar
         */
        public void setSteepStatusBar(boolean isSetStatusBar) {
            this.isSetStatusBar = isSetStatusBar;
        }

        /**
         * [是否允许屏幕旋转]
         *
         * @param isAllowScreenRoate
         */
        public void setScreenRoate(boolean isAllowScreenRoate) {
            this.isAllowScreenRoate = isAllowScreenRoate;
        }

    }

