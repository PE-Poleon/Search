package com.poleon.search.searchdef.engine;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.poleon.search.NetdiskActivity;
import com.poleon.search.searchdef.ISearchAdapter;
import com.poleon.search.searchdef.OnSearchResultListener;
import com.poleon.search.searchdef.SearchResult;

@SuppressLint("JavascriptInterface") public class PanAdapter implements ISearchAdapter{
	
	private static final String TAG = PanAdapter.class.getSimpleName();
	  private int mGetCount;
	  private boolean mIsLoadFinished;
	  private String mKeyWord;
	  private boolean mNewSearch;
	  private OnSearchResultListener mOnSearchResultListener;
	  public int mRecordCount;
	  private WebView mWebView;

	  @SuppressLint("SetJavaScriptEnabled") public PanAdapter(WebView paramWebView)
	  {
	    this.mWebView = paramWebView;
	    this.mWebView.getSettings().setJavaScriptEnabled(true);
	    this.mWebView.setWebViewClient(new WebViewClient()
	    {
	    	
	      public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
	      {
	        if ((PanAdapter.this.mNewSearch) && (PanAdapter.this.mKeyWord != null) && (paramAnonymousString.equals("http://m.pansou.com/")))
	        {
	          Log.d(PanAdapter.TAG, "New Search!");
	          PanAdapter.this.setFormValueAndSearch(PanAdapter.this.mKeyWord);
	        }
	        if (paramAnonymousString.toLowerCase().contains("?q="))
	          paramAnonymousWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
	      }

	      public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
	      {
	        Log.e(PanAdapter.TAG, "ERROR: " + paramAnonymousString1);
	      }

	      public WebResourceResponse shouldInterceptRequest(WebView paramAnonymousWebView, String paramAnonymousString)
	      {
		    paramAnonymousString = paramAnonymousString.toLowerCase();
	        Log.d(PanAdapter.TAG, "url: " + paramAnonymousString);
	        if (!PanAdapter.this.isWhiteURL(paramAnonymousString))
	        {
	          Log.d(PanAdapter.TAG, "屏蔽广告url " + paramAnonymousString);
	          return new WebResourceResponse(null, null, null);
	        }
	        return super.shouldInterceptRequest(paramAnonymousWebView, paramAnonymousString);
	      }
	    });
	    this.mWebView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
	    this.mWebView.loadUrl("http://m.pansou.com/");
	  }

	  private boolean isWhiteURL(String paramString)
	  {
	    return paramString.contains("pansou.com");
	  }

	  private void setFormValueAndSearch(String paramString)
	  {
	    this.mWebView.loadUrl("javascript:(function(){ document.getElementsByName('q')[0].value = '" + paramString + "';})(); ");
	    this.mWebView.loadUrl("javascript:(function(){ document.getElementsByTagName('form')[0].submit();})();");
	    this.mIsLoadFinished = false;
	  }

	  public int getRecordCount()
	  {
	    return this.mRecordCount;
	  }

	  public boolean next()
	  {
		  return true;
	  }

	  private List<SearchResult> parseHtml(String paramString)
	  {
		    ArrayList<SearchResult> localArrayList = new ArrayList();
		    if ((paramString.contains("对不起")))
		      return localArrayList;
		    int i = paramString.indexOf("相关结果约") + "相关结果约".length();
		    int j = paramString.indexOf("个", i);
		    String str;
	      str = paramString.substring(i, j);
	      if ((i > "相关结果约".length()) && (j >= 0)){
	    	  if (str.length() > 0)
	    	  {
	    		  this.mRecordCount = Integer.parseInt(str);
	    		  Log.e("记录", "" + this.mRecordCount);
	    	  }
	      }
	    
	    i = paramString.indexOf("<h2>");
	    for(;i>=0;i=j){
	    	j = paramString.indexOf("<h2>", "<h2>".length() + i);
	    	if(j<0){
	    		str = paramString.substring("<h2>".length() + i);
		    	localArrayList.add(parseItemHtml(str));
	    		break;
	    	}
	    	str = paramString.substring("<h2>".length() + i, j);
	    	localArrayList.add(parseItemHtml(str));
	    }
	    
	    return localArrayList;
	  }

	  protected SearchResult parseItemHtml(String paramString)
	  {
	    SearchResult localSearchResult = new SearchResult();
	    int i = paramString.indexOf("<a href=\"") + "<a href=\"".length();
	    int j = paramString.indexOf("\"", i);
	    String str1 = paramString.substring(i, j).replaceAll("amp;", "");
	    i = paramString.indexOf("nofollow\">", j) + "nofollow\">".length();
	    j = paramString.indexOf("</a>", i);
	    String str2 = paramString.substring(i, j).replaceAll("<b>", "").replaceAll("</b>", "");
	    
	    String str = paramString.substring(j+"</a>".length(), paramString.indexOf("</div>"));
	    String str3;
	    if(str.indexOf("大小:")>0){
	    	i = str.indexOf("大小:") + "大小:".length();
	    	j = str.indexOf(" ", i);
	    	str3 = str.substring(i, j);
	    }else
	    	str3 = "-";
	    String str4;
	    if(str.indexOf("时间:")>0){
	    	i = str.indexOf("时间:") + "时间:".length();
	    	j = str.indexOf(" ", i);
	    	str4 = str.substring(i, j).replaceAll("\r|\n", "");
	    }else
	    	str4 = "-";
	    String str5;
	    if(str.indexOf("次数:")>0){
	    	i = str.indexOf("次数:") + "次数:".length();
	    	j = str.indexOf(".", i);
	    	str5 = str.substring(i, j);
	    }else
	    	str5 = "-";
	    localSearchResult.keyword = this.mKeyWord;
	    localSearchResult.name = str2;
	    localSearchResult.link = "http://m.pansou.com" + str1;
	    localSearchResult.recordingTime = str4;
	    localSearchResult.fileSize = str3;
	    localSearchResult.fileCount = str1;
	    localSearchResult.speed = str1;
	    localSearchResult.popularity = str5;
	    return localSearchResult;
	  }
	  
	  @Override
		public void search(String paramString, OnSearchResultListener paramOnSearchResultListener) {
			// TODO Auto-generated method stub
		    this.mKeyWord = paramString;
		    this.mOnSearchResultListener = paramOnSearchResultListener;
		    this.mGetCount = 0;
		    this.mRecordCount = 0;
		    this.mNewSearch = true;
		}
	  
	  class LoadListener
	  {
	    LoadListener()
	    {
	    }

	    @JavascriptInterface
	    public void processHTML(String paramString)
	    {
	      List<SearchResult> searchResult = parseHtml(paramString);
	      if (mOnSearchResultListener != null)
	       mOnSearchResultListener.onSuccess(searchResult);
	    }
	  }
}
