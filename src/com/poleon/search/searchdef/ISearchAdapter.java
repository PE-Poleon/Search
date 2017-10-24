package com.poleon.search.searchdef;

public abstract interface ISearchAdapter {

	  public abstract int getRecordCount();

	  public abstract boolean next();

	  public abstract void search(String paramString, OnSearchResultListener paramOnSearchResultListener);
}
