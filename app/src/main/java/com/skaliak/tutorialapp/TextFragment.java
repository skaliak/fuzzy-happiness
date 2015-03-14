package com.skaliak.tutorialapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by skaliak on 3/7/15.
 */

//TODO delete this?
public class TextFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_frag_activity_first, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("***** TextFragment", "********In onActivityCreated********");

        View view = getView();
        if (view != null) {
            TextView tv = (TextView) view.findViewById(R.id.the_text);
            Bundle args = getArguments();
            tv.setText((String) args.get("text"));
        } else
            Log.d("***** TextFragment", "getView returned null!!!!");

    }
}
