package com.peike.theatersubtitle.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.MovieUtil;

import java.util.List;

public class DetailFragment extends Fragment implements DetailActivity.View {

    private static final String LOG_TAG = "DetailFragment";
    private CollapsingToolbarLayout collapsingToolbar;
    private NetworkImageView imageView;
    private FloatingActionButton playButton;
    private RecyclerView mRecyclerView;
    private SubtitleRecyclerAdapter adapter;
    private View progressBar;
    private View bottomSheet;

    private TextView fileTitle;
    private TextView language;
    private TextView fileSize;
    private TextView duration;
    private TextView downloadCount;
    private TextView addDate;

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
        progressBar = view.findViewById(R.id.progress_bar);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        setupBottomSheet(bottomSheet);
        playButton = (FloatingActionButton) view.findViewById(R.id.play_button);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new SubtitleRecyclerAdapter();
        adapter.setItemClickListener(new SubtitleRecyclerAdapter.ClickListener() {
            @Override
            public void onItemViewClicked(int position) {
                setBottomSheet(adapter.getSubtitle(position));
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }

    private void setupBottomSheet(View bottomSheet) {
        fileTitle = (TextView) bottomSheet.findViewById(R.id.sub_file_name);
        language = (TextView) bottomSheet.findViewById(R.id.sub_language);
        fileSize = (TextView) bottomSheet.findViewById(R.id.file_size);
        duration = (TextView) bottomSheet.findViewById(R.id.duration);
        downloadCount = (TextView) bottomSheet.findViewById(R.id.download_count);
        addDate = (TextView) bottomSheet.findViewById(R.id.add_date);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
        ((DetailActivity)getActivity()).onDetailFragmentStart();
    }

    @Override
    public void setShowProgressBar(boolean canShow) {
        progressBar.setVisibility(canShow ? View.VISIBLE : View.GONE);
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

    private void setBottomSheet(Subtitle subtitle) {
        bottomSheet.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.VISIBLE);
        fileTitle.setText(subtitle.getFileName());
        language.setText(subtitle.getLanguage());
        fileSize.setText(MovieUtil.byteToKB(subtitle.getFileSize()));
        downloadCount.setText(subtitle.getDownloadCount().toString());
        addDate.setText(subtitle.getAddDate());
        duration.setText(subtitle.getDuration());
    }
}
