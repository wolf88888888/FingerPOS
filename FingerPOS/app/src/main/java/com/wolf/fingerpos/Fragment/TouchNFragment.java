package com.wolf.fingerpos.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolf.fingerpos.R;

public class TouchNFragment extends Fragment {
    public static TouchNFragment newInstance() {
        Bundle bundle = new Bundle();

        TouchNFragment fragment = new TouchNFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_touch_n, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //ListView lvItems = (ListView) view.findViewById(R.id.lvItems);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
