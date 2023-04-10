package com.ddsmile.util;

public class ButtonUtil {
    public int flag;//开关标志位
    public String buttonName;//按钮名称

    public ButtonUtil() {
    }

    public ButtonUtil(int flag, String buttonName) {
        this.flag = flag;
        this.buttonName = buttonName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setButtonName(String buttonName) {
        this.buttonName = buttonName;
    }
}
