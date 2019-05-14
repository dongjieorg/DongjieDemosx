package com.dongjie.dongjiedemosx.rxbinding;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.dongjie.dongjiedemosx.R;
import com.dongjie.dongjiedemosx.base.BaseActivity;
import com.dongjie.dongjiedemosx.tools.LogUtils;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

/**
 * 参考文章： https://www.jianshu.com/p/1ad3633ef0b4
 *
 * 依赖RxBinding的库之后就不用依赖RxJava的库， 因为已经包含了
 * RxBinding3.0以后开始支持androidx
 */
public class RxBindingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_binding);
        Button bt = findViewById(R.id.bt);

        // button设置点击事件，并且两秒内只能点一次
        RxView.clicks(bt).throttleFirst(2, TimeUnit.SECONDS).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        LogUtils.showLog("aaaaaaaaaaaaaaaaaaaaaaa");
                    }
                });

        // 给按钮设置长压事件
        RxView.longClicks(bt)
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) throws Exception {
                        Log.d("dongjiejie", "1111111111111111");
                    }
                });
    }

    /**
     设置View的显示隐藏
     try {
        RxView.visibility(bt).accept(visibility);  accept里传true显示，传false隐藏
     }
     catch (Exception e) {

     }
     */
}
