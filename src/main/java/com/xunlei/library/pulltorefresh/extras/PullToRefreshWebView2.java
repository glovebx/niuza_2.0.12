package com.xunlei.library.pulltorefresh.extras;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshWebView;
import java.util.concurrent.atomic.AtomicBoolean;

public class PullToRefreshWebView2 extends PullToRefreshWebView {
    static final String DEF_JS_READY_PULL_DOWN_CALL = "javascript:isReadyForPullDown();";
    static final String DEF_JS_READY_PULL_UP_CALL = "javascript:isReadyForPullUp();";
    static final String JS_INTERFACE_PKG = "ptr";
    private final AtomicBoolean mIsReadyForPullDown = new AtomicBoolean(false);
    private final AtomicBoolean mIsReadyForPullUp = new AtomicBoolean(false);
    private JsValueCallback mJsCallback;

    final class JsValueCallback {
        JsValueCallback() {
        }

        public void isReadyForPullUpResponse(boolean response) {
            PullToRefreshWebView2.this.mIsReadyForPullUp.set(response);
        }

        public void isReadyForPullDownResponse(boolean response) {
            PullToRefreshWebView2.this.mIsReadyForPullDown.set(response);
        }
    }

    public PullToRefreshWebView2(Context context) {
        super(context);
    }

    public PullToRefreshWebView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshWebView2(Context context, Mode mode) {
        super(context, mode);
    }

    protected WebView createRefreshableView(Context context, AttributeSet attrs) {
        return super.createRefreshableView(context, attrs);
    }

    protected boolean isReadyForPullStart() {
        ((WebView) getRefreshableView()).loadUrl(DEF_JS_READY_PULL_DOWN_CALL);
        return this.mIsReadyForPullDown.get();
    }

    protected boolean isReadyForPullEnd() {
        ((WebView) getRefreshableView()).loadUrl(DEF_JS_READY_PULL_UP_CALL);
        return this.mIsReadyForPullUp.get();
    }
}
