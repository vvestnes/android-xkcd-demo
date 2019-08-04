package com.example.xkcd.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.xkcd.R;
import com.example.xkcd.repository.info.InfoEntity;
import com.example.xkcd.viewmodel.ItemViewModel;
import com.github.chrisbanes.photoview.PhotoView;

import org.jetbrains.annotations.NotNull;

public class ItemFragment extends Fragment {

    private static final String TAG = "ItemFragment";

    private AlertDialog dialog;

    private InfoEntity mInfo;

    private ItemViewModel mModel;

    public static ItemFragment newInstance(int num) {
        Log.d(TAG, "newInstance: " + num);
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    private void share(int num) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject).replace("{num}", Integer.toString(num)));
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text).replace("{num}", Integer.toString(num)));
        startActivity(Intent.createChooser(i, getString(R.string.share_title)));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        final Context ctx = getContext();
        int num = getArguments() != null ? getArguments().getInt("num", 1) : 1;

        mModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        /*
        mModel.getInfo(num).observe(this, new Observer<Info>() {

            @Override
            public void onChanged(Info item) {
                Log.d(TAG, "info onChanged: " + item);
                if(item != null) {
                    setupInfoViews(item);
                }
            }
        });
        */

        mModel.getInfo2(num).observe(this, new Observer<InfoEntity>() {
            @Override
            public void onChanged(InfoEntity info) {
                Log.d(TAG, "LiveData onChanged: " + info);
                if (info != null) {
                    setupInfoViews(info);
                }
            }
        });
        // TODO: implement favorite status button
        //mModel.getFavorite(mNum);
    }

    /*
    private void showDataNotAvailable() {
        Log.d(TAG, "showDataNotAvailable: ");
        View view = getView();
        if(view == null) {
            Log.d(TAG, "==> view is null ");
            return;
        }
        Log.d(TAG, "==> hiding all info views... ");
        view.findViewById(R.id.view_header).setVisibility(View.GONE);
        view.findViewById(R.id.photoview).setVisibility(View.GONE);
        view.findViewById(R.id.view_actions).setVisibility(View.GONE);
        view.findViewById(R.id.view_actions).setVisibility(View.VISIBLE);
    }
    */

    private void hideLoadingInterstitial() {
        View view = getView();
        if(view == null) {
            return;
        }
        view.findViewById(R.id.view_loading_interstitial).setVisibility(View.GONE);
    }

    private void setupInfoViews(final InfoEntity info) {
        Log.d(TAG, "setupInfoViews: " + info);

        final Context ctx = getContext();
        View view = getView();

        /*
         * Context or View is not available (Fragment destroyed)
         */
        if (ctx == null || view == null) {
            Log.d(TAG, "==> setupInfoViews: Skipping ctx or view is null: ");
            return;
        }

        mInfo = info;

        TextView mTextViewTitle = view.findViewById(R.id.textview_title);
        TextView mTextViewNum = view.findViewById(R.id.textview_num);
        PhotoView mPhotoView = view.findViewById(R.id.photoview);

        mTextViewTitle.setText(info.getTitle());
        mTextViewNum.setText(String.valueOf(info.getNum()));
        Glide.with(ctx)
                .load(info.getImg())
                .fallback(R.drawable.ic_image_fallback)
                .placeholder(R.drawable.ic_imgage_placeholder)
                .into(mPhotoView);

        ImageView mImageViewShare = view.findViewById(R.id.imageview_share);
        ImageView mImageViewTranscript = view.findViewById(R.id.imageview_transcript);

        mImageViewTranscript.setImageResource(getTranscriptIcon(mInfo.getTranscript()));


        final ImageView mImageViewFavorite = view.findViewById(R.id.imageview_favorite);

        mImageViewFavorite.setImageResource(getFavoriteIcon(info.isFavorite()));

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), info.getAlt(), Toast.LENGTH_LONG).show();
            }
        });
        mImageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean newVal = !mInfo.isFavorite();
                Log.d(TAG, "onClick: updateFavorite: curVal=" + mInfo.isFavorite() + " => newVal = " + newVal);
                mImageViewFavorite.setImageResource(getFavoriteIcon(newVal));
                mInfo.setFavorite(newVal);
                Log.d(TAG, "onClick: new val = " + newVal + " => " + mInfo.toString());
                mModel.updateInfo(mInfo);
            }
        });
        mImageViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(info.getNum());
            }
        });


        mImageViewTranscript.setEnabled(mInfo.getTranscript().trim().length() > 0);

        mImageViewTranscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dialog = new AlertDialog.Builder(ctx)
                        .setTitle("Transcript")
                        .setMessage(info.getTranscript())
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        //.setIcon(android.R.drawable.ic_dialog_info)
                        .show();
                TextView textView = dialog.findViewById(android.R.id.message);
                //textView.setMaxLines(5);
                if (textView != null) {
                    textView.setScroller(new Scroller(ctx));
                    textView.setVerticalScrollBarEnabled(true);
                    textView.setMovementMethod(new ScrollingMovementMethod());
                }
            }
        });
        hideLoadingInterstitial();
    }

    private int getFavoriteIcon(boolean isFavorited) {
        if(isFavorited) {
            return R.drawable.ic_favorite_pink;
        } else {
            return R.drawable.ic_favorite_gray;
        }
    }

    private int getTranscriptIcon(String transcript) {
        if(transcript.trim().length()>0) {
            return R.drawable.ic_description_gray;
        } else {
            return R.drawable.ic_description_light;
        }
    }

}