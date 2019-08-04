package com.example.xkcd.adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.xkcd.fragment.ItemFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "GalleryAdapter";

    private List<Integer> mData;
    private int mMaxNum;

    public GalleryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : mMaxNum;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "info: pos -> " + position);
        int num = mData != null ? mData.get(position) : position + 1;
        return ItemFragment.newInstance(num);
    }

    public void setData(List<Integer> data) {
        Log.d(TAG, "setData: size -> " + data.size());
        mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setData(int maxNum) {
        mMaxNum = maxNum;
        mData = null;
    }

    public int getItemPosition(@NotNull Object object) {
        return POSITION_NONE;
    }

    public int getDataAt(int pos) {
        return mData != null ? mData.get(pos) : pos + 1;
    }
}
