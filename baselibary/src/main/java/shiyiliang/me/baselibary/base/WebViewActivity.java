package shiyiliang.me.baselibary.base;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import shiyiliang.me.baselibary.R;
import shiyiliang.me.baselibary.R2;
import shiyiliang.me.baselibary.view.LangeWebView;
import shiyiliang.me.baselibary.view.titlebar.CustomTitleBar;

public class WebViewActivity extends DefaultBaseActivity {
    //在库中，必须使用R2，同时在gralbe中配置一下
    @BindView(R2.id.ctb_title_bar)
    CustomTitleBar ctbTitleBar;
    @BindView(R2.id.ll_web)
    LinearLayout llWeb;

    WebView wvChrome;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_web_view;
    }


    @Override
    protected void init() {
        addWebView();
        initWebView();
    }

    /**
     * 这里为什么这么调用？是为了内存泄露
     */
    private void addWebView() {
        wvChrome = new LangeWebView(mContext.getApplicationContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wvChrome.setLayoutParams(params);

        llWeb.addView(wvChrome);
    }

    private void initWebView() {
        initWebViewSetting();
        wvChrome.setWebChromeClient(new DefaultWebChromeClient());
        wvChrome.setWebViewClient(new DefaultWebViewClient());

        wvChrome.loadUrl("http://www.baidu.com");
    }

    private void initWebViewSetting() {
        WebSettings webSettings = wvChrome.getSettings();

        wvChrome.requestFocusFromTouch();///支持获取手势焦点，输入用户名、密码或其他


        webSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8

        webSettings.setSupportZoom(true);  //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
//        webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100


        //设置缓存
    }

    @Override
    protected void onResume() {
        super.onResume();
        //// TODO: 2017/7/4 这里为什么
        wvChrome.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        wvChrome.getSettings().setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wvChrome != null) {
            ViewParent parent = wvChrome.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(wvChrome);
            }

            wvChrome.stopLoading();
            clearHistory();
            wvChrome.loadUrl("about:blank");
            wvChrome.removeAllViews();

            wvChrome.destroy();
        }
    }

    protected void clearHistory() {
        wvChrome.clearHistory();
    }

    /**
     * 清除一个表单数据
     */
    protected void clearFormData() {
        wvChrome.clearFormData();
    }

    /**
     * 清除缓存，true包括RAM的缓存，也包括硬盘上的缓存
     */
    protected void clearCache() {
        wvChrome.clearCache(true);
    }

    /**
     * 点击返回
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvChrome.canGoBack()) {
            wvChrome.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //显示加载页面
    protected void showDialog() {

    }

    //关闭加载页面
    protected void closeDialog() {

    }

    /**
     * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
     */
    class DefaultWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {

        }
    }

    /**
     * WebViewClient就是帮助WebView处理各种通知、请求事件的
     */
    class DefaultWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //显示开始加载页面

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //加载结束页面

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        //webView默认是不处理https请求的，页面显示空白
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }
}
