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

import com.poleon.search.searchdef.ISearchAdapter;
import com.poleon.search.searchdef.OnSearchResultListener;
import com.poleon.search.searchdef.SearchResult;

@SuppressLint("JavascriptInterface") public class BtKittyAdapter implements ISearchAdapter{
	
	private static final String TAG = BtKittyAdapter.class.getSimpleName();
	  private static final String WEBURL = "http://btkitty.bid/";
	  private int mGetCount;
	  private boolean mIsLoadFinished;
	  private String mKeyWord;
	  private boolean mNewSearch;
	  private OnSearchResultListener mOnSearchResultListener;
	  public int mRecordCount;
	  private WebView mWebView;

	  @SuppressLint("SetJavaScriptEnabled") public BtKittyAdapter(WebView paramWebView)
	  {
	    this.mWebView = paramWebView;
	    this.mWebView.getSettings().setJavaScriptEnabled(true);
	    this.mWebView.setWebViewClient(new WebViewClient()
	    {
	    	
	      public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
	      {
	        if ((BtKittyAdapter.this.mNewSearch) && (BtKittyAdapter.this.mKeyWord != null) && (paramAnonymousString.equals("http://btkitty.bid/")))
	        {
	          Log.d(BtKittyAdapter.TAG, "New Search!");
	          BtKittyAdapter.this.setFormValueAndSearch(BtKittyAdapter.this.mKeyWord);//打开网站首页时，将关键字填入搜索框并搜索
	        }
	        if (paramAnonymousString.toLowerCase().contains("search"))//在搜索结果页面时，解析源码
	          paramAnonymousWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
	      }

	      //接收错误
	      public void onReceivedError(WebView paramAnonymousWebView, int paramAnonymousInt, String paramAnonymousString1, String paramAnonymousString2)
	      {
	        Log.e(BtKittyAdapter.TAG, "ERROR: " + paramAnonymousString1);
	      }

	      //拦截其他网页
	      public WebResourceResponse shouldInterceptRequest(WebView paramAnonymousWebView, String paramAnonymousString)
	      {
		    paramAnonymousString = paramAnonymousString.toLowerCase();
	        Log.d(BtKittyAdapter.TAG, "url: " + paramAnonymousString);
	        if (!BtKittyAdapter.this.isWhiteURL(paramAnonymousString))
	        {
	          Log.d(BtKittyAdapter.TAG, "屏蔽广告url " + paramAnonymousString);
	          return new WebResourceResponse(null, null, null);
	        }
	        return super.shouldInterceptRequest(paramAnonymousWebView, paramAnonymousString);
	      }
	    });
	    this.mWebView.addJavascriptInterface(new LoadListener(), "HTMLOUT");
	    this.mWebView.loadUrl("http://btkitty.bid/");
	  }

	  private boolean isWhiteURL(String paramString)
	  {
	    return paramString.contains("btkitty.bid");
	  }

	  private void setFormValueAndSearch(String paramString)//填充关键字并提交表单
	  {
	    this.mWebView.loadUrl("javascript:(function(){ document.getElementsByName('keyword')[0].value = '" + paramString + "';})(); ");
	    this.mWebView.loadUrl("javascript:(function(){ document.getElementsByTagName('form')[0].submit();})();");
	    this.mIsLoadFinished = false;
	  }

	  public int getRecordCount()
	  {
	    return this.mRecordCount;
	  }

	  public boolean next()//下一页
	  {
	    boolean bool2 = false;
	    int i = 0;
	    boolean bool1 = bool2;
	    if (this.mWebView.getUrl().toLowerCase().contains("search"))//在搜索结果页面
	    {
	      Log.d(TAG, "mGetCount " + this.mGetCount);
	      bool1 = bool2;
	      if (this.mGetCount < this.mRecordCount)
	      {
	        String[] arrayOfString = this.mWebView.getUrl().split("/");
	        String str2 = arrayOfString[(arrayOfString.length - 3)];
	        int j = Integer.parseInt(str2);
	        arrayOfString[(arrayOfString.length - 3)] = String.valueOf(j + 1);
	        Object localObject = "";
	        j = arrayOfString.length;
	        while (i < j)
	        {
	          String str3 = arrayOfString[i];
	          String str1 = (String)localObject + str3;
	          localObject = str1;
	          if (str3 != arrayOfString[(arrayOfString.length - 1)])
	            localObject = str1 + "/";
	          i += 1;
	        }
	        this.mWebView.loadUrl((String)localObject);
	        bool1 = true;
	      }
	    }
	    return bool1;
	  }

