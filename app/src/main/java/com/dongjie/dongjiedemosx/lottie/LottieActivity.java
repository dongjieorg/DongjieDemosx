package com.dongjie.dongjiedemosx.lottie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.LottieTask;
import com.dongjie.dongjiedemosx.R;
import com.dongjie.dongjiedemosx.base.BaseActivity;
import com.dongjie.dongjiedemosx.tools.ThreadUtils;
import com.dongjie.dongjiedemosx.tools.ToastUtils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * GitHub地址：https://github.com/airbnb/lottie-android
 * 主要是加载json动画
 * 依赖implementation 'com.airbnb.android:lottie:3.0.0'之后会报错， 在Application标签下加下面两句即可：
 *   tools:replace="android:appComponentFactory"
 *   android:appComponentFactory=""
 *   2.8（包括2.8）以上版本只支持androidx
 */
public class LottieActivity extends BaseActivity {
    LottieAnimationView lottieAnimationView2 = null;
    LottieAnimationView lottieAnimationView3 = null;
    LottieAnimationView lottieAnimationView4 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        lottieAnimationView2 = findViewById(R.id.animation_view2);
        lottieAnimationView3 = findViewById(R.id.animation_view3);
        lottieAnimationView4 = findViewById(R.id.animation_view4);

        // 直接读取assets下的json文件
        lottieAnimationView2.setAnimation("logo/LogoSmall.json");
        lottieAnimationView2.setRepeatCount(-1);
        lottieAnimationView2.playAnimation();

        // 先申请sdcard权限， 申请到以后从sdcard上读取数据
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        if (data.size() == 2) {
                            // 从sdcard上读取并显示动画， cacheKey自己起
                            lottieAnimationView3.setAnimationFromJson(getAnimFromSDCard(), "anim");
                            lottieAnimationView3.setRepeatCount(-1); // -1为无限循环
                            lottieAnimationView3.playAnimation();

                            // 从网络上加载动画
                            ThreadUtils.newThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        String jsonString = getAnimFromSDCard(); // 请求回来的动画json, 这里用从sdcard上读取来代替
                                        LottieTask<LottieComposition> lottieTask = LottieCompositionFactory.fromJsonString(jsonString, "");
                                        lottieTask.addListener(new LottieListener<LottieComposition>() {
                                            @Override
                                            public void onResult(LottieComposition result) {
                                                lottieAnimationView4.setComposition(result);
                                                lottieAnimationView4.playAnimation();
                                            }
                                        });

                                    }catch (Exception e) {

                                    }
                                }
                            });
                        }
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);
                    }
                })
                .start();
    }

    private String getAnimFromSDCard() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/mnt/sdcard/" + "HamburgerArrow.json")));
            String content = null;
            while ((content = bufferedReader.readLine()) != null){
                stringBuilder.append(content);
            }
            return stringBuilder.toString();
        }
        catch (Exception e) {
            Log.d("dongjiejie", e.toString());
        }
        return null;
    }
}
