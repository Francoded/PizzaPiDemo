package com.edu.fsu.cs.cen4020.pizzapi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.fsu.cs.cen4020.flaskinterface.GetData;
import com.edu.fsu.cs.cen4020.flaskinterface.PostData;

import java.util.ArrayList;
import java.util.Iterator;

public class ListFragment extends Fragment implements SearchView.OnQueryTextListener {

    final static String TAG = "ListFragment";

    ListView list;
    public static ArrayList<String> listContent;
    public static ArrayList<Integer> imagepos = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public static boolean onIngredient;
    Adapter adapter2;
    Adapter2 adapter3;
    SearchView searchView;

    public static String globalQuery;

    private OnFragmentInteractionListener mListener;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setListContent(String url) {
        GetData newData = new GetData();
        String mURL = getString(R.string.serverIP) + url;
        newData.execute(mURL);
        try {
            listContent = newData.get();
            // for (String x : listContent)
            //  Log.d(TAG, "After ListContent: " + x);
        } catch (Exception x) {
            x.printStackTrace();
            Log.e(TAG, "Error updating list");
        }
    }

   /* @Override
    public void onResume() {
        super.onResume();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"I am in onCreateView");
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        list = (ListView) view.findViewById(R.id.list);
        searchView = (SearchView) view.findViewById(R.id.searchList);
        searchView.setOnQueryTextListener(this);

        Log.d(TAG,"This is the query: " + searchView.getQuery());
        Log.d(TAG,"This is the globalQuery: " + globalQuery);

        String url;
        ArrayList temp;

        if(globalQuery == null) {
            imagepos.clear();
            switch (MainActivity.getToolbarName()) {
                case "All Ingredients":
                    url = "userlist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    temp = listContent;
                    url = "ingredients/";
                    setListContent(url);

                    for (int i = 0; i < listContent.size(); i++) {
                        imagepos.add(0);
                    }
                    for (int i = 0; i < listContent.size(); i++) {
                        for (int j = 0; j < temp.size(); j++) {
                            if (listContent.get(i).equals(temp.get(j))) {
                                imagepos.set(i, 1);
                                //if the recipe is in the favorite recipe list, set the star on
                            }
                        }
                    }
                    onIngredient = true;
                    break;
                case "All Recipes":
                    url = "userreclist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    temp = listContent;
                    url = "recipe/";
                    setListContent(url);
                    for (int i = 0; i < listContent.size(); i++) {
                        imagepos.add(0);
                    }
                    for (int i = 0; i < listContent.size(); i++) {
                        for (int j = 0; j < temp.size(); j++) {
                            if (listContent.get(i).equals(temp.get(j))) {
                                imagepos.set(i, 1);
                                //if the recipe is in the favorite recipe list, set the star on
                            }
                        }
                    }
                    onIngredient = false;
                    break;
                case "Selected Ingredients":
                    url = "userlist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    for (int i = 0; i < listContent.size(); i++)
                        imagepos.add(1);
                    onIngredient = true;
                    break;
                case "Available Recipes":
                    url = "userreclist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    temp = listContent;
                    url = "trueFilter/" + LoginActivity.getUsername() + "/";
                    setListContent(url);

                    for (int i = 0; i < listContent.size(); i++)
                        imagepos.add(0);
                    for (int i = 0; i < listContent.size(); i++) {
                        for (int j = 0; j < temp.size(); j++) {
                            if (listContent.get(i).equals(temp.get(j))) {
                                imagepos.set(i, 1);
                                //if the recipe is in the favorite recipe list, set the star on
                            }
                        }
                    }
                    onIngredient = false;
                    break;
                case "Favorite Recipes":
                    url = "userreclist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    for (int i = 0; i < listContent.size(); i++)
                        imagepos.add(1);
                    onIngredient = false;
                    break;
            }
        } else {
            onQueryTextSubmit(globalQuery);
        }

        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView temp = (TextView) view.findViewById(R.id.textView3);
                        Log.d(TAG, "This is the write thing?: " + temp.getText().toString());
                        String item = temp.getText().toString();
                        Log.d(TAG, "This is list size: " + list.getCount());

                        switch (((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString()) {
                            case "All Recipes":
                            case "Available Recipes":
                            case "Favorite Recipes":
                                try {
                                    onIngredient = false;
                                    Fragment fragment;
                                    fragment = RecipeInfoFragment.newInstance(item);
                                    MainActivity.getFragManager().beginTransaction().replace(R.id.flContent, fragment).addToBackStack(MainActivity.getToolbarName()).commit();
                                } catch (Exception x) {
                                    x.printStackTrace();
                                    Log.e(TAG, "An error occurred while loading the recipe's information page");
                                }
                                break;
                            case "All Ingredients":
                                try {
                                    GetData getSelected = new GetData();
                                    String url = getString(R.string.serverIP) + "userlist/" + LoginActivity.getUsername() + "/";
                                    getSelected.execute(url);
                                    ArrayList<String> data = getSelected.get();
                                    onIngredient = true;
                                    Log.d(TAG, "ListContent size: " + listContent.size() + " && imagepos size: " + imagepos.size());
                                    if (!data.contains(listContent.get(i))) {
                                        adapter3.check(i, item);
                                    } else {
                                        adapter3.uncheck(i, item);
                                    }
                                } catch (Exception x) {
                                    x.printStackTrace();
                                    Log.e(TAG, "There was an error adding an ingredient to the user's list");
                                }
                                break;
                            case "Selected Ingredients":
                                try {
                                    onIngredient = true;
                                    PostData pushIngredients = new PostData();
                                    pushIngredients.execute(getString(R.string.serverIP) + "deleteItem/", LoginActivity.getUsername(), "ingredient", item);
                                    listContent.remove(i);
                                    imagepos.remove(i);
                                    Log.d(TAG, "ListContent size: " + listContent.size() + " && imagepos size: " + imagepos.size());
                                    adapter3.notifyDataSetChanged();
                                    Toast.makeText(getContext(), "Removing " + item, Toast.LENGTH_LONG).show();
                                } catch (Exception x) {
                                    x.printStackTrace();
                                    Log.e(TAG, "There was an error removing an ingredient from the user's list");
                                }
                                break;
                        }
                    }
                }

        );

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listContent);
        adapter2 = new Adapter(getActivity(), listContent, imagepos);
        adapter3 = new Adapter2(getActivity(), listContent, imagepos);

        if (onIngredient) {
            list.setAdapter(adapter3);
        } else {
            Log.i("hi", "hi");
            list.setAdapter(adapter2);
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

    @Override
    public boolean onQueryTextSubmit(String query) {

        String url;
        ArrayList<String> temp = new ArrayList();

        globalQuery = query;
        Log.d(TAG,"This is global query: " + globalQuery);

        /*
        Sets the listContent and temp ArrayLists accordingly. The listContent ArrrayList represents
        what will be displayed in the ListView. The temp ArrayList is a subset of listContent which
        represents which ingredients/recipes are selected/favorited.
         */
        switch (MainActivity.getToolbarName()) {
            case "All Recipes":
                onIngredient = false;
                imagepos.clear();
                url = "userreclist/" + LoginActivity.getUsername() + "/";
                setListContent(url);
                temp = listContent;
                url = "recipe/";
                setListContent(url);
                Log.i("i", "listcont size: " + String.valueOf(listContent.size()) + " imagepos size: " + String.valueOf(imagepos.size()));
                break;
            case "All Ingredients":
                onIngredient = true;
                imagepos.clear();
                url = "userlist/" + LoginActivity.getUsername() + "/";
                setListContent(url);
                temp = listContent;
                url = "ingredients/";
                setListContent(url);
                Log.i("i", "listcont size: " + String.valueOf(listContent.size()) + " imagepos size: " + String.valueOf(imagepos.size()));
                break;
            case "Selected Ingredients":
                url = "userlist/" + LoginActivity.getUsername() + "/";
                setListContent(url);
                onIngredient = true;
                break;
            case "Available Recipes":
                url = "userreclist/" + LoginActivity.getUsername() + "/";
                setListContent(url);
                temp = listContent;
                url = "trueFilter/" + LoginActivity.getUsername() + "/";
                setListContent(url);
                break;
            case "Favorite Recipes":
                url = "userreclist/" + LoginActivity.getUsername() + "/";
                setListContent(url);
                onIngredient = false;
                break;
        }

