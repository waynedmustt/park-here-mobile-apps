package com.dmustt.mdewantara.floor.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dmustt.mdewantara.floor.schema.FloorSchema;

import java.util.ArrayList;

/**
 * Created by mdewantara on 7/23/17.
 */

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<FloorSchema> floorSchemas;

    public CustomAdapter(Context mContext, ArrayList<FloorSchema> floorSchemas) {
        this.mContext = mContext;
        this.floorSchemas = floorSchemas;
    }

    @Override
    public int getCount() {
        return floorSchemas.size();
    }

    @Override
    public Object getItem(int i) {
        return floorSchemas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
