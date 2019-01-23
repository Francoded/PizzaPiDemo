package com.edu.fsu.cs.cen4020.pizzapi;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.fsu.cs.cen4020.flaskinterface.PostData;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter {


    private static final String TAG = "Adapter";
    private final Activity context;
    private final ArrayList<String> itemname = new ArrayList<>();
    private final ArrayList<Integer> switchPos = new ArrayList<>();

    public Adapter(Activity context, ArrayList<String> itemname, ArrayList<Integer> switchPos) {
        super(context, R.layout.adapter, itemname);

        this.context = context;
        this.itemname.addAll(itemname);
        this.switchPos.addAll(switchPos);
    }


    @Override
    public int getCount() {
        switchPos.clear();
        switchPos.addAll(ListFragment.imagepos);
        return switchPos.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        itemname.clear();
        itemname.addAll(ListFragment.listContent);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        //Log.i("help", String.valueOf(position));

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.adapter, null, true);

        if (position >= switchPos.size()) {
            Log.i("help", String.valueOf(position));
            return rowView;
        }
        final TextView txtTitle = (TextView) rowView.findViewById(R.id.textView3);
        final ImageView image = (ImageView) rowView.findViewById(R.id.imageView2);

        txtTitle.setText(itemname.get(position));

        if (switchPos.get(position) == 1) {
            image.setImageResource(R.drawable.ic_favorite_star);
        } else {
            image.setImageResource(R.drawable.ic_favorite_start_off);
        }

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String recipe = txtTitle.getText().toString();
                if (switchPos.get(position) == 1) {
                    Log.d(TAG, "Item onClickListener triggered: remove star");
                    Log.d(TAG, "MainActivity.getToolbarName() = \t" + MainActivity.getToolbarName());
                    Log.d(TAG, "R.string.nav_recipes_favorite = \t" + R.string.nav_recipes_favorite);
                    ListFragment.imagepos.set(position, 0);
                    image.setImageResource(R.drawable.ic_favorite_start_off);
                    String temp = txtTitle.getText().toString();
                    //remove from database
                    try {
                        PostData removeFavRec = new PostData();
                        removeFavRec.execute(getContext().getString(R.string.serverIP) + "deleteRecipe/", LoginActivity.getUsername(), "recipe", recipe);
                        //remove from list if favorite recipes page
                        if (MainActivity.getToolbarName().equals("Favorite Recipes")) {
                            ListFragment.imagepos.remove(ListFragment.listContent.indexOf(temp));
                            ListFragment.listContent.remove(temp);
                            Log.d(TAG, "Removing item from listview in \"Favorite Recipes\"");
                        }
                    } catch (Exception x) {
                        x.printStackTrace();
                        Log.e(TAG, "Failed to remove favorite recipes!");
                    }
                } else {
                    Log.d(TAG, "Item onClickListener triggered: set star");
                    ListFragment.imagepos.set(position, 1);
                    image.setImageResource(R.drawable.ic_favorite_star);
                    try {
                        PostData addFavRec = new PostData();
                        addFavRec.execute(getContext().getString(R.string.serverIP) + "addRecipe/", LoginActivity.getUsername(), "recipe", recipe);
                    } catch (Exception x) {
                        x.printStackTrace();
                        Log.e(TAG, "Failed to add favorite recipes!");
                    }
                }
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
