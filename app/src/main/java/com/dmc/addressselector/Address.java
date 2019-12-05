package com.dmc.addressselector;

public class Address {

    public String title;
    public boolean isCheck = false;

    public boolean isLastAddress = false;

    public boolean isLastAddress() {
        return isLastAddress;
    }

    public void setLastAddress(boolean lastAddress) {
        isLastAddress = lastAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
