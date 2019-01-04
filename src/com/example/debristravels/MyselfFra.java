package com.example.debristravels;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.lang.reflect.Field;
import com.example.adapter.*;
import com.example.myinfo.*;

public class MyselfFra extends Fragment{

	private ViewPager viewpager;
	private ImageView iv_parallax,iv_back,iv_date,toolbar_avatar,iv_head;
	private SmartRefreshLayout refreshLayout;
	private ButtonBarLayout buttonBarLayout;
	private CollapsingToolbarLayout collapsing_toolbar;
	private AppBarLayout appbar;
	private Toolbar toolbar;
	private int mOffset = 0;
	boolean isblack = false;//状态栏字体是否是黑色
	boolean iswhite = true;//状态栏字体是否是亮色
	private TabLayout tabLayout;

	public ImmersionBar mImmersionBar;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.myself, container, false);
		mImmersionBar = ((MainActivity)getActivity()).getmImmersionBar();

		initView(view);
		initViewPager(view);
		initListener(view);
		return view;
	}

	private void initView(View view) {
		appbar = (AppBarLayout) view.findViewById(R.id.appbar);
		viewpager = (ViewPager) view.findViewById(R.id.view_pager);
		iv_parallax = (ImageView) view.findViewById(R.id.iv_parallax);
		refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
		buttonBarLayout = (ButtonBarLayout) view.findViewById(R.id.buttonBarLayout);
		collapsing_toolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
		tabLayout = (TabLayout)view.findViewById(R.id.tabs);
		toolbar = (Toolbar) view.findViewById(R.id.toolbar);
		iv_back = (ImageView) view.findViewById(R.id.iv_back);
		iv_date = (ImageView) view.findViewById(R.id.iv_date);
		toolbar_avatar = (ImageView) view.findViewById(R.id.toolbar_avatar);
		iv_head = (ImageView) view.findViewById(R.id.iv_head);
		//初始化沉浸式
		mImmersionBar.titleBar(toolbar).init();
		RequestOptions options = new RequestOptions();
		Glide.with(this).load(R.mipmap.head)
				.apply(options.circleCrop())
				.into(iv_head);
		Glide.with(this).load(R.mipmap.head)
				.apply(options.circleCrop())
				.into(toolbar_avatar);
	}

	/**
	 * 设置item
	 * @param
	 */
	private void initViewPager(View view) {
		viewpager = (ViewPager) view.findViewById(R.id.view_pager);
		Adapter_Page adapter = new Adapter_Page(getFragmentManager());
		adapter.addFragment(new Fragment_One(), "记录");
		adapter.addFragment(new Fragment_Two(), "文章");
		//adapter.addFragment(new Fragment_Three(), "相册");
		viewpager.setAdapter(adapter);
		//设置tablayout，viewpager上的标题
		tabLayout.setupWithViewPager(viewpager);
		tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
		viewpager.setCurrentItem(1);
		tabLayout.post(new Runnable() {
			@Override
			public void run() {
				setIndicator(tabLayout,30,30);
			}
		});

	}

	private void initListener(View view) {
		refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
			@Override
			public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
				mOffset = offset / 2;
				iv_parallax.setTranslationY(mOffset);
			}
			@Override
			public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
				mOffset = offset / 2;
				iv_parallax.setTranslationY(mOffset);
			}
		});

		appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				iv_parallax.setTranslationY(verticalOffset);
				//200是appbar的高度
				if (Math.abs(verticalOffset) == DensityUtil.dp2px(200) - toolbar.getHeight()) {//关闭
					if (iswhite) {//变黑
						if (ImmersionBar.isSupportStatusBarDarkFont()) {
							mImmersionBar.statusBarDarkFont(true).init();
							isblack = true;
							iswhite = false;
						}
					}
					buttonBarLayout.setVisibility(View.VISIBLE);
					collapsing_toolbar.setContentScrimResource(R.color.white);
					iv_back.setBackgroundResource(R.mipmap.back_black);
					iv_date.setBackgroundResource(R.mipmap.date_black);
//                    toolbar.setVisibility(View.VISIBLE);
				} else {  //展开
					if (isblack) { //变白
						mImmersionBar.statusBarDarkFont(false).init();
						isblack = false;
						iswhite = true;
					}
					buttonBarLayout.setVisibility(View.INVISIBLE);
					collapsing_toolbar.setContentScrimResource(R.color.transparent);
					iv_back.setBackgroundResource(R.mipmap.back_white);
					iv_date.setBackgroundResource(R.mipmap.date_white);
//                    toolbar.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	/**
	 * 通过反射修改踏遍layout的宽，其实相当于margin
	 * @param tabs
	 * @param leftDip
	 * @param rightDip
	 */
	public void setIndicator (TabLayout tabs, int leftDip, int rightDip) {
		Class<?> tabLayout = tabs.getClass();
		Field tabStrip = null;
		try {
			tabStrip = tabLayout.getDeclaredField("mTabStrip");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		tabStrip.setAccessible(true);
		LinearLayout llTab = null;
		try {
			llTab = (LinearLayout) tabStrip.get(tabs);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
		int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

		for (int i = 0; i < llTab.getChildCount(); i++) {
			View child = llTab.getChildAt(i);
			child.setPadding(0, 0, 0, 0);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
			params.leftMargin = left;
			params.rightMargin = right;
			child.setLayoutParams(params);
			child.invalidate();
		}
	}
}
