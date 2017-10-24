package com.poleon.search;

import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class ViewPagerActivity extends FragmentActivity implements
		OnPageChangeListener, TabListener {

	private ViewPager mPager;
	private ArrayList<Fragment> mfragmentList;
	// 标题列表
	ArrayList<String> titleList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view_pager);

		initViewPager();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void initViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);

		mfragmentList = new ArrayList<Fragment>();
		mfragmentList.add(new FragmentMagnet());
		mfragmentList.add(new FragmentNetdisk());

		mPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),mfragmentList));
		mPager.setCurrentItem(0);   
		mPager.addOnPageChangeListener(this);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getActionBar().setDisplayShowHomeEnabled(false);
		// 初始化TAB属性
		
		String[] tabName = null;
		String[] temTabName = { "磁力", "网盘" };
		tabName = temTabName;

		for (int i = 0; i < tabName.length; i++) {
			ActionBar.Tab tab = getActionBar().newTab();
			tab.setText(tabName[i]);
			tab.setTabListener(this);
			tab.setTag(i);
			getActionBar().addTab(tab);
		}
	}

	//页面选项卡Fragment适配器
	public class MyViewPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyViewPagerAdapter(FragmentManager fManager,
				ArrayList<Fragment> arrayList) {
			super(fManager);
			this.list = arrayList;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Fragment getItem(int arg0) {

			return list.get(arg0);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == ((Fragment) obj).getView();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageSelected(int arg0) {
		//滑动ViewPager的时候设置相对应的ActionBar Tab被选中  
		getActionBar().getTabAt(arg0).select();

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		if (tab.getTag() == null)
			return;
		//选中tab,滑动选项卡
		int index = ((Integer) tab.getTag()).intValue();
		if (mPager != null && mPager.getChildCount() > 0
				&& mfragmentList.size() > index)
			mPager.setCurrentItem(index);
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {  
	    getMenuInflater().inflate(R.menu.main, menu);  
	    return true;  
	} 
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 退出
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


	public boolean checkNetWork(){
		
		ConnectivityManager con=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
		boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
		boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();  
		if(wifi|internet){  
			return true;
		}else{  
			return false;
		}  
	}
	
	public void showDialog(String paramString1, String paramString2){
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
