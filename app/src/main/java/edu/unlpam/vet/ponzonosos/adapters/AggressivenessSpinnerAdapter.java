package edu.unlpam.vet.ponzonosos.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edu.unlpam.vet.ponzonosos.R;
import edu.unlpam.vet.ponzonosos.model.Agresividad;

import java.util.List;

public class AggressivenessSpinnerAdapter extends ArrayAdapter<String>{

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Agresividad> items;
    private final int mResource;

    public AggressivenessSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        Agresividad defaultAggressiveness = new Agresividad();
        defaultAggressiveness.setId(0L);
        defaultAggressiveness.setName("-- Peligrosidad --");
        items.add(0, defaultAggressiveness);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);
        TextView tvName =  view.findViewById(R.id.item_name);
        tvName.setText(items.get(position).getName());
                return view;
    }
}
