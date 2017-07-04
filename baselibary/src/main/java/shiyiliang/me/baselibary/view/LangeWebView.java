package shiyiliang.me.baselibary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/4
 * Desc  : webview的封装
 *
 * <p>
 *    实现的功能有几个：
 *    1. 能够进度条加载进度
 *    2. 能够弹出框加载进度
 *    3. 能够点击图片，放大，观看
 *    4. 能够复制和索索
 *    5. 能够点击链接
 *    6. 和js的交互问题
 * </p>
 */

public class LangeWebView extends WebView {
    private WebSettings settings;

    public LangeWebView(Context context) {
        this(context, null);
    }

    public LangeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LangeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initSetting();
    }

    private void initSetting() {
        settings = this.getSettings();
    }


}
