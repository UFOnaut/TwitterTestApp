package com.ufonaut.twittertestapp.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.ufonaut.twittertestapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setBackStackListener();
        if (savedInstanceState == null)
            switchContent(MainFragment.newInstance());

    }

    public void switchContent(Fragment fragment) {
        if (!this.isFinishing()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(fragment.getClass().getCanonicalName());
            transaction.replace(R.id.flContent, fragment).commit();
        }
    }

    private void setBackStackListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
            }
            }
        });
    }
}
