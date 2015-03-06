package com.keyboard.view.I;

import com.keyboard.bean.EmoticonBean;

public interface IView {
    void onItemClick(EmoticonBean bean);
    void onItemDisplay(EmoticonBean bean);
    void onPageChangeTo(int position);
}
