package com.dongjie.dongjiedemosx;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongjie.dongjiedemosx.base.BaseActivity;
import com.dongjie.dongjiedemosx.lottie.LottieActivity;
import com.dongjie.dongjiedemosx.rxbinding.RxBindingActivity;

import java.util.LinkedList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycleview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinkedList<ClassBean> list = new LinkedList<>();
        ClassBean classBean;

        classBean = new ClassBean();
        classBean.setTitle("lottie动画");
        classBean.setClassName(LottieActivity.class);
        list.add(classBean);

        classBean = new ClassBean();
        classBean.setTitle("RxBinding");
        classBean.setClassName(RxBindingActivity.class);
        list.add(classBean);

        MyAdapter myAdapter = new MyAdapter(this, list);
        mRecyclerView.setAdapter(myAdapter);
    }

    class MyAdapter extends RecyclerView.Adapter {
        private Context context;
        private View view;
        private LinkedList<ClassBean> list;

        MyAdapter(Context context, LinkedList<ClassBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_demo_show_item_item, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ClassBean myBean = list.get(position);
            ((MyHolder) holder).myTextView.setText(myBean.getTitle());
            ((MyHolder) holder).myRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent mIntent = new Intent();
                    mIntent.setClass(context, myBean.getClassName());
                    startActivity(mIntent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (list == null) {
                return 0;
            } else {
                return list.size();
            }
        }

        class MyHolder extends RecyclerView.ViewHolder {
            private TextView myTextView;
            private RelativeLayout myRel;

            MyHolder(View itemView) {
                super(itemView);
                myRel = view.findViewById(R.id.desk_adapter_rel);
                myTextView = view.findViewById(R.id.desk_adapter_txt_id);
            }
        }
    }

    public class ClassBean<T> {
        private String title;
        private Class<T> className;

        String getTitle() {
            return title;
        }

        void setTitle(String title) {
            this.title = title;
        }

        Class<T> getClassName() {
            return className;
        }

        void setClassName(Class<T> className) {
            this.className = className;
        }
    }
}