        /* Eliminates things from listContent based on query */
        Iterator<String> iterator = listContent.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().toLowerCase().contains(query.toLowerCase())) {
                iterator.remove();
            }
        }

        /* Determines what is displayed as selected/favorited */
        switch (MainActivity.getToolbarName()) {
            case "All Recipes":
            case "All Ingredients":
            case "Available Recipes":
                imagepos.clear();
                for (int i = 0; i < listContent.size(); i++) {
                    imagepos.add(0);
                    for (int j = 0; j < temp.size(); j++)
                        if (listContent.get(i).equals(temp.get(j)))
                            imagepos.set(i, 1);
                }
                break;
            case "Selected Ingredients":
            case "Favorite Recipes":
                imagepos.clear();
                for (int i = 0; i < listContent.size(); i++)
                    imagepos.add(1);
                break;

        }

        Log.i("i", "listcont size: " + String.valueOf(listContent.size()) + " imagepos size: " + String.valueOf(imagepos.size()));
        for (String x : listContent)
            Log.d(TAG, "AThis is listContent: " + x);
        for (int x : imagepos)
            Log.d(TAG, "AThis is imagepos: " + x);

        // adapter2.notifyDataSetChanged();
        // adapter3.notifyDataSetChanged();

        if (!onIngredient) {
            adapter2.notifyDataSetChanged();
            list.setAdapter(adapter2);
        } else {
            adapter3.notifyDataSetChanged();
            list.setAdapter(adapter3);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        /*
        Ensures that nothing is changed unless the query is empty in which case the method returns
        a non filtered list of ingredients/recipes.
         */
        if (newText.length() == 0) {

            globalQuery = null;
            ArrayList<String> temp = new ArrayList();
            String url;

            /*
            Sets the listContent and temp ArrayLists accordingly. The listContent ArrrayList represents
            what will be displayed in the ListView. The temp ArrayList is a subset of listContent which
            represents which ingredients/recipes are selected/favorited.
            */
            switch (MainActivity.getToolbarName()) {
                case "All Recipes":
                    url = "userreclist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    temp = listContent;
                    url = "recipe/";
                    setListContent(url);
                    break;
                case "All Ingredients":
                    onIngredient = true;
                    imagepos.clear();
                    url = "userlist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    temp = listContent;
                    url = "ingredients/";
                    setListContent(url);
                    Log.i("i", "listcont size: " + String.valueOf(listContent.size()) + " imagepos size: " + String.valueOf(imagepos.size()));
                    break;
                case "Selected Ingredients":
                    url = "userlist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    onIngredient = true;
                    break;
                case "Available Recipes":
                    url = "userreclist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    temp = listContent;
                    url = "trueFilter/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    break;
                case "Favorite Recipes":
                    url = "userreclist/" + LoginActivity.getUsername() + "/";
                    setListContent(url);
                    break;
            }

            /* Determines what is displayed as selected/favorited */
            switch (MainActivity.getToolbarName()) {
                case "All Recipes":
                case "All Ingredients":
                case "Available Recipes":
                    imagepos.clear();
                    for (int i = 0; i < listContent.size(); i++) {
                        imagepos.add(0);
                        for (int j = 0; j < temp.size(); j++)
                            if (listContent.get(i).equals(temp.get(j)))
                                imagepos.set(i, 1);
                    }
                    break;
                case "Selected Ingredients":
                case "Favorite Recipes":
                    imagepos.clear();
                    for (int i = 0; i < listContent.size(); i++)
                        imagepos.add(1);
                    break;

            }

            adapter2.notifyDataSetChanged();
            adapter3.notifyDataSetChanged();
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
