package com.dmc.addressselector;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * method:
 * author: Daimc(xiaocheng.ok@qq.com)
 * date: 2019-12-03 10:44
 * description: 地址选择器
 */
public class AddressSelector extends Dialog {
    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<AddressListView> addressListViews = new ArrayList<>();
    private ViewPagerAdapter viewPagerAdapter;
    private int selectPosition = 0;
    private OnAddressClickListener addressClickListener;

    private List<String> addressTitle = new ArrayList<>();

    //这里记录下是哪个级别了
    private int INDEX = 0;

    public AddressSelector(@NonNull Context context) {
        super(context, R.style.AddressSelectStyle);
        init(context);
    }

    public AddressSelector(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected AddressSelector(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = getLayoutInflater().inflate(R.layout.dialog_address_selector, null);
        setContentView(view);
        view.findViewById(R.id.close).setOnClickListener(view1 -> dismiss());
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        //置于界面底部
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = getScreenSizeHeight(mContext) / 2;
        window.setAttributes(params);
        //设置动画
        window.setWindowAnimations(R.style.Animation_Design_BottomSheetDialog);

        //配置ViewPager
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.white)));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public static int getScreenSizeHeight(Context con) {
        WindowManager wm = (WindowManager) con
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight(); // 屏幕高度（像素）
        return height;
    }


    public void createAddressListView(String title) {
        AddressListView addressListView = new AddressListView(mContext);
        addressListView.setTitle(title);
        addressListView.setItemClickListener(new AddressListView.OnAddressActionListener() {
            @Override
            public void onClick(Address address) {
                //这里判断下是不是点击了前面的地区，是的话，就先移除后面的视图，再加入新点击的
                if (selectPosition < addressListViews.size() - 1) {
                    List<AddressListView> tempList = new ArrayList<>();
                    for (int i = (selectPosition + 1); i < addressListViews.size(); i++) {
                        tempList.add(addressListViews.get(i));
                        //这里选择的级别也要减1
                        INDEX = INDEX - 1;
                    }
                    addressListViews.removeAll(tempList);
                    viewPagerAdapter.notifyDataSetChanged();

                }
                //如果是最后层级的地址，这里直接返回了
                if (address.isLastAddress()) {
                    if (addressClickListener != null) {
                        addressClickListener.onClick(getSelectAddress(), addressListViews.get(addressListViews.size() - 1).getSelectAddress());
                        dismiss();
                    }
                } else {
                    createAddressListView(address.getTitle());
                }
            }
        });
        INDEX = INDEX + 1;
        addressListView.initData(INDEX);
        addressListViews.add(addressListView);
        viewPagerAdapter.notifyDataSetChanged();
        //这里加个跳转，自动跳转到下一个地址选择列表
        viewPager.setCurrentItem(selectPosition + 1, true);
    }

    /**
     * 获取选择的地址信息，从第一级到最后一级
     *
     * @return
     */
    private List<String> getSelectAddress() {
        addressTitle.clear();
        for (int i = 0; i < addressListViews.size(); i++) {
            addressTitle.add(addressListViews.get(i).getSelectAddress().getTitle());
        }
        return addressTitle;
    }

    public class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return addressListViews.size();
        }


        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return addressListViews.get(position).getTitle();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(addressListViews.get(position));
            return addressListViews.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((AddressListView) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            if (object instanceof AddressListView) {
                if (addressListViews.contains(object)) {
                    return addressListViews.indexOf(object);
                } else {
                    return POSITION_NONE;
                }
            }
            return super.getItemPosition(object);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void setAddressClickListener(OnAddressClickListener addressClickListener) {
        this.addressClickListener = addressClickListener;
    }

    public interface OnAddressClickListener {
        void onClick(List<String> titleList, Address selectAddress);
    }


}
