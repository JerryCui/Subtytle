package com.peike.theatersubtitle.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.util.MovieUtil;
import com.peike.theatersubtitle.view.FloatingButton;

import java.util.List;

public class DetailFragment extends Fragment implements DetailActivity.View {

    private static final String LOG_TAG = "DetailFragment";
    private CollapsingToolbarLayout collapsingToolbar;
    private NetworkImageView imageView;
    private FloatingButton downloadPlayButton;
    private RecyclerView mRecyclerView;
    private SubtitleRecyclerAdapter adapter;
    private View progressBar;
    private View bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private View modalView;

    private TextView fileTitle;
    private TextView language;
    private TextView fileSize;
    private TextView duration;
    private TextView downloadCount;
    private TextView addDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        imageView = (NetworkImageView) view.findViewById(R.id.backdrop);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        progressBar = view.findViewById(R.id.progress_bar);
        modalView = view.findViewById(R.id.modal);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        downloadPlayButton = (FloatingButton) view.findViewById(R.id.download_play_button);
        setupBottomSheet(bottomSheet);
        setupRecyclerView();
        setupFloatingButton();
    }

    private void setupFloatingButton() {
        downloadPlayButton.setDownloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailActivity) getActivity()).onDownloadClicked(v.getTag().toString());
            }
        });
        downloadPlayButton.setPlayButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailActivity) getActivity()).onPlayClicked(v.getTag().toString());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        ((DetailActivity) getActivity()).onDetailFragmentStart();
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

    @Override
    public void setShowButtonProgressCircle(boolean canShow) {
        if (canShow) {
            downloadPlayButton.showProgressCircle();
        } else {
            downloadPlayButton.hideProgressCircle();
        }
    }

    @Override
    public void fadeInPlayButton() {
        downloadPlayButton.fadeInPlayButton();
    }

    @Override
    public void setDownloadButtonEnabled(boolean enabled) {
        downloadPlayButton.setDownloadButtonEnabled(enabled);
    }

    private void setupRecyclerView() {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new SubtitleRecyclerAdapter();
        adapter.setItemClickListener(new SubtitleRecyclerAdapter.ClickListener() {
            @Override
            public void onItemViewClicked(Subtitle subtitle) {
                setBottomSheet(subtitle);

                downloadPlayButton.setVisibility(View.VISIBLE);
                if (AppApplication.getInternalFileCache().isFileExist(subtitle.getFileId().toString())) {
                    downloadPlayButton.showPlayButton();
                } else {
                    downloadPlayButton.hidePlayButton();
                }
                downloadPlayButton.setSubtitleFileId(subtitle.getFileId().toString());
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private void setupBottomSheet(final View bottomSheet) {
        fileTitle = (TextView) bottomSheet.findViewById(R.id.sub_file_name);
        language = (TextView) bottomSheet.findViewById(R.id.sub_language);
        fileSize = (TextView) bottomSheet.findViewById(R.id.file_size);
        duration = (TextView) bottomSheet.findViewById(R.id.duration);
        downloadCount = (TextView) bottomSheet.findViewById(R.id.download_count);
        addDate = (TextView) bottomSheet.findViewById(R.id.add_date);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    modalView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (modalView.getVisibility() == View.GONE && slideOffset > 0) {
                    showModalView(false);
                }
                slideOffset = slideOffset < 0F ? slideOffset + 1F : slideOffset;
                modalView.setAlpha(slideOffset / 2F);
            }

        });
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if (modalView.getVisibility() == View.GONE) {
                        showModalView(true);
                    }
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    hideModalView();
                }
            }
        });
        modalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                hideModalView();
            }
        });
    }

    private void setBottomSheet(Subtitle subtitle) {
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        fileTitle.setText(subtitle.getFileName());
        language.setText(subtitle.getLanguage());
        fileSize.setText(MovieUtil.byteToKB(subtitle.getFileSize()));
        downloadCount.setText(MovieUtil.formatNumber(subtitle.getDownloadCount()));
        addDate.setText(subtitle.getAddDate());
        duration.setText(subtitle.getDuration());
    }

    private void showModalView(boolean isAnimated) {
        modalView.setAlpha(0F);
        modalView.setVisibility(View.VISIBLE);
        if (isAnimated) {
            modalView.animate().alpha(Constants.MODAL_ALPHA);
        }
    }

    private void hideModalView() {
        modalView.animate().alpha(0F);
    }

}
