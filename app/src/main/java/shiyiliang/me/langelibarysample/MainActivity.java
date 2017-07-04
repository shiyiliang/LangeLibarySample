package shiyiliang.me.langelibarysample;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import shiyiliang.me.baselibary.base.DefaultBaseActivity;
import shiyiliang.me.baselibary.base.WebViewActivity;
import shiyiliang.me.baselibary.util.RxToast;
import shiyiliang.me.baselibary.view.titlebar.CustomTitleBar;
import shiyiliang.me.baselibary.view.textview.CustomTextView;
import shiyiliang.me.langelibarysample.news.NewMainActivity;

public class MainActivity extends DefaultBaseActivity {
    @BindView(R.id.two)
    Button two;
    @BindView(R.id.title)
    CustomTitleBar mCustomTitleBar;

    @BindView(R.id.ctvTestClick)
    CustomTextView customTextView;

    @OnClick(R.id.two)
    void two() {
        System.out.println(two.getText().toString());
        startActivity(new Intent(mContext, SwipLayoutActivity.class));
    }

    @OnClick(R.id.three)
    void three() {
        startActivity(new Intent(mContext, PopupWindowActivity.class));
    }

    @OnClick(R.id.four)
    void four() {
        startActivity(new Intent(mContext, RetrofitActivity.class));
    }

    @OnClick(R.id.news)
    void news() {
        startActivity(new Intent(mContext, WebViewActivity.class));
    }


    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }


    @Override
    protected void init() {
        initTitleBar();
    }

    private void initTitleBar() {
        customTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.toast(mContext, "onClick");
            }
        });
        customTextView.setDrawableLeftListener(new CustomTextView.DrawableLeftListener() {
            @Override
            public void onDrawableLeftClick(View view) {
                RxToast.toast(mContext, "onDrawableLeftClick");
            }
        });
//        customTextView.setDrawableRightListener(new CustomTextView.DrawableRightListener() {
//            @Override
//            public void onDrawableRightClick(View view) {
//                RxToast.toast(mContext, "onDrawableRightClick");
//            }
//        });

        mCustomTitleBar.setTitleClickListener(new CustomTitleBar.TitleClickListener() {
            @Override
            public void onLeftClick() {
                RxToast.toast(mContext, "onleftclick");
            }

            @Override
            public void onLeftButton1Click() {
                RxToast.toast(mContext, "onLeftButton1Click");
            }

            @Override
            public void onLeftButton2Click() {
                RxToast.toast(mContext, "onLeftButton2Click");

            }

            @Override
            public void onRightClick() {
                RxToast.toast(mContext, "onRightClick");
            }

            @Override
            public void onRightButton1Click() {
                RxToast.toast(mContext, "onRightButton1Click");
            }

            @Override
            public void onRightButton2Click() {
                RxToast.toast(mContext, "onRightButton2Click");
            }
        });
    }

    @Override
    protected void networkDisconnected() {
        System.out.println("没有连接");
        Toast.makeText(this, "连接不上", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void networkConnected() {
        System.out.println("连接上了");
        Toast.makeText(this, "连接上了", Toast.LENGTH_SHORT).show();
    }

}
