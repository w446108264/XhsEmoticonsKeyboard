package sj.keyboard.interfaces;

import android.view.ViewGroup;

import sj.keyboard.adpater.EmoticonsAdapter;

public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}
