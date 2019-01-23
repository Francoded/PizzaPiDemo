package com.edu.fsu.cs.cen4020.pizzapi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HelpFragment extends Fragment{

    ListView list;
    RelativeLayout layout;
    Button back;
    TextView title;
    TextView help;

    private RecipeInfoFragment.OnFragmentInteractionListener mListener;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_help, container, false);

        layout = (RelativeLayout) view.findViewById(R.id.help_content);
        back = (Button) view.findViewById(R.id.button_help);
        list = (ListView) view.findViewById(R.id.listview_help_options);
        title = (TextView) view.findViewById(R.id.textView_help_title);
        help = (TextView) view.findViewById(R.id.textView_help);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                title.setText((String) list.getItemAtPosition(i));
                help.setText(getResources().getStringArray(R.array.option_description)[i]);
                list.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public static HelpFragment newInstance() {
        return new HelpFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeInfoFragment.OnFragmentInteractionListener) {
            mListener = (RecipeInfoFragment.OnFragmentInteractionListener) context;
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
