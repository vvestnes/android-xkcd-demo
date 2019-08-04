package com.example.xkcd.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.xkcd.R;
import com.example.xkcd.adapter.GalleryAdapter;
import com.example.xkcd.viewmodel.GalleryViewModel;

import java.util.Arrays;
import java.util.List;

public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";

    private static final String SOURCE_CURRENT = "SOURCE_CURRENT";
    private static final String SOURCE_BROWSE = "SOURCE_BROWSE";
    private static final String SOURCE_FAVORITES = "SOURCE_FAVORITES";
    private static final String SOURCE_SEARCH = "SOURCE_SEARCH";

    private GalleryAdapter mAdapter;

    private Observer<List<Integer>> mObserver;
    private GalleryViewModel mModel;
    private ViewPager mPager;
    private TextView mStatus;
    private boolean mIsFragmentReady = false;

    private String mSource;
    private String mQuery;
    private boolean mResetPageSelected;

    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        mPager = view.findViewById(R.id.viewpager);
        mStatus = view.findViewById(R.id.textview_status);

        mAdapter = new GalleryAdapter(getChildFragmentManager());
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                mModel.setPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        mModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        mObserver = new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> data) {
                Log.d(TAG, "getData() onChanged: " + data);
                if (data == null) {
                    mPager.setVisibility(View.GONE);
                    mStatus.setText(R.string.data_loading_error);
                    mStatus.setVisibility(View.VISIBLE);
                } else if (data.size() == 0) {
                    mPager.setVisibility(View.GONE);
                    mStatus.setText(R.string.no_data_found);
                    mStatus.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setData(data);
                    mPager.setVisibility(View.VISIBLE);
                    mStatus.setVisibility(View.GONE);
                }
                if (mResetPageSelected) {
                    mModel.setPageSelected(0);
                    mResetPageSelected = false;
                }
                mPager.setCurrentItem(mModel.getPageSelected(), false);
            }
        };
        mIsFragmentReady = true;
        loadData(mSource);
    }

    private void setSource(String source, String... query) {
        Log.d(TAG, "setSource: " + source + ", query=" + Arrays.toString(query));
        mSource = source;
        mResetPageSelected = true;
        if (SOURCE_SEARCH.equals(source)) {
            mQuery = query[0];
        }
        /*
         * Load data only if fragment is ready (onActivityCreated is run)
         */
        if (mIsFragmentReady) {
            loadData(mSource);
        }
    }

    private LiveData<List<Integer>> liveData;

    private void loadData(String source) {
        Log.d(TAG, "loadData: " + source);
        if(liveData != null) {
            liveData.removeObserver(mObserver);
        }
        liveData = getData(source);
        liveData.observe(this, mObserver);
    }

    private LiveData<List<Integer>> getData(String source) {
        Log.d(TAG, "getData: " + source);
        switch (source) {
            case SOURCE_CURRENT:
                return mModel.getCurrentData();
            case SOURCE_BROWSE:
                return mModel.getBrowseData();
            case SOURCE_FAVORITES:
                return mModel.getFavoritesData();
            case SOURCE_SEARCH:
                return mModel.getSearchData(mQuery);
        }
        return new MutableLiveData<>();
    }

    public void initCurrent() {
        setSource(SOURCE_CURRENT);
    }

    public void initBrowse() {
        setSource(SOURCE_BROWSE);
    }

    public void initFavorites() {
        setSource(SOURCE_FAVORITES);
    }

    public void initSearch(String query) {
        setSource(SOURCE_SEARCH, query);
    }
}
