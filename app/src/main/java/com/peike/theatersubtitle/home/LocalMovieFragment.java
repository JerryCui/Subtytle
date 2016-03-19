package com.peike.theatersubtitle.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.Movie;

public class LocalMovieFragment extends Fragment {
    private LocalMovieRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_local_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshLocalView();
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = getAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private LocalMovieRecyclerAdapter getAdapter() {
        LocalMovieRecyclerAdapter localMovieRecyclerAdapter = new LocalMovieRecyclerAdapter();
        localMovieRecyclerAdapter.setItemClickListener(new LocalMovieRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClicked(Movie movie) {
                ((HomeActivity) getActivity()).onMovieClicked(movie);
            }
        });
        return localMovieRecyclerAdapter;
    }

    private void refreshLocalView() {

    }

}
