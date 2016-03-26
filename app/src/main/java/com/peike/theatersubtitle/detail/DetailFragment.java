package com.peike.theatersubtitle.detail;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.peike.theatersubtitle.AppApplication;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Subtitle;
import com.peike.theatersubtitle.util.Constants;
import com.peike.theatersubtitle.view.FloatingButton;
import com.peike.theatersubtitle.view.SubtitleDetailBottomSheet;

import java.util.List;

public class DetailFragment extends Fragment implements DetailActivity.View, View.OnClickListener {

    private static final String LOG_TAG = "DetailFragment";
    private CollapsingToolbarLayout collapsingToolbar;
    private NetworkImageView imageView;
    private FloatingButton downloadPlayButton;
    private RecyclerView mRecyclerView;
    private SubtitleRecyclerAdapter adapter;
    private View progressView;
    private SubtitleDetailBottomSheet bottomSheet;
    private View modalView;
    private TextView emptyText;
    private View retryView;
    private View detailView;

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
        progressView = view.findViewById(R.id.progress_view);
        modalView = view.findViewById(R.id.modal);
        emptyText = (TextView) view.findViewById(R.id.empty_text);
        retryView = view.findViewById(R.id.retry_view);
        detailView = view.findViewById(R.id.subtitle_detail);
        bottomSheet = (SubtitleDetailBottomSheet) view.findViewById(R.id.bottom_sheet);
        downloadPlayButton = (FloatingButton) view.findViewById(R.id.download_play_button);

        setupRetryView();
        setupRecyclerView();
        setupFloatingButton();
        setupBottomSheet();
    }

    private void setupRetryView() {
        Button retryButton = (Button) retryView.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);
    }

    private void setupFloatingButton() {
        downloadPlayButton.setDownloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailActivity) getActivity()).onDownloadClicked((Subtitle) v.getTag());
            }
        });
        downloadPlayButton.setPlayButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DetailActivity) getActivity()).onPlayClicked((Subtitle) v.getTag());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((DetailActivity) getActivity()).onDetailFragmentStart();
    }

    @Override
    public void onClick(View v) {
        ((DetailActivity) getActivity()).onRetryClicked();
    }

    @Override
    public void setShowProgressView(boolean canShow) {
        emptyText.setVisibility(View.GONE);
        retryView.setVisibility(View.GONE);
        progressView.setVisibility(canShow ? View.VISIBLE : View.GONE);
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

    @Override
    public void markSubtitleDownloaded(Subtitle subtitle) {
        adapter.markSubtitleDownloaded(subtitle);
    }

    @Override
    public void showEmptyText(@StringRes int emptyTextId) {
        emptyText.setText(emptyTextId);
        emptyText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRetryText() {
        retryView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setShowDetailView(boolean canShow) {
        detailView.setVisibility(canShow ? View.VISIBLE : View.GONE);
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
                downloadPlayButton.setAssociatedSubtitle(subtitle);
            }
        });

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    private void setupBottomSheet() {
        bottomSheet.initView();
        bottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
                if (bottomSheet.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if (modalView.getVisibility() == View.GONE) {
                        showModalView(true);
                    }
                } else {
                    bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    hideModalView();
                }
            }
        });
        modalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                hideModalView();
            }
        });
    }

    private void setBottomSheet(Subtitle subtitle) {
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.updateDetail(subtitle);
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
