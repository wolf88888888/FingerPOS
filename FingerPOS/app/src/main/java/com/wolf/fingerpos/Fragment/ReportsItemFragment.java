package com.wolf.fingerpos.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wolf.fingerpos.ItemDetailActivity;
import com.wolf.fingerpos.POSApplication;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.Type.SoldItem;
import com.wolf.fingerpos.database.MenuTable;
import com.wolf.fingerpos.database.ReceiptTable;
import com.wolf.fingerpos.database.SoldItemTable;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import java.util.ArrayList;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReportsItemFragment extends Fragment {

    ListView lvAll;
    ListView lvItem;
    public SoldAllListAdapter listAllAdapter;
    public SoldItemListAdapter listItemAdapter;
    TextView tvUser, tvDuration;
    TextView tvTotal_stk, tvTotal_sold, tvTotal_bal;
    TextView tvItemDesc, tvItemCode, tvItemTotal;
    private int _nSelected = -1;
    float currentPrice, currentTotalPrice;

    ArrayList<SoldItem> soldItems = new ArrayList<>();
    RealmResults<SoldItemTable> listSoldItems = null;
    public static ReportsItemFragment newInstance() {
        Bundle bundle = new Bundle();

        ReportsItemFragment fragment = new ReportsItemFragment();
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
        return inflater.inflate(R.layout.fragment_reports_item, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvAll = (ListView) view.findViewById(R.id.lvAll);
//        ViewGroup headerView_sold = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.list_header_sold_all, lvAll, false);
//        lvAll.addHeaderView(headerView_sold);
        listAllAdapter = new SoldAllListAdapter(getContext());
        lvAll.setAdapter(listAllAdapter);
        lvAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                if (_nSelected != pos) {
                    view.setBackgroundColor(Color.LTGRAY);
                    _nSelected = pos;
                    listSoldItems = soldItems.get(pos).resultItems;

                    tvItemCode.setText(soldItems.get(pos).code);
                    tvItemDesc.setText(soldItems.get(pos).desc);
                    currentPrice = soldItems.get(pos).price;
                    currentTotalPrice = soldItems.get(pos).sold_value;

                    tvItemTotal.setText(String.format("%.2f", currentTotalPrice));
                }
                else {
                    _nSelected = -1;
                    listSoldItems = null;
                    tvItemCode.setText("");
                    tvItemDesc.setText("");
                    currentPrice = 0.0f;
                    currentTotalPrice = 0.0f;
                }
                listItemAdapter.notifyDataSetChanged();
            }
        });

        lvItem = (ListView) view.findViewById(R.id.lvItem);
