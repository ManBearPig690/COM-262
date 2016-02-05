package com.fleuret.memegenerator;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomPhotoFragment extends Fragment  {

    private static TextView topTextview;
    private static TextView bottomTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.bottom_photo_fragment, container, false);
        topTextview = (TextView) view.findViewById(R.id.topTextView);
        bottomTextView = (TextView) view.findViewById(R.id.bottomTextView);
        return view;
    }

    public void setMemeText(String top, String bottom){
        topTextview.setText(top);
        bottomTextView.setText(bottom);
    }


}
