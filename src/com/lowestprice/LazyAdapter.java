package com.lowestprice;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LazyAdapter extends BaseAdapter {

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_NUMBER = "number";
    private LayoutInflater inflater = null;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private int rowId, templateId, titleId, descriptionId, numberId;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d,
                       int templateId, int rowId, int titleId, int descriptionId, int numberId) {
        this(a, d);
        this.rowId = rowId;
        this.templateId = templateId;
        this.titleId = titleId;
        this.descriptionId = descriptionId;
        this.numberId = numberId;
    }

    private LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(templateId, null);

        TextView id = (TextView) vi.findViewById(rowId);
        TextView title = (TextView) vi.findViewById(titleId);
        TextView description = (TextView) vi.findViewById(descriptionId);
        TextView number = (TextView) vi.findViewById(numberId);

        HashMap<String, String> date = new HashMap<String, String>();
        date = data.get(position);

        // Setting all values in listview
        id.setText(date.get(KEY_ID));
        title.setText(date.get(KEY_TITLE));
        description.setText(date.get(KEY_DESCRIPTION));
        number.setText(date.get(KEY_NUMBER));
        return vi;
    }
}