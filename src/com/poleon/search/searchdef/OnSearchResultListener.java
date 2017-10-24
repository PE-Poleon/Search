package com.poleon.search.searchdef;

import java.util.List;

public abstract interface OnSearchResultListener {

	  public abstract void onError();

	  public abstract void onSuccess(List<SearchResult> paramList);
}
