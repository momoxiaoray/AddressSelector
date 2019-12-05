package com.dmc.addressselector;

import android.content.Context;
import android.net.Network;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class AddressListView extends FrameLayout {
    private Context mContext;
    private RecyclerView recyclerView;
    private ProgressBar loadingBar;

    private List<Address> mAddressDataList = new ArrayList<>();
    private Address selectBean = null;
    private AddressAdapter addressAdapter;
    private OnAddressActionListener onAddressActionListener;

    private String title;

    public AddressListView(@NonNull Context context) {
        this(context, null);
    }

    public AddressListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public AddressListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.view_address_selector, this, true);
        loadingBar = view.findViewById(R.id.loading);
        recyclerView = view.findViewById(R.id.recycler_view);
        addressAdapter = new AddressAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(addressAdapter);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取数据
     *
     * @param level 模拟级别，网络请求的话，这里可以传父级id
     */
    public void initData(int level) {
        loadingBar.setVisibility(View.VISIBLE);
        //模拟网络请求
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingBar.setVisibility(View.GONE);
                mAddressDataList.addAll(DataUtils.getTestData(level));
                addressAdapter.notifyDataSetChanged();
            }
        }, 500);
    }


    private class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        private Context mContext;

        AddressAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_addrss_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return mAddressDataList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
            Address item = mAddressDataList.get(i);
            holder.index.setVisibility(item.isCheck() ? View.VISIBLE : View.GONE);
            holder.itemTitle.setText(item.getTitle());
            holder.itemTitle.setTextColor(item.isCheck() ? mContext.getResources().getColor(R.color.colorPrimary)
                    : mContext.getResources().getColor(R.color.text_color));
            holder.itemLayout.setBackgroundResource(item.isCheck() ? R.color.grey_100 : R.color.white);
            holder.itemLayout.setOnClickListener(view -> {
                for (int j = 0; j < mAddressDataList.size(); j++) {
                    mAddressDataList.get(j).setCheck(i == j);
                }
                addressAdapter.notifyDataSetChanged();
                //记录下选择的地址
                selectBean = mAddressDataList.get(i);

                if (onAddressActionListener != null) {
                    onAddressActionListener.onClick(item);
                }
            });
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout itemLayout;
            TextView itemTitle;
            ImageView index;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemLayout = itemView.findViewById(R.id.item_layout);
                itemTitle = itemView.findViewById(R.id.title);
                index = itemView.findViewById(R.id.index);
            }
        }
    }

    public void setItemClickListener(OnAddressActionListener itemClickListener) {
        this.onAddressActionListener = itemClickListener;
    }

    public interface OnAddressActionListener {
        void onClick(Address address);
    }

    public Address getSelectAddress() {
        return selectBean;
    }
}
