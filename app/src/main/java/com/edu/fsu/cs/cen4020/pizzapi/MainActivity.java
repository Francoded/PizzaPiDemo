package com.edu.fsu.cs.cen4020.pizzapi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.fsu.cs.cen4020.flaskinterface.PostData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListFragment.OnFragmentInteractionListener,
        RecipeInfoFragment.OnFragmentInteractionListener {

    final static String TAG = "MainActivity";

    public static boolean isFavorited = false;

    TextView username;
    static Toolbar toolbar;
    NavigationView navigationView;


    static private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        /* Load Recipe of the Day */
        Fragment fragment = RecipeInfoFragment.newInstance("");
        renameToolbar(getString(R.string.nav_recipe_day));


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(getString(R.string.nav_recipe_day)).commit();

        /* Customize Side Drawer */
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        username = (TextView) header.findViewById(R.id.usernameTextView);
        username.setText("Hello, " + LoginActivity.getUsername() + "!");
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    public static String getToolbarName() {
        return toolbar.getTitle().toString();
    }

    private void renameToolbar(String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public static FragmentManager getFragManager() {
        return fragmentManager;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        int count = fragmentManager.getBackStackEntryCount();
        if (count != 1) {
            fragmentManager.popBackStack();
            renameToolbar(fragmentManager.getBackStackEntryAt(count - 1).getName());
            for(int i = 0; i < navigationView.getMenu().size(); i++)
                if(navigationView.getMenu().getItem(i).getTitle().equals(fragmentManager.getBackStackEntryAt(count - 1).getName())){
                    navigationView.getMenu().getItem(i).setChecked(true);
                } else {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String toolbarTitle = getToolbarName();
        ListFragment.globalQuery = null;

        if (id == R.id.nav_recipe_day) {
            renameToolbar(getString(R.string.nav_recipe_day));
            fragment = RecipeInfoFragment.newInstance("");
        } else if (id == R.id.nav_recipes) {
            renameToolbar(getString(R.string.nav_recipes_all));
            fragment = ListFragment.newInstance();
            ListFragment.onIngredient = false;
        } else if (id == R.id.nav_ingredients_all) {
            renameToolbar(getString(R.string.nav_ingredients_all));
            fragment = ListFragment.newInstance();
            ListFragment.onIngredient = true;
        } else if (id == R.id.nav_ingredients_selected) {
            renameToolbar(getString(R.string.nav_ingredients_selected));
            fragment = ListFragment.newInstance();
            ListFragment.onIngredient = true;
        } else if (id == R.id.nav_recipes_available) {
            renameToolbar(getString(R.string.nav_recipes_available));
            fragment = ListFragment.newInstance();
            ListFragment.onIngredient = false;
        } else if (id == R.id.nav_recipes_favorite) {
            renameToolbar(getString(R.string.nav_recipes_favorite));
            fragment = ListFragment.newInstance();
            ListFragment.onIngredient = false;
        } else if (id == R.id.action_help) {
            renameToolbar(getString(R.string.menu_help));
            fragment = HelpFragment.newInstance();
        } else if (id == R.id.nav_logout) {
            Intent mIntent = new Intent(this, LoginActivity.class);
            startActivity(mIntent);
            return true;
        }

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(toolbarTitle).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void toggleFavorite(View view) {
        ImageButton favoriteButton = (ImageButton) findViewById(view.getId());
        String recipe = ((TextView) findViewById(R.id.textView_recipe)).getText().toString();
        if (!isFavorited) {
            try {
                PostData addFavRec = new PostData();
                addFavRec.execute(getString(R.string.serverIP) + "addRecipe/", LoginActivity.getUsername(), "recipe", recipe);
                if (addFavRec.get()) {
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_star));
                    isFavorited = true;
                    Toast.makeText(this, "Adding " + recipe, Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(this, "Failed to add " + recipe, Toast.LENGTH_SHORT).show();
            } catch (Exception x) {
                x.printStackTrace();
                Log.e(TAG, "There was an error adding a favorite recipe to the user's list");
            }
        } else {
            try {
                PostData removeFavRec = new PostData();
                removeFavRec.execute(getString(R.string.serverIP) + "deleteRecipe/", LoginActivity.getUsername(), "recipe", recipe);
                if (removeFavRec.get()) {
                    favoriteButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_start_off));
                    isFavorited = false;
                    Toast.makeText(this, "Removing " + recipe, Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(this, "Failed to remove " + recipe, Toast.LENGTH_SHORT).show();
            } catch (Exception x) {
                x.printStackTrace();
                Log.e(TAG, "There was an error removing a favorite recipe to the user's list");
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
