package shiyiliang.me.langelibarysample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Network;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;
import shiyiliang.me.baselibary.base.BaseActivity;
import shiyiliang.me.baselibary.util.NetworkUtil;

public class MainActivity extends BaseActivity {

    boolean flag = false;

    @OnClick(R.id.one)
    void one() {
        System.out.println("onclick");
        System.out.println(NetworkUtil.isNetworkAvailable(this));
        System.out.println(NetworkUtil.isNetworkConnected(this));
        System.out.println(NetworkUtil.getDataEnabled(this));
        flag = !flag;
        System.out.println(NetworkUtil.getWifiEnabled(this));
        System.out.println(NetworkUtil.getNetworkType(this).name());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
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
