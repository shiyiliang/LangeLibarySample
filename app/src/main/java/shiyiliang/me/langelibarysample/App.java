package shiyiliang.me.langelibarysample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-07-05
 * Desc  :
 */

public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
