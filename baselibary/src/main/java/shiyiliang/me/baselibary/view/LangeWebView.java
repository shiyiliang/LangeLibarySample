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
