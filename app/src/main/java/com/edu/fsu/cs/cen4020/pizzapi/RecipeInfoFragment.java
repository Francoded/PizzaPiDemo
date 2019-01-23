package com.edu.fsu.cs.cen4020.pizzapi;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.fsu.cs.cen4020.flaskinterface.GetData;
import com.edu.fsu.cs.cen4020.flaskinterface.GetImage;

import java.util.ArrayList;

public class RecipeInfoFragment extends Fragment {

    ArrayList<String> recipeInfoContent;
    String recipe;

    TextView recipeName;
    TextView description;
    TextView ingredients;
    TextView missingIngredients;
    TextView instructions;
    ImageButton favoriteButton;
    ImageView recipeImage;

    private OnFragmentInteractionListener mListener;

    public RecipeInfoFragment() {
        // Required empty public constructor
    }

    public static RecipeInfoFragment newInstance(String recipe) {
        RecipeInfoFragment fragment = new RecipeInfoFragment();
        Bundle args = new Bundle();
        args.putString("recipe", recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getString("recipe");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        switch (MainActivity.getToolbarName()) {
            case "Recipe of the Day":
                try {
                    GetData getRecipeDay = new GetData();
                    String url = getString(R.string.serverIP) + "rotd/" + LoginActivity.getUsername() + "/";
                    getRecipeDay.execute(url);
                    recipeInfoContent = getRecipeDay.get();
                } catch (Exception x) {
                    x.printStackTrace();
                    Log.e("RecipeInfoFragment", "There was an error getting the recipe of the day");
                }
                break;
            default:
                try {
                    GetData getRecipeInfo = new GetData();
                    String url = getString(R.string.serverIP) + "recipepage/" + recipe + "/" + LoginActivity.getUsername() + "/";
                    getRecipeInfo.execute(url);
                    recipeInfoContent = getRecipeInfo.get();
                } catch (Exception x){
                    x.printStackTrace();
                    Log.e("RecipeInfoFragment","There was an error getting a recipe page");
                }

        }


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        recipeName = (TextView) view.findViewById(R.id.textView_recipe);
        description = (TextView) view.findViewById(R.id.textView_description);
        ingredients = (TextView) view.findViewById(R.id.textView_ingredients);
        missingIngredients = (TextView) view.findViewById(R.id.textView_missing_ingredients);
        instructions = (TextView) view.findViewById(R.id.textView_instructions);
        favoriteButton = (ImageButton) view.findViewById(R.id.toggleButton);
        recipeImage = (ImageView) view.findViewById(R.id.recipeImage);

        try {
            GetData getFavoriteRecipes = new GetData();
            String url = getString(R.string.serverIP) + "userreclist/" + LoginActivity.getUsername() + "/";
            getFavoriteRecipes.execute(url);
            ArrayList<String> FavRecipes = getFavoriteRecipes.get();
            if (!FavRecipes.contains(recipeInfoContent.get(0))) {
                favoriteButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_start_off));
                MainActivity.isFavorited = false;
            } else {
                favoriteButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_star));
                MainActivity.isFavorited = true;
            }
        } catch (Exception x) {
            x.printStackTrace();
            Log.e("RecipeInfoFragment", "There was an error determining whether the recipe was favorited or not");
        }

        recipeName.setText(recipeInfoContent.get(0).trim());
        description.setText(recipeInfoContent.get(1).trim());
        if (!recipeInfoContent.get(2).trim().isEmpty()) {
            ingredients.setVisibility(View.VISIBLE);
            ingredients.setText(recipeInfoContent.get(2).trim());
        } else
            ingredients.setVisibility(View.GONE);
        if (!recipeInfoContent.get(3).trim().isEmpty()) {
            missingIngredients.setVisibility(View.VISIBLE);
            missingIngredients.setText(recipeInfoContent.get(3).trim());
        } else
            missingIngredients.setVisibility(View.GONE);
        instructions.setText(recipeInfoContent.get(4).trim());

        try {
            GetImage getImage = new GetImage();
            getImage.execute(getString(R.string.serverIP) + "getrecipeimage/" + recipeInfoContent.get(0).trim() + "/");
            Drawable image = getImage.get();
            recipeImage.setImageDrawable(image);
        } catch (Exception x){
            x.printStackTrace();
            Log.e("RecipeInfoFragment","Failed to get image");
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
