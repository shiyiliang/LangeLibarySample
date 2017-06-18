package shiyiliang.me.langelibarysample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import shiyiliang.me.baselibary.base.DefaultBaseActivity;
import shiyiliang.me.baselibary.view.popupwindow.CommonPopupWindow;

public class PopupWindowActivity extends DefaultBaseActivity {
    @BindView(R.id.one)
    Button btnBelow;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_popup_window;
    }

    @Override
    protected void init() {
        getSupportActionBar().setTitle("PopupWindow");
    }

    @OnClick(R.id.one)
    void one() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.test, null);
        int[] position = new int[2];
        btnBelow.getLocationOnScreen(position);
        CommonPopupWindow window = new CommonPopupWindow.Builder(this)
                .setView(v)
                //这里的宽高，在代码里测量的采用的是upspecified的，所以测量的大小可能和设置的值不一样
//                .setWidthAndHeight(200, 200)
//                .setBackGroundLevel(f, true)
                .setOutsideTouchable(true)
                .build();
        //显示在屏幕的下面
//                .showAtLocation(findViewById(android.R.id.content), Gravity.START|Gravity.BOTTOM,0,0);
        int with = window.getWidth();
        int height = window.getHeight();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        System.out.println(position[0] + "-->" + position[1] + "-->" + with + "-->" + height);
        //上面显示
//        window.showAtLocation(btnBelow, Gravity.NO_GRAVITY, position[0], position[1]-height);
//        window.showAsDropDown(btnBelow);
        window.showAtLocation(btnBelow,Gravity.NO_GRAVITY,position[0]+btnBelow.getWidth(),position[1]-height+btnBelow.getHeight());
    }
}
