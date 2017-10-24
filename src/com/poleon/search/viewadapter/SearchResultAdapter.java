package com.poleon.search.viewadapter;

import java.util.ArrayList;
import java.util.List;

import com.poleon.search.MagnetActivity;
import com.poleon.search.R;
import com.poleon.search.R.layout;
import com.poleon.search.searchdef.SearchResult;

import android.R.color;
import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

	  private static final int FOOTER_TYPE = 1;
	  private static final int ITEM_TYPE = 2;
	  private Context mContext;
	  private int mFooterCount;
	  private View mFooterView;
	  private OnItemClickListener mOnItemClickListener;
	  private List<SearchResult> mSearchResultList =new ArrayList();

	  public SearchResultAdapter(Context paramContext)
	  {
	    this.mContext = paramContext;
	  }

	  public View getFooterView()
	  {
	    return this.mFooterView;
	  }
	  
	  public int getItemCount()
	  {
	    return this.mSearchResultList.size() + this.mFooterCount;
	  }

	  public int getItemViewType(int paramInt)
	  {
		    if (paramInt == this.mSearchResultList.size())
		      return FOOTER_TYPE;
		    return ITEM_TYPE;
	  }

	  public OnItemClickListener getOnItemClickListener()
	  {
	    return this.mOnItemClickListener;
	  }

	  public List<SearchResult> getSearchResultList()
	  {
	    return this.mSearchResultList;
	  }

	  @Override
	  public void onBindViewHolder(final RecyclerView.ViewHolder paramViewHolder, final int paramInt)
	  {
		  if(getItemViewType(paramInt)==ITEM_TYPE){
		  		final SearchResult localSearchResult;
				  final SearchItemViewHolder localSearchItemViewHolder;
				  Object localObject;
				  int i;
				  SpannableString localSpannableString;
				  int j;
				  int k;
				  int m;
				  
		      localSearchResult = (SearchResult)this.mSearchResultList.get(paramInt);
		      localSearchItemViewHolder = (SearchItemViewHolder)paramViewHolder;
		      localObject = new SpannableString(localSearchResult.name);
		      i = localSearchResult.name.toLowerCase().indexOf(localSearchResult.keyword.toLowerCase());
		      if (i >= 0)
		          ((SpannableString)localObject).setSpan(new ForegroundColorSpan(Color.parseColor("#006400")), i, localSearchResult.keyword.length() + i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		      localSearchItemViewHolder.nameText.setText((CharSequence)localObject);
		      localObject = "收录时间: " + localSearchResult.recordingTime + "  大小: " + localSearchResult.fileSize + "  人气: " + localSearchResult.popularity;
		      localSpannableString = new SpannableString((CharSequence)localObject);
		      i = ((String)localObject).indexOf(localSearchResult.recordingTime);
		      j = ((String)localObject).indexOf(localSearchResult.fileSize);
		      k = ((String)localObject).indexOf(localSearchResult.popularity);
		      m = this.mContext.getResources().getColor(android.R.color.darker_gray);
		      localSpannableString.setSpan(new ForegroundColorSpan(m), i, localSearchResult.recordingTime.length() + i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		      localSpannableString.setSpan(new ForegroundColorSpan(m), j, localSearchResult.fileSize.length() + j, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		      localSpannableString.setSpan(new ForegroundColorSpan(m), k, localSearchResult.popularity.length() + k, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		      localSearchItemViewHolder.detailText.setText(localSpannableString);
		    
		      //点击item
		      if(mOnItemClickListener!=null){
					paramViewHolder.itemView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mOnItemClickListener.onClick(localSearchItemViewHolder, localSearchResult, paramInt);
						}
					});
		      }
		    }
		  	if(getItemViewType(paramInt)==FOOTER_TYPE){
		  		return;
		  	}
		  }

	  @Override
	  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt)
	  {
		  if (paramInt == ITEM_TYPE)
		      return new SearchItemViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.search_result, paramViewGroup, false));
		    if (paramInt == FOOTER_TYPE)
		      return new StaticViewHolder(this.mFooterView);
		    return null;
	  }
	  
	  public void setFooterView(View paramView)
	  {
	    this.mFooterView = paramView;
	    if (paramView != null)
	    {
	      this.mFooterCount = 1;
	      return;
	    }
	    this.mFooterCount = 0;
	  }
	  
	  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
	  {
	    this.mOnItemClickListener = paramOnItemClickListener;
	  }

	  public void setSearchResultList(List<SearchResult> paramList)
	  {
	    this.mSearchResultList = paramList;
	  }
	  
	  public interface OnItemClickListener{
			void onClick(SearchItemViewHolder localSearchItemViewHolder, SearchResult localSearchResult, int position);
		}

	  //设置item内容
	  public class SearchItemViewHolder extends RecyclerView.ViewHolder
	  {
	    public TextView detailText;
	    public TextView nameText;

	    public SearchItemViewHolder(View arg2)
	    {
	      super(arg2);
	      this.nameText = (TextView)arg2.findViewById(R.id.name);
	      this.detailText = (TextView)arg2.findViewById(R.id.detail);
	    }
	  }
	  
	  class StaticViewHolder extends RecyclerView.ViewHolder
	  {
	    public StaticViewHolder(View arg2)
	    {
	      super(arg2);
	    }
	  }


}
