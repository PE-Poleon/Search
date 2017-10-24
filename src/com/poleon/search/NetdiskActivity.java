package com.poleon.search;

import java.util.List;

import com.poleon.search.searchdef.ISearchAdapter;
import com.poleon.search.searchdef.OnSearchResultListener;
import com.poleon.search.searchdef.SearchResult;
import com.poleon.search.searchdef.engine.PanAdapter;
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
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class NetdiskActivity extends Activity {

	private static final String TAG = NetdiskActivity.class.getSimpleName();
	private EndlessRecyclerOnScrollListener eListner;
	private RecyclerView mRecyclerView;
	private ISearchAdapter mSearchAdapter;
	private ProgressBar mSearchLoadingBar;
	private SearchResultAdapter mSearchResultAdapter;
	private TextView mTipsView;
	private WebView mWebview;
	private LinearLayout mLinearlayout;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_netdisk);

		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setIcon(R.drawable.back);
		
		Intent intent=getIntent();
		final CharSequence c=intent.getCharSequenceExtra("something");
		
		mSearchLoadingBar=(ProgressBar) findViewById(R.id.progress_search);
		mTipsView=(TextView) findViewById(R.id.textview_netdisk);
		mWebview=(WebView) findViewById(R.id.webview_netdisk);
		mLinearlayout=(LinearLayout) findViewById(R.id.linearlayout2);
		mRecyclerView=(RecyclerView) findViewById(R.id.recyclerview_netdisk);
		
		this.mSearchLoadingBar.setVisibility(View.VISIBLE);
		this.mLinearlayout.setVisibility(View.INVISIBLE);
	    
		this.mSearchResultAdapter = new SearchResultAdapter(this);
		mSearchAdapter=new PanAdapter(mWebview);
		
		final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
		eListner = new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
			
			@Override
			public void onLoadMore(int current_page) {
				// TODO Auto-generated method stub
				if (mSearchAdapter.next())
	              {
	        		setLoading(true);
	                View localView = LayoutInflater.from(NetdiskActivity.this).inflate(R.layout.search_footer, mRecyclerView, false);
	                mSearchResultAdapter.setFooterView(localView);
	                mSearchResultAdapter.notifyDataSetChanged();
	                return;
	              }
	              View localView2 =LayoutInflater.from(NetdiskActivity.this).inflate(R.layout.search_footer2, mRecyclerView, false);
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

          public void onSuccess(final List<SearchResult> paramAnonymous2List)
          {
            NetdiskActivity.this.runOnUiThread(new Runnable()
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

			@Override
			public void onClick(SearchItemViewHolder localSearchItemViewHolder,
					SearchResult localSearchResult, int position) {
				// TODO Auto-generated method stub
				 if (localSearchResult != null){
					 Intent brs = new Intent(Intent.ACTION_VIEW,Uri.parse(localSearchResult.link));
					 NetdiskActivity.this.startActivity(brs);
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
	    
}
