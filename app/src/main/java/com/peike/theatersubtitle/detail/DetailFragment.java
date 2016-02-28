package com.peike.theatersubtitle.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;

import java.util.List;

public class DetailFragment extends Fragment implements DetailActivity.View {

    private static final String LOG_TAG = "DetailFragment";
    private CollapsingToolbarLayout collapsingToolbar;
    private NetworkImageView imageView;
    private RecyclerView mRecyclerView;
    private SubtitleRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        imageView = (NetworkImageView) view.findViewById(R.id.backdrop);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new SubtitleRecyclerAdapter();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
        ((DetailActivity)getActivity()).onDetailFragmentStart();
    }

    @Override
    public void setTitle(String title) {
        collapsingToolbar.setTitle(title);
    }

    @Override
    public void setBackdrop(String backdropUrl) {
        imageView.setImageUrl(backdropUrl, AppApplication.getImageLoader());
    }

    @Override
    public void updateSubtitle(List<Subtitle> subtitleList) {
        adapter.updateList(subtitleList);
    }
}
