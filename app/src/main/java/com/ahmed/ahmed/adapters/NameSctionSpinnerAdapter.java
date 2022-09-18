package com.ahmed.ahmed.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ahmed.ahmed.R;
import com.ahmed.ahmed.model.Section;

import java.util.List;

public class NameSctionSpinnerAdapter extends BaseAdapter {
    private List<Section> sections;

    public NameSctionSpinnerAdapter(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Section getItem(int i) {
        return sections.get(i);
    }

    @Override
    public long getItemId(int i) {
        return sections.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v==null){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.custom_spinner_item,
                            null,false);
        }
        TextView tv_name = v.findViewById(R.id.tv_custom_sp);
        Section e = sections.get(i);

        tv_name.setText(e.getName());

        return v;
    }
}
