package com.dmc.addressselector;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    /**
     * 这里模拟数据，level表示地区层级
     *
     * @param level
     * @return
     */
    public static List<Address> getTestData(int level) {
        List<Address> testList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Address address = new Address();
            if (level==1){
                address.title = level + "省--" + i;
            }else if (level==2){
                address.title = level + "市--" + i;
            }else if (level==3){
                address.title = level + "县--" + i;
            }else {
                address.title = level + "镇--" + i;
            }
            //最后的层级
            address.setLastAddress(level == 4);
            testList.add(address);
        }
        return testList;
    }
}
