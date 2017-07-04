package shiyiliang.me.baselibary.base;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import shiyiliang.me.baselibary.R;
import shiyiliang.me.baselibary.R2;
import shiyiliang.me.baselibary.view.titlebar.CustomTitleBar;

public class WebViewActivity extends DefaultBaseActivity {
    //在库中，必须使用R2，同时在gralbe中配置一下
    @BindView(R2.id.ctb_title_bar)
    CustomTitleBar ctbTitleBar;
    @BindView(R2.id.wv_chrome)
    WebView wvChrome;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web_view;
    }


    @Override
    protected void init() {
        initWebView();
    }

    private void initWebView() {
        wvChrome.setWebChromeClient(new DefaultWebChromeClient());
        wvChrome.setWebViewClient(new DefaultWebViewClient());
    }

    /**
     *
     */
    class DefaultWebChromeClient extends WebChromeClient{

    }

    /**
     * 处理页面加载过程中的控制
     */
    class DefaultWebViewClient extends WebViewClient{

    }
}
