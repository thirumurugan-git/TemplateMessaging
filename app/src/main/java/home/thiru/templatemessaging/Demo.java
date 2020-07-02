package home.thiru.templatemessaging;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class Demo extends AppCompatActivity {
    WebView mWebView;

    public Demo() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_demo);
        this.mWebView = (WebView)this.findViewById(R.id.videoview);
        String videoStr = "<html><body style=\"margin:0px;padding:0px;background:black\"><h2 style=\"color:white;margin-top:5%\" align=\"center\">Demo video</h2><br><iframe style=\"max-height:100%;width:100%;margin-top:5%;\" src=\"https://www.youtube.com/embed/-QMxip4mlto\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        this.mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings ws = this.mWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        this.mWebView.loadData(videoStr, "text/html", "utf-8");
    }
}
