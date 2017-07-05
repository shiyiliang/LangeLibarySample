package shiyiliang.me.baselibary.view.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import shiyiliang.me.baselibary.util.AppUtil;
import shiyiliang.me.baselibary.util.RxToast;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/4
 * Desc  : webview的封装
 * <p>
 * <p>
 * 实现的功能有几个：
 * 1. 能够进度条加载进度
 * 2. 能够弹出框加载进度
 * 3. 能够点击图片，放大，观看
 * 4. 能够复制和索索
 * 5. 能够点击链接
 * 6. 和js的交互问题
 * </p>
 */

public class ProgressWebView extends WebView {
    private static final int UPDATE_PROGRESS = 0x1;
    private static final int LOAD_COMPLETE = UPDATE_PROGRESS + 1;

    private ProgressBar progressBar;
    private CoustomHandler handler;

    public ProgressWebView(Context context) {
        super(context);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //add progressbar
        addProgressBar();
        handler = new CoustomHandler(getContext(), progressBar);

        initSetting();
        setWebViewClient(new DefaultWebViewClient());
        setWebChromeClient(new DefaultWebChromeClient());
    }

    //添加进度条
    private void addProgressBar() {
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8);
        progressBar.setLayoutParams(layoutParams);
        addView(progressBar);
    }


    private void initSetting() {

    }

    public void setProgressDrawable(Drawable drawable) {
        AppUtil.checkNull(drawable);
        AppUtil.checkNull(progressBar);

        progressBar.setProgressDrawable(drawable);
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }


    /**
     * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
     */
    class DefaultWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                packageMessage(LOAD_COMPLETE, 100);
            } else {
                packageMessage(UPDATE_PROGRESS, newProgress);
            }
            Log.i("webview", "进度->" + newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {

        }

        private void packageMessage(int what, int value) {
            Message message = handler.obtainMessage();
            message.what = what;
            message.obj = value;

            handler.sendMessage(message);
        }
    }

    class CoustomHandler extends Handler {
        private final ProgressBar bar;
        private WeakReference<Context> wrfContext;

        public CoustomHandler(Context context, ProgressBar bar) {
            super(Looper.getMainLooper());
            wrfContext = new WeakReference<Context>(context);
            this.bar = bar;
        }

        @Override
        public void handleMessage(Message msg) {
            int code = msg.what;
            if (code == UPDATE_PROGRESS) {
                bar.setVisibility(VISIBLE);
                bar.setProgress((Integer) msg.obj);
            } else if (code == LOAD_COMPLETE) {
                bar.setVisibility(GONE);
            }
        }
    }

    /**
     * WebViewClient就是帮助WebView处理各种通知、请求事件的
     */
    class DefaultWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.i("webview", url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //显示开始加载页面
            System.out.println("开始了");
            Log.i("webview", "开始了");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //加载结束页面
            System.out.println("结束了");
            Log.i("webview", "结束了");
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
