package com.fleuret.memegenerator;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopSectionFragment extends Fragment {

    private static EditText bottomEditText;
    private static EditText topEditText;

    TopSectionListener activityCommader;

    public interface TopSectionListener{
        void createMeme(String top, String Bottom);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            activityCommader = (TopSectionListener) context;
        }catch (ClassCastException cce){
            throw new ClassCastException(cce.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.top_section_fragment, container, false);

        topEditText = (EditText) view.findViewById(R.id.topEditText); // use view
        bottomEditText = (EditText) view.findViewById(R.id.bottomEditText);
        final Button button = (Button) view.findViewById(R.id.button);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonClicked(v);
                    }
                }
        );

        return view;
    }

    public void buttonClicked(View v){
        activityCommader.createMeme(topEditText.getText().toString(),bottomEditText.getText().toString());
    }


}
