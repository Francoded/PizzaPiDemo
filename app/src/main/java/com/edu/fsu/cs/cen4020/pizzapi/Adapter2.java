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

public class Adapter2 extends ArrayAdapter {

    private static final String TAG = "Adapter2";
    private final Activity context;
    private final ArrayList<String> itemname = new ArrayList<>();
    private final ArrayList<Integer> switchPos = new ArrayList<>();

    private TextView txtTitle;
    private ImageView image;

    public Adapter2(Activity context, ArrayList<String> itemname, ArrayList<Integer> switchPos) {
        super(context, R.layout.adapter, itemname);

        this.context = context;
        this.itemname.addAll(itemname);
        this.switchPos.addAll(switchPos);
    }

    @Override
    public int getCount() {
        Log.d(TAG,"This is switchpos size: " + switchPos.size());
        switchPos.clear();
        switchPos.addAll(ListFragment.imagepos);
        return switchPos.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d(TAG,"This is listContent size in notify: " + ListFragment.listContent.size());
        itemname.clear();
        itemname.addAll(ListFragment.listContent);
        for (String x : ListFragment.listContent)
            Log.d(TAG, "This is list Contet: " + x);
    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.adapter, null, true);

        txtTitle = (TextView) rowView.findViewById(R.id.textView3);
        image = (ImageView) rowView.findViewById(R.id.imageView2);

        Log.d(TAG, "itemname length: " + itemname.size());
        // if(!itemname.isEmpty()) {
        txtTitle.setText(itemname.get(position));

        if (switchPos.get(position) == 1) {
            image.setImageResource(R.drawable.ic_selected_ingredient);
        } else {
            image.setVisibility(View.INVISIBLE);
        }
        // }

        return rowView;
    }

    public void check(int position, String item) {
        Log.d(TAG, "Item onClickListener triggered: set check");
        ListFragment.imagepos.set(position, 1);
        image.setImageResource(R.drawable.ic_selected_ingredient);
        try {
            PostData addFavRec = new PostData();
            Log.d(TAG,"This is item: " + item);
            addFavRec.execute(getContext().getString(R.string.serverIP) + "addItem/", LoginActivity.getUsername(), "ingredient", item);
        } catch (Exception x) {
            x.printStackTrace();
            Log.e(TAG, "Failed to add selected ingredients!");
        }
        notifyDataSetChanged();
    }

    public void uncheck(int position, String item) {
        Log.d(TAG,"This is listContent size in uncheck: " + ListFragment.listContent.size());
        Log.d(TAG, "Item onClickListener triggered: remove check");
        Log.d(TAG, "MainActivity.getToolbarName() = \t" + MainActivity.getToolbarName());
        ListFragment.imagepos.set(position, 0);
        image.setVisibility(View.INVISIBLE);
        String temp = txtTitle.getText().toString();
        //remove from database
        try {
            PostData removeFavRec = new PostData();
            removeFavRec.execute(getContext().getString(R.string.serverIP) + "deleteItem/", LoginActivity.getUsername(), "ingredient", item);
            //remove from list if favorite recipes page
            if (MainActivity.getToolbarName().equals("Selected Ingredients")) {
                Log.d(TAG, "Removing item from listview in \"Selected Ingredients\"");
                ListFragment.imagepos.remove(ListFragment.listContent.indexOf(temp));
                ListFragment.listContent.remove(temp);
            }
        } catch (Exception x) {
            x.printStackTrace();
            Log.e(TAG, "Failed to remove selected ingredients!");
        }
        notifyDataSetChanged();
    }
}
