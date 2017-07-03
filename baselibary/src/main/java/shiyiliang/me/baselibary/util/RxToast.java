package shiyiliang.me.baselibary.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/3
 * Desc  :
 */

public class RxToast {
    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
