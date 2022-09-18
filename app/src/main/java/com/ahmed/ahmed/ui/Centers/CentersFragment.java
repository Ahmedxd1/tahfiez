package com.ahmed.ahmed.ui.Centers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ahmed.ahmed.databinding.FragmentCentersBinding;

public class CentersFragment extends Fragment {
    Context context;

    private FragmentCentersBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static CentersFragment newInstance() {
        CentersFragment fragment = new CentersFragment();
        return fragment;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CentersViewModel centersViewModel =
                new ViewModelProvider(this).get(CentersViewModel.class);

        binding = FragmentCentersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.floatingAddCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,AddCenterActivity.class);
                startActivity(intent);
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentCentersBinding binding = FragmentCentersBinding.inflate(getLayoutInflater());

    }
}