	  private List<SearchResult> parseHtml(String paramString)//解析网页源码
	  {
	    ArrayList<SearchResult> localArrayList = new ArrayList();
	    if ((paramString.contains("抱歉，没有找到")) || (paramString.contains("Nothing Found")) || (paramString.contains("抱歉，沒有找到")) || (paramString.contains("ソーリー，キーワード")) || (paramString.contains("아무것도")))
	      return localArrayList;
	    int i = paramString.indexOf("<a class=\"select\"") + "<a class=\"select\"".length();
	    int j = paramString.indexOf("</a>", i);
	    String str;
	    if ((i > "<a class=\"select\"".length()) && (j >= 0))//获取搜索结果数
	    {
	      str = paramString.substring(i, j);
	      Log.e("countHTML", str);
	      i = str.indexOf("(") + "(".length();
	      str = str.substring(i, str.indexOf(")", i));
	      if (str.length() > 0)
	      {
	        this.mRecordCount = Integer.parseInt(str);
	        Log.e("记录", "" + this.mRecordCount);
	      }
	    }
	    //获取包含全部信息的每一条资源的代码段
	    i = paramString.indexOf("<dl class=\"list-con\">", paramString.indexOf("<div class=\"content\">") + "<div class=\"content\">".length());
	    for(;i>=0;i=j){
	    	j = paramString.indexOf("<dl class=\"list-con\">", "<dl class=\"list-con\">".length() + i);
	    	if(j<0){
	    		str = paramString.substring("<dl class=\"list-con\">".length() + i);
		    	localArrayList.add(parseItemHtml(str));
	    		break;
	    	}
	    	str = paramString.substring("<dl class=\"list-con\">".length() + i, j);
	    	localArrayList.add(parseItemHtml(str));
	    }
	    
	    return localArrayList;
	  }

	  protected SearchResult parseItemHtml(String paramString)//解析出各项结果
	  {
	    SearchResult localSearchResult = new SearchResult();
	    int i = paramString.indexOf("target=\"_blank\">") + "target=\"_blank\">".length();
	    String str1 = paramString.substring(i, paramString.indexOf("</a>", i)).replaceAll("<b>", "").replaceAll("</b>", "");
	    String str2 = paramString.substring(paramString.indexOf("</a>", i) + "</a>".length());
	    i = str2.indexOf("<a href=\"") + "<a href=\"".length();
	    paramString = str2.substring(i, str2.indexOf("\">[", i));
	    String str3 = str2.substring(str2.indexOf("\">[", i) + "\">[".length());
	    i = str3.indexOf("<b>") + "<b>".length();
	    str2 = str3.substring(i, str3.indexOf("</b>", i));
	    String str4 = str3.substring(str3.indexOf("</b>", i) + "</b>".length());
	    i = str4.indexOf("<b>") + "<b>".length();
	    str3 = str4.substring(i, str4.indexOf("</b>", i));
	    String str5 = str4.substring(str4.indexOf("</b>", i) + "</b>".length());
	    i = str5.indexOf("<b>") + "<b>".length();
	    str4 = str5.substring(i, str5.indexOf("</b>", i));
	    String str6 = str5.substring(str5.indexOf("</b>", i) + "</b>".length());
	    i = str6.indexOf("<b>") + "<b>".length();
	    str5 = str6.substring(i, str6.indexOf("</b>", i));
	    str6 = str6.substring(str6.indexOf("</b>", i) + "</b>".length());
	    i = str6.indexOf("<b>") + "<b>".length();
	    str6 = str6.substring(i, str6.indexOf("</b>", i));
	    localSearchResult.keyword = this.mKeyWord;
	    localSearchResult.name = str1;
	    localSearchResult.link = paramString;
	    localSearchResult.recordingTime = str2;
	    localSearchResult.fileSize = str3;
	    localSearchResult.fileCount = str4;
	    localSearchResult.speed = str5;
	    localSearchResult.popularity = str6;
	    return localSearchResult;
	  }
	  
	  //执行搜索，传递关键字，搜索状态，初始化
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
