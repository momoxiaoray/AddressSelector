package com.dmc.addressselector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView hello;
    private AddressSelector selector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hello = findViewById(R.id.hello);
        findViewById(R.id.btn).setOnClickListener(view -> showDialog());
    }

    private void showDialog() {
        if (selector == null) {
            selector = new AddressSelector(MainActivity.this);
            selector.setAddressClickListener((titleList, selectAddress) -> {
                hello.setText(getAddressTitle(titleList));
            });
            //这里传的title是第一级的标题
            selector.createAddressListView("中国");
        }
        selector.show();
    }

    /**
     * 整理下地址标题
     * 例如：自贡市/富顺县/飞龙冠
     *
     * @return
     */
    private String getAddressTitle(List<String> titleList) {
        StringBuffer titleBuffer = new StringBuffer();
        for (int i = 0; i < titleList.size(); i++) {
            titleBuffer.append(titleList.get(i) + (i == titleList.size() - 1 ? "" : "/"));
        }
        return titleBuffer.toString();
    }

}
