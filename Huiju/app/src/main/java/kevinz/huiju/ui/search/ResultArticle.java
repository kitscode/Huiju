package kevinz.huiju.ui.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kevinz.huiju.R;

/**
 * Created by Administrator on 2017/4/18.
 */

public class ResultArticle extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_web);
        String url=getIntent().getStringExtra("url");
        WebView webview=(WebView)findViewById(R.id.web);
        webview.loadUrl("http://www.guokr.com"+url);
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress==100){
                    //加载完成
                }else{
                    //加载中
                }

            }
        });
    }
}
