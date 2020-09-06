package com.example.tb.ui.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tb.R;

import java.util.List;

public class ListAdapter extends BaseAdapter {
        private List<String> list;
        private Context context;
        public ListAdapter(Context context, List<String> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = View.inflate(context, R.layout.list_item, null);
            }
            TextView num = (TextView) convertView.findViewById(R.id.num);
            num.setText(list.get(position));
            return convertView;
        }
}
