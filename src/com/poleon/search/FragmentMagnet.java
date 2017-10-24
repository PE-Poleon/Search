package com.poleon.search;

import java.io.ObjectOutputStream.PutField;

import javax.security.auth.PrivateCredentialPermission;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FragmentMagnet extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View rootView = inflater.inflate(R.layout.fragment1, container, false);//关联布局文件

		return rootView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		final Button btn=(Button) getActivity().findViewById(R.id.btn_magnet);
		final EditText edt=(EditText) getActivity().findViewById(R.id.edt_magnet);
		
		final Intent intent=new Intent();
		intent.putExtra("something", edt.getText());
		intent.setClass(getActivity(),MagnetActivity.class);
		
		TextWatcher textWatch=new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if(arg0.length()>0){
					btn.setClickable(true);
				}
				else{
					btn.setClickable(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				btn.setClickable(false);
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		edt.addTextChangedListener(textWatch);
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (edt.getText().length() > 0){
					if (!((ViewPagerActivity) getActivity()).checkNetWork()){
						((ViewPagerActivity) getActivity()).showDialog("连接失败", "请检查手机网络！");
					}
					else{
						startActivity(intent);
					}
				}
			}
		});
		
		edt.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				 if ((actionId == EditorInfo.IME_ACTION_SEARCH) && (v.getText().length() > 0)){
			          if (!((ViewPagerActivity) getActivity()).checkNetWork()){
			        	  ((ViewPagerActivity) getActivity()).showDialog("连接失败", "请检查手机网络！");
			        	  return false;
			          }
			          startActivity(intent);
			          return true;
			     }
				 return true;
			}
		});
	}
	
}
