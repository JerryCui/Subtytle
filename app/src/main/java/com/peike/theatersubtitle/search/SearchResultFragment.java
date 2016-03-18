package com.peike.theatersubtitle.search;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.db.MovieSearchResult;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchResultFragment extends Fragment implements SearchActivity.View {


    private ListView resultList;
    private View progressView;
    private SearchResultAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressView = view.findViewById(R.id.progress_container);
        resultList = (ListView) view.findViewById(R.id.search_result_list);
        adapter = new SearchResultAdapter();
        resultList.setAdapter(adapter);
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieSearchResult searchResult = (MovieSearchResult) adapter.getItem(position);
                ((SearchActivity) getActivity()).onResultItemClicked(searchResult);
            }
        });
    }

    @Override
    public void updateList(List<MovieSearchResult> movieSearchResults) {
        adapter.updateList(movieSearchResults);
    }

    @Override
    public void setShowProgressCircle(boolean canShow) {
        progressView.setVisibility(canShow ? View.VISIBLE : View.GONE);
    }
}