//        ViewGroup headerView = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.list_header_sold_item, lvItem, false);
//        lvItem.addHeaderView(headerView);
        listItemAdapter = new SoldItemListAdapter(getContext());
        lvItem.setAdapter(listItemAdapter);

        tvUser = (TextView)view.findViewById(R.id.tvUser);
        tvDuration = (TextView)view.findViewById(R.id.tvDuration);

        tvTotal_stk = (TextView)view.findViewById(R.id.tvTotal_stk);
        tvTotal_sold = (TextView)view.findViewById(R.id.tvTotal_sold);
        tvTotal_bal = (TextView)view.findViewById(R.id.tvTotal_bal);

        tvItemDesc = (TextView)view.findViewById(R.id.tvItemDesc);
        tvItemCode = (TextView)view.findViewById(R.id.tvItemCode);

        tvItemTotal = (TextView)view.findViewById(R.id.tvItemTotal);
        if (_from_duration == -1 || _to_duration == -1)
            return;
        refreshWidget();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static RealmResults<ReceiptTable> _resultTotal;
    public static float _total;
    public static long _from_duration = -1, _to_duration = -1;
    public void setDuration(long from_duration, long to_duration) {
        _from_duration = from_duration;
        _to_duration = to_duration;
        if (tvDuration == null)
            return;

        refreshWidget();
    }

    void refreshWidget() {
        tvDuration.setText(Utils.convertDate(_from_duration, Utils.DATE_TIME_FORMAT) + " ~ " + Utils.convertDate(_to_duration, Utils.DATE_TIME_FORMAT));

        RealmQuery<MenuTable> queryMenus = POSApplication.mRealmDB.where(MenuTable.class);
        RealmResults<MenuTable> resultMenus = queryMenus.findAll();

        //String code, String desc, float price, int qty_stk, int qty_sold, int qty_bal, float stk_value, float sold_value, float bal_value

        float totalStkValue = 0;
        float totalSoldValue = 0;
        float totalBalValue = 0;
        for (int i = 0; i < resultMenus.size(); i ++) {
            int qty_stk = 0, qty_sold = 0;

            RealmQuery<SoldItemTable> querySolds = POSApplication.mRealmDB.where(SoldItemTable.class);
            querySolds.equalTo("code", resultMenus.get(i).getCode());
            querySolds.between("pay_time", _from_duration, _to_duration);
            RealmResults<SoldItemTable> resultSolds = querySolds.findAll();
//            if (resultSolds.size() == 0)
//                continue;

            for (int j = 0; j < resultSolds.size(); j ++) {
                qty_sold += resultSolds.get(j).getQty();
            }

            float price = resultMenus.get(i).getPrice();
            float stk_value = price * qty_stk;
            float sold_value = price * qty_sold;
            float bal_value = stk_value - sold_value;
            SoldItem item = new SoldItem(resultMenus.get(i).getCode(), resultMenus.get(i).getDesc(), price,
                    qty_stk, qty_sold, qty_stk - qty_sold, stk_value, sold_value, bal_value);

            item.resultItems = resultSolds;
            totalStkValue += stk_value;
            totalSoldValue += sold_value;
            totalBalValue += bal_value;

            soldItems.add(item);
        }

        tvTotal_stk.setText(String.format("%.2f", totalStkValue));
        tvTotal_sold.setText(String.format("%.2f", totalSoldValue));
        tvTotal_bal.setText(String.format("%.2f", totalBalValue));
    }

    public class SoldAllListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public SoldAllListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return soldItems.size();
        }

        @Override
        public Object getItem(int position) {
            return soldItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder_Sold_All holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_sold_all, parent, false);
                holder = ViewHolder_Sold_All.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_Sold_All) convertView.getTag();
            }
            bindView(convertView, holder, position);

            return convertView;
        }

        private void bindView(View convertView, ViewHolder_Sold_All viewHolder, int position){
            SoldItem item = soldItems.get(position);

            viewHolder.tvID.setText(String.valueOf(position + 1));
            viewHolder.tvCODE.setText(item.code);
            viewHolder.tvDESCRIPTION.setText(item.desc);
            viewHolder.tvPRICE.setText(String.format("%.2f", item.price));
            viewHolder.tvQTYSTK.setText(String.valueOf(item.qty_stk));
            viewHolder.tvQTYSOLD.setText(String.valueOf(item.qty_sold));
            viewHolder.tvQTYBAL.setText(String.valueOf(item.qty_bal));

            viewHolder.tvSTKVALUE.setText(String.format("%.2f", item.stk_value));
            viewHolder.tvSOLDVALUE.setText(String.format("%.2f", item.sold_value));
            viewHolder.tvBALVALUE.setText(String.format("%.2f", item.bal_value));
            if (_nSelected != position) {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            else {
                convertView.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    public class SoldItemListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public SoldItemListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (listSoldItems == null)
                return 0;
            return listSoldItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listSoldItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder_Sold_Item holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_sold_item, parent, false);
                holder = ViewHolder_Sold_Item.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_Sold_Item) convertView.getTag();
            }
            bindView(holder, position);

            return convertView;
        }

        private void bindView(ViewHolder_Sold_Item viewHolder, int position){
            SoldItemTable item = listSoldItems.get(position);

            viewHolder.tvID.setText(String.valueOf(position + 1));
            viewHolder.tvDATE.setText(Utils.convertDate(item.getPay_time(), Utils.DATE_FORMAT));
            viewHolder.tvTIME.setText(Utils.convertDate(item.getPay_time(), Utils.TIME_FORMAT));
            viewHolder.tvQTY.setText(String.valueOf(item.getQty()));
            viewHolder.tvPRICE.setText(String.format("%.2f", currentPrice));
            viewHolder.tvTOTAL.setText(String.format("%.2f", currentPrice * item.getQty()));

            String receiptID = "REC";
            if (item.getReceipt_id() >= 1000)
                receiptID += String.valueOf(item.getReceipt_id());
            else
                receiptID += String.format("%03d", item.getReceipt_id());
            viewHolder.tvTRANS.setText(receiptID);

            viewHolder.tvTRANS_TYPE.setText(receiptID);

            String strPayMode = GlobalVariable.getInstance(getContext()).g_arrayPayMode.get(item.getPay_type());
            viewHolder.tvTRANS_TYPE.setText(strPayMode);

            viewHolder.tvTYPE_REF.setText(item.getType_ref());
            viewHolder.tvUSER.setText(item.getUser_id());

//            viewHolder.tvSTKVALUE.setText(String.format("%.2f", item.stk_value));
//            viewHolder.tvSOLDVALUE.setText(String.format("%.2f", item.sold_value));
//            viewHolder.tvBALVALUE.setText(String.format("%.2f", item.bal_value));
        }
    }

    static class ViewHolder_Sold_All {
        TextView tvID;
        TextView tvCODE;
        TextView tvDESCRIPTION;
        TextView tvPRICE;
        TextView tvQTYSTK;
        TextView tvQTYSOLD;
        TextView tvQTYBAL;
        TextView tvSTKVALUE;
        TextView tvSOLDVALUE;
        TextView tvBALVALUE;
        public static ViewHolder_Sold_All getInstance(View view){
            ViewHolder_Sold_All holder = new ViewHolder_Sold_All();
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvCODE = (TextView) view.findViewById(R.id.tvCODE);
            holder.tvDESCRIPTION = (TextView) view.findViewById(R.id.tvDESCRIPTION);
            holder.tvPRICE = (TextView) view.findViewById(R.id.tvPRICE);
            holder.tvQTYSTK = (TextView) view.findViewById(R.id.tvQTYSTK);
            holder.tvQTYSOLD = (TextView) view.findViewById(R.id.tvQTYSOLD);
            holder.tvQTYBAL = (TextView) view.findViewById(R.id.tvQTYBAL);
            holder.tvSTKVALUE = (TextView) view.findViewById(R.id.tvSTKVALUE);
            holder.tvSOLDVALUE = (TextView) view.findViewById(R.id.tvSOLDVALUE);
            holder.tvBALVALUE = (TextView) view.findViewById(R.id.tvBALVALUE);
            return holder;
        }
    }

    static class ViewHolder_Sold_Item {
        TextView tvID;
        TextView tvDATE;
        TextView tvTIME;
        TextView tvQTY;
        TextView tvPRICE;
        TextView tvTOTAL;
        TextView tvTRANS;
        TextView tvTRANS_TYPE;
        TextView tvTYPE_REF;
        TextView tvUSER;
        public static ViewHolder_Sold_Item getInstance(View view){
            ViewHolder_Sold_Item holder = new ViewHolder_Sold_Item();
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvDATE = (TextView) view.findViewById(R.id.tvDATE);
            holder.tvTIME = (TextView) view.findViewById(R.id.tvTIME);
            holder.tvQTY = (TextView) view.findViewById(R.id.tvQTY);
            holder.tvPRICE = (TextView) view.findViewById(R.id.tvPRICE);
            holder.tvTOTAL = (TextView) view.findViewById(R.id.tvTOTAL);
            holder.tvTRANS = (TextView) view.findViewById(R.id.tvTRANS);
            holder.tvTRANS_TYPE = (TextView) view.findViewById(R.id.tvTRANS_TYPE);
            holder.tvTYPE_REF = (TextView) view.findViewById(R.id.tvTYPE_REF);
            holder.tvUSER = (TextView) view.findViewById(R.id.tvUSER);
            return holder;
        }
    }
}
