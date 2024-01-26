package com.wolf.fingerpos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wolf.fingerpos.R;
import com.wolf.fingerpos.database.MenuTable;

import io.realm.RealmResults;

public class MenuListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private RealmResults<MenuTable> resultMenus = null;
    private int nSelected = -1;

    public MenuListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setMenus(RealmResults<MenuTable> resultMenus, int nSelected){
        this.resultMenus = resultMenus;
        this.nSelected = nSelected;
    }

    @Override
    public int getCount() {
        if (resultMenus == null)
            return 0;
        return resultMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return resultMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_menu, parent, false);
            holder = ViewHolder.getInstance(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindView(convertView, holder, position);

        return convertView;
    }

    private void bindView(View convertView, ViewHolder viewHolder, int position){

        MenuTable menu = resultMenus.get(position);
        viewHolder.tvID.setText(String.format("%d", position + 1));
        viewHolder.tvCODE.setText(String.valueOf(menu.getCode()));
        viewHolder.tvDESCRIPTIOIN.setText(menu.getDesc());
        viewHolder.tvPRICE.setText(String.format("%.2f", menu.getPrice()));
        viewHolder.tvMON.setText(menu.getMon()?"YES":"NO");
        viewHolder.tvTUE.setText(menu.getTue()?"YES":"NO");
        viewHolder.tvWED.setText(menu.getWed()?"YES":"NO");
        viewHolder.tvTHU.setText(menu.getThu()?"YES":"NO");
        viewHolder.tvFRI.setText(menu.getFri()?"YES":"NO");
        viewHolder.tvSAT.setText(menu.getSat()?"YES":"NO");
        viewHolder.tvSUN.setText(menu.getSun()?"YES":"NO");
        if (nSelected != position) {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        else {
            convertView.setBackgroundColor(Color.LTGRAY);
        }
    }

    static class ViewHolder {
        TextView tvID;
        TextView tvCODE;
        TextView tvDESCRIPTIOIN;
        TextView tvPRICE;
        TextView tvMON;
        TextView tvTUE;
        TextView tvWED;
        TextView tvTHU;
        TextView tvFRI;
        TextView tvSAT;
        TextView tvSUN;
        public static ViewHolder getInstance(View view){
            ViewHolder holder = new ViewHolder();
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvCODE = (TextView) view.findViewById(R.id.tvCODE);
            holder.tvDESCRIPTIOIN = (TextView) view.findViewById(R.id.tvDESCRIPTIOIN);
            holder.tvPRICE = (TextView) view.findViewById(R.id.tvPRICE);
            holder.tvMON = (TextView) view.findViewById(R.id.tvMON);
            holder.tvTUE = (TextView) view.findViewById(R.id.tvTUE);
            holder.tvWED = (TextView) view.findViewById(R.id.tvWED);
            holder.tvTHU = (TextView) view.findViewById(R.id.tvTHU);
            holder.tvFRI = (TextView) view.findViewById(R.id.tvFRI);
            holder.tvSAT = (TextView) view.findViewById(R.id.tvSAT);
            holder.tvSUN = (TextView) view.findViewById(R.id.tvSUN);
            return holder;
        }
    }
}

