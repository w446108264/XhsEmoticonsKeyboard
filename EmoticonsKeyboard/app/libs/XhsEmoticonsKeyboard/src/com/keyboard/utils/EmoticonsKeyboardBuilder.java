package com.keyboard.utils;

import com.keyboard.bean.EmoticonSetBean;

import java.util.ArrayList;

public class EmoticonsKeyboardBuilder {

    public Builder builder;

    public EmoticonsKeyboardBuilder(Builder builder){
        this.builder = builder;
    }

    public static class Builder {

        ArrayList<EmoticonSetBean> mEmoticonSetBeanList = new ArrayList<EmoticonSetBean>();

        public Builder(){ }

        public ArrayList<EmoticonSetBean> getEmoticonSetBeanList() { return mEmoticonSetBeanList; }

        public Builder setEmoticonSetBeanList(ArrayList<EmoticonSetBean> mEmoticonSetBeanList) { this.mEmoticonSetBeanList = mEmoticonSetBeanList;  return this;}

        public EmoticonsKeyboardBuilder build() { return new EmoticonsKeyboardBuilder(this); }
    }
}
