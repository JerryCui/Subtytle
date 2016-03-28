package com.peike.theatersubtitle.search;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.peike.theatersubtitle.BaseActivity;
import com.peike.theatersubtitle.R;
import com.peike.theatersubtitle.api.ResponseListener;
import com.peike.theatersubtitle.db.DaoHelper;
import com.peike.theatersubtitle.db.MovieSearchResult;
import com.peike.theatersubtitle.detail.DetailActivity;
import com.peike.theatersubtitle.util.Constants;

import java.util.List;

public class SearchActivity extends BaseActivity {


    public interface View {

        void updateList(List<MovieSearchResult> movieSearchResults);
        void setShowProgressCircle(boolean canShow);
    }
    private static final String TAG = SearchActivity.class.getSimpleName();
    private DaoHelper daoHelper;

    private String encodedQuery;
    private View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getToolBar(PINNED_SEARCH_BOX);

        daoHelper = new DaoHelper();

        Intent intent = getIntent();
        String queryString = intent.getStringExtra(Constants.EXTRA_MOVIE_QUERY);
        setSearchBoxText(queryString);
        encodedQuery = buildQuery(queryString);

        initSearchMovieTask();
    }
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof SearchResultFragment) {
            view = (SearchResultFragment) fragment;
        }
    }

    @Override
    protected boolean canShowBackButton() {
        return false;
    }

    @Override
    protected android.view.View.OnClickListener getLeftIconClickListener() {
        return new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                finish();
            }
        };
    }

    public void onResultItemClicked(MovieSearchResult searchResult) {
        daoHelper.insertToMovie(searchResult);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.EXTRA_IMDB_ID, searchResult.getImdbId());
        startActivity(intent);
    }

    private void initSearchMovieTask() {
        view.setShowProgressCircle(true);
        new SearchMovieTask(new SearchMovieResponseListener()).execute(encodedQuery);
    }

    private String buildQuery(String query) {
        query = query.trim();
        query = query.replaceAll(" +", "+");
        return query;
    }

    private class SearchMovieResponseListener implements ResponseListener {

        @Override
        public void onSuccess() {
            view.setShowProgressCircle(false);
            List<MovieSearchResult> movieSearchResults = daoHelper.getMovieSearchResult(encodedQuery);
            Log.d(TAG, "Search result: " + movieSearchResults.size());
            view.updateList(movieSearchResults);
        }

        @Override
        public void onFailure() {
            view.setShowProgressCircle(false);

        }
    }
}
