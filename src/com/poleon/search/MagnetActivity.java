package com.poleon.search;

import java.util.List;

import com.poleon.search.searchdef.ISearchAdapter;
import com.poleon.search.searchdef.OnSearchResultListener;
import com.poleon.search.searchdef.SearchResult;
import com.poleon.search.searchdef.engine.BtKittyAdapter;
import com.poleon.search.viewadapter.EndlessRecyclerOnScrollListener;
import com.poleon.search.viewadapter.SearchResultAdapter;
import com.poleon.search.viewadapter.SearchResultAdapter.OnItemClickListener;
import com.poleon.search.viewadapter.SearchResultAdapter.SearchItemViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MagnetActivity extends Activity {
	
	private static final String TAG = MagnetActivity.class.getSimpleName();
	private EndlessRecyclerOnScrollListener eListner;
	private ISearchAdapter mSearchAdapter;
	private RecyclerView mRecyclerView;
	private ProgressBar mSearchLoadingBar;
	private SearchResultAdapter mSearchResultAdapter;
	private TextView mTipsView;
	private WebView mWebview;
	private LinearLayout mLinearlayout;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magnet);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.back);
		
		Intent intent=getIntent();
		final CharSequence c=intent.getCharSequenceExtra("something");
		
		mSearchLoadingBar=(ProgressBar) findViewById(R.id.progress_search);
		mTipsView=(TextView) findViewById(R.id.textview_magnet);
		mWebview=(WebView) findViewById(R.id.webview_magnet);
		mLinearlayout=(LinearLayout) findViewById(R.id.linearlayout);
		mRecyclerView=(RecyclerView) findViewById(R.id.recyclerview_magnet);
		
		this.mSearchLoadingBar.setVisibility(View.VISIBLE);
		this.mLinearlayout.setVisibility(View.INVISIBLE);
	    
		mSearchResultAdapter = new SearchResultAdapter(this);
		mSearchAdapter = new BtKittyAdapter(mWebview);
		
		final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
		eListner = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
			
			@Override
			public void onLoadMore(int current_page) {
				// TODO Auto-generated method stub
				if (mSearchAdapter.next())//翻页
	              {
	        		setLoading(true);
	                View localView = LayoutInflater.from(MagnetActivity.this).inflate(R.layout.search_footer, mRecyclerView, false);
	                mSearchResultAdapter.setFooterView(localView);
	                mSearchResultAdapter.notifyDataSetChanged();
	                return;
	              }
				  //已经没有更多结果
	              View localView2 =LayoutInflater.from(MagnetActivity.this).inflate(R.layout.search_footer2, mRecyclerView, false);
	              mSearchResultAdapter.setFooterView(localView2);
	              setLoading(false);
	              mSearchResultAdapter.notifyDataSetChanged();
			}
		};
		
		mSearchResultAdapter.getSearchResultList().clear();
		mSearchResultAdapter.notifyDataSetChanged();
		mSearchAdapter.search(c.toString(), new OnSearchResultListener()
        {
          public void onError()
          {
          }

          public void onSuccess(final List<SearchResult> paramAnonymous2List)//搜索成功界面
          {
            MagnetActivity.this.runOnUiThread(new Runnable()
            {
              public void run()
              {
                mSearchLoadingBar.setVisibility(View.GONE);
                mLinearlayout.setVisibility(View.VISIBLE);
                eListner.setLoading(false);
                mSearchResultAdapter.setSearchResultList(paramAnonymous2List);
                mSearchResultAdapter.notifyDataSetChanged();
                mTipsView.setText("搜索到" + mSearchAdapter.getRecordCount() + "条记录");
              }
            });
          }
        });
		this.mRecyclerView.setLayoutManager(mLinearLayoutManager);
		this.mSearchResultAdapter.setOnItemClickListener(new OnItemClickListener(){

			//重写点击的响应事件
			@Override
			public void onClick(SearchItemViewHolder localSearchItemViewHolder,
					SearchResult localSearchResult, int position) {
				// TODO Auto-generated method stub
				 if (localSearchResult != null){
			          ((ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("MagnetLink", localSearchResult.link));
			          showDialog("已复制到剪贴板", "打开百度网盘、迅雷等工具新建任务，粘贴磁力链接即可下载！");
			        }
				
			}
		});
		this.mRecyclerView.setAdapter(mSearchResultAdapter);
		this.mRecyclerView.setOnScrollListener(eListner);
	   
	}
	
		@Override
		  public void onPause(){
		    super.onPause();
		  }

		@Override
		  public void onResume(){
		    super.onResume();
		  }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // 返回
	        int id = item.getItemId();
	        if(id==android.R.id.home){
	        	this.finish();
	        }
	        return super.onOptionsItemSelected(item);
	    }
	    
	    private void showDialog(String paramString1, String paramString2){
		    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
		    localBuilder.setTitle(paramString1);
		    localBuilder.setMessage(paramString2);
		    localBuilder.setCancelable(false);
		    localBuilder.setPositiveButton("好的", new DialogInterface.OnClickListener()
		    {
		      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
		      {
		        paramAnonymousDialogInterface.dismiss();
		      }
		    });
		    localBuilder.create().show();
		  }
}
