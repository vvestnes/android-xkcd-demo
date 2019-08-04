package com.example.xkcd.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.xkcd.R;
import com.example.xkcd.fragment.GalleryFragment;
import com.example.xkcd.fragment.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static final String TAG_GALLERY = "TAG_GALLERY";
    private static final String TAG_SETTINGS = "TAG_SETTINGS";

    boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        drawer = findViewById(R.id.drawer_layout);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.hideOverflowMenu();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        /*
         * Dynamic background on the Navigation Header
         */
        AnimationDrawable pagerBackground = (AnimationDrawable) navigationView.getHeaderView(0).getBackground();
        pagerBackground.setEnterFadeDuration(2000);
        pagerBackground.setExitFadeDuration(4000);
        pagerBackground.start();

        if (savedInstanceState == null) {
            navigationView.getMenu().getItem(0).setChecked(true);
            mToolbar.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fadeOutSplash();
                }
            }, 2000);
        } else {
            findViewById(R.id.splash_screen).setVisibility(View.GONE);
        }
    }

    private void setToolbarTitle(int title) {
        setToolbarTitle(getString(title));
    }

    private void setToolbarTitle(String title) {
        String html = ("<b>XKCD</b> | <i>{title}<i>").replace("{title}", title);
        Spanned spanned = Html.fromHtml(html);
        ((Toolbar) findViewById(R.id.toolbar)).setTitle(spanned);
    }

    private void fadeOutSplash() {
        Log.d(TAG, "fadeOutSplash: ");
        final View splash = findViewById(R.id.splash_screen);
        final View toolbar = findViewById(R.id.toolbar);
        Animation fadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.anim_zoom_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                splash.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                showCurrent();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        splash.startAnimation(fadeIn);
    }

    private Fragment createFragment(String tag) {
        Log.d(TAG, "createFragment: " + tag);
        Fragment fragment;
        if (TAG_SETTINGS.equals(tag)) {
            fragment = SettingsFragment.newInstance();
        } else {
            Log.d(TAG, "createFragment: default");
            fragment = GalleryFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        /*
         * Close navigation drawer
         */
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        /*
         * Require double back tap to exit
         */
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.click_back_again), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: " + query);
                showSearchResult(query);
                // Collapse the search view
                searchView.setQuery("", false);
                searchView.setIconified(true);
                deselectNavMenuItems();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "onQueryTextChange: " + s);
                return true;
            }
        });
        return true;
    }

    private void deselectNavMenuItems() {
        NavigationView nav = findViewById(R.id.nav_view);
        int size = nav.getMenu().size();
        for (int i = 0; i < size; i++) {
            nav.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Fragment getFragment(String tag) {
        Log.d(TAG, "getFragment: " + tag);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            return createFragment(tag);
        }
        return fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: ");
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.nav_current:
                showCurrent();
                break;
            case R.id.nav_browse:
                showBrowse();
                break;
            case R.id.nav_favorites:
                showFavorites();
                break;
            case R.id.nav_settings:
                showSettings();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showSearchResult(String query) {
        Log.d(TAG, "showSearchResult: " + query);
        GalleryFragment fragment = (GalleryFragment) getFragment(TAG_GALLERY);
        fragment.initSearch(query);
        showFragment(fragment, TAG_GALLERY);
        setToolbarTitle(SpannableString.valueOf("<b>" + query + "</b>").toString());
    }

    private void showCurrent() {
        Log.d(TAG, "showCurrent: ");
        GalleryFragment fragment = (GalleryFragment) getFragment(TAG_GALLERY);
        fragment.initCurrent();
        showFragment(fragment, TAG_GALLERY);
        setToolbarTitle(R.string.menu_current);
    }

    private void showBrowse() {
        Log.d(TAG, "showBrowse: ");
        GalleryFragment fragment = (GalleryFragment) getFragment(TAG_GALLERY);
        fragment.initBrowse();
        showFragment(fragment, TAG_GALLERY);
        setToolbarTitle(R.string.menu_gallery);
    }

    private void showFavorites() {
        Log.d(TAG, "showFavorites: ");
        GalleryFragment fragment = (GalleryFragment) getFragment(TAG_GALLERY);
        fragment.initFavorites();
        showFragment(fragment, TAG_GALLERY);
        setToolbarTitle(R.string.menu_favorites);
    }

    private void showSettings() {
        Log.d(TAG, "showSettings: ");
        Fragment fragment = getFragment(TAG_SETTINGS);
        showFragment(fragment, TAG_SETTINGS);
        setToolbarTitle(R.string.menu_settings);
    }

    private void showFragment(Fragment fragment, String tag) {
        Log.d(TAG, "showFragment: oldTag=" + fragment.getTag() + ", newTag=" + tag);
        if (fragment.getTag() != null) {
            Log.d(TAG, "==> tag exist - fragment is already visible");
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }
}
