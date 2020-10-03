package smarthealthcard.com.smarthealthcard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PdfViewerActivity extends AppCompatActivity {
    WebView webview;
    String pdfStringUrl;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPdf();
        setContentView(webview);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void viewPdf() {
        pdfStringUrl = getIntent().getStringExtra("DOC_URL");
        webview = new WebView(PdfViewerActivity.this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webview.setWebViewClient(new Callback());
        try {
            url= URLEncoder.encode(pdfStringUrl,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url.toString().trim());

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }
}
