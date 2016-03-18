package com.peike.theatersubtitle.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.peike.theatersubtitle.R;

public class SearchBox extends CardView {

    private final SearchView searchView;
    private final ImageView leftIcon;
    private final AutoCompleteTextView searchAutoComplete;
    private View editTextContainer;

    public SearchBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_search_view, this);

        searchView = (SearchView) findViewById(R.id.search_view);

        int leftIconId = searchView.getResources().getIdentifier("android:id/search_mag_icon", null, null);
        leftIcon = (ImageView) searchView.findViewById(leftIconId);

        int containerId = searchView.getResources().getIdentifier("android:id/search_plate", null, null);
        editTextContainer = searchView.findViewById(containerId);

        editTextContainer.setBackgroundResource(android.R.color.transparent);

        int editTextResId = searchView.getResources().getIdentifier("android:id/search_src_text", null, null);
        searchAutoComplete = (AutoCompleteTextView) searchView.findViewById(editTextResId);
//        searchAutoComplete.setBackgroundResource(android.R.color.transparent);
    }

    public void setLeftIconResource(@DrawableRes int id) {
        leftIcon.setImageResource(id);
    }

    public void setOnLeftIconClickedListener(OnClickListener listener) {
        leftIcon.setOnClickListener(listener);
    }

    public void setOnQueryTextListener(SearchView.OnQueryTextListener listener) {
        searchView.setOnQueryTextListener(listener);
    }

    public void setText(String text) {
        searchAutoComplete.setText(text);
    }

    public void hideLeftIcon() {
        leftIcon.setImageResource(android.R.color.transparent);
        leftIcon.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    }
}
