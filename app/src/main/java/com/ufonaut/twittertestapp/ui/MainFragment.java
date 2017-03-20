package com.ufonaut.twittertestapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ufonaut.twittertestapp.R;
import com.ufonaut.twittertestapp.api.model.Tweet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements MainCallback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.lvTweets)
    ListView lvTweets;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private MainPresenterImpl mainPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        initPresenter();
        checkUserTimeLine(savedInstanceState);
        initListeners();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mainPresenter.onPause();
    }

    private void initPresenter() {
        mainPresenter = new MainPresenterImpl(this);
    }

    private void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void checkUserTimeLine(Bundle savedInstanceState) {
        mainPresenter.getUserTimeLine(savedInstanceState);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoginError(String error) {
        showError(error);
    }


    @Override
    public void onUserTimelineSuccess(List<Tweet> tweets) {
        if (tweets.size() > 0) {
            lvTweets.setAdapter(new TweetsAdapter(getActivity(), tweets));
        } else {
            showError(getString(R.string.no_tweets));
        }
    }

    @Override
    public void onUserTimelineError(String error) {
        showError(error);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mainPresenter.onSavedInstanceState(outState);
    }


    @Override
    public void onRefresh() {
        mainPresenter.getUserTimeLine(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if (mainPresenter != null)
            searchView.setQuery(mainPresenter.getScreenName(), false);
        if (lvTweets.getAdapter() == null)
            searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainPresenter.saveScreenName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void showError(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
