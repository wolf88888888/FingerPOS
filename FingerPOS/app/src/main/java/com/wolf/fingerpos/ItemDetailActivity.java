package com.wolf.fingerpos;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wolf.fingerpos.Fragment.ReportsSummaryFragment;
import com.wolf.fingerpos.database.MenuTable;
import com.wolf.fingerpos.database.ReceiptTable;
import com.wolf.fingerpos.database.SoldItemTable;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String KEY_SHOW_TYPE = "show_type";

    ListView lvAll;
    ListView lvItem;
    public DetailAllListAdapter listAllAdapter;
    public DetailItemListAdapter listItemAdapter;
    TextView tvCashier, tvDuration, tvTotal;
    TextView tvReceiptID, tvPaymentType, tvTransRef, tvTotalItems;

    RealmResults<ReceiptTable> listReceipts = null;
    RealmList<SoldItemTable> listSoldItems = null;
    private int _nSelected = -1;
    private int _paymentType = GlobalVariable.PAYMENT_TYPE.CASH.getValue();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_item);

        _paymentType = getIntent().getIntExtra(KEY_SHOW_TYPE, GlobalVariable.PAYMENT_TYPE.CASH.getValue());
        initWidget();
        setWidgetValue();
    }

    public void initWidget() {
        lvAll = (ListView) findViewById(R.id.lvAll);
//        ViewGroup headerView_sold = (ViewGroup)getLayoutInflater().inflate(R.layout.list_header_detail_all, lvAll, false);
//        lvAll.addHeaderView(headerView_sold);
        listAllAdapter = new DetailAllListAdapter(this);
        lvAll.setAdapter(listAllAdapter);
        lvAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                if (_nSelected != pos) {
                    view.setBackgroundColor(Color.LTGRAY);
                    _nSelected = pos;
                    listSoldItems = listReceipts.get(pos).getListSold();

                    String receiptID = "REC";
                    if (listReceipts.get(pos).getId() >= 1000)
                        receiptID += String.valueOf(listReceipts.get(pos).getId());
                    else
                        receiptID += String.format("%03d", listReceipts.get(pos).getId());


                    tvReceiptID.setText(receiptID);
                    tvTransRef.setText(String.format("%.2f", listReceipts.get(pos).getTrans()));

                    String strPayMode = GlobalVariable.getInstance(ItemDetailActivity.this).g_arrayPayMode.get(listReceipts.get(pos).getPay_type());
                    tvPaymentType.setText(strPayMode);
                    tvTotalItems.setText(String.format("%.2f", listReceipts.get(pos).getTotal() + listReceipts.get(pos).getTrans()));
                }
                else {
                    _nSelected = -1;
                    listSoldItems = null;
                    tvReceiptID.setText("");
                    tvPaymentType.setText("");
                    tvTransRef.setText("-");
                    tvTotalItems.setText("");
                }
                listItemAdapter.notifyDataSetChanged();
            }
        });

        lvItem = (ListView) findViewById(R.id.lvItem);
//        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.list_header_detail_item, lvItem, false);
//        lvItem.addHeaderView(headerView);
        listItemAdapter = new DetailItemListAdapter(this);
        lvItem.setAdapter(listItemAdapter);

        tvCashier = (TextView)findViewById(R.id.tvCashier);
        tvDuration = (TextView)findViewById(R.id.tvDuration);
        tvTotal = (TextView)findViewById(R.id.tvTotal);

        tvReceiptID = (TextView)findViewById(R.id.tvReceiptID);
        tvPaymentType = (TextView)findViewById(R.id.tvPaymentType);
        tvTransRef = (TextView)findViewById(R.id.tvTransRef);
        tvTotalItems = (TextView)findViewById(R.id.tvTotalItems);
    }

    public void setWidgetValue() {
        tvDuration.setText(Utils.convertDate(ReportsSummaryFragment._from_duration, Utils.DATE_TIME_FORMAT) + " ~ " + Utils.convertDate(ReportsSummaryFragment._to_duration, Utils.DATE_TIME_FORMAT));

        if (_paymentType == GlobalVariable.PAYMENT_TYPE.CASH.getValue()) {
            listReceipts = ReportsSummaryFragment._resultCash;
            tvTotal.setText(String.format("%.2f", ReportsSummaryFragment._cash));
        }
        else if (_paymentType == GlobalVariable.PAYMENT_TYPE.CREDIT.getValue()) {
            listReceipts = ReportsSummaryFragment._resultCredit;
            tvTotal.setText(String.format("%.2f", ReportsSummaryFragment._credit));
        }
        else if (_paymentType == GlobalVariable.PAYMENT_TYPE.MPESA.getValue()) {
            listReceipts = ReportsSummaryFragment._resultMpesa;
            tvTotal.setText(String.format("%.2f", ReportsSummaryFragment._Mpesa));
        }
        else if (_paymentType == GlobalVariable.PAYMENT_TYPE.ACC_PREPAY.getValue()) {
            listReceipts = ReportsSummaryFragment._resultAccPre;
            tvTotal.setText(String.format("%.2f", ReportsSummaryFragment._AccPre));
        }
        else if (_paymentType == GlobalVariable.PAYMENT_TYPE.ACC_POSTPAID.getValue()) {
            listReceipts = ReportsSummaryFragment._resultAccPost;
            tvTotal.setText(String.format("%.2f", ReportsSummaryFragment._AccPost));
        }
        else {
            listReceipts = ReportsSummaryFragment._resultTotal;
            tvTotal.setText(String.format("%.2f", ReportsSummaryFragment._total));
        }

        _nSelected = -1;
        listAllAdapter.notifyDataSetChanged();

        tvReceiptID.setText("");
        tvPaymentType.setText("");
        tvTransRef.setText("");
        tvTotalItems.setText("");
    }

    public void onBack(View view) {
        finish();
    }

    public void onPrint(View view) {

    }

    public class DetailAllListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public DetailAllListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (listReceipts == null)
                return 0;
            return listReceipts.size();
        }

        @Override
        public Object getItem(int position) {
            return listReceipts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder_Detail_All holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_detail_all, parent, false);
                holder = ViewHolder_Detail_All.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_Detail_All) convertView.getTag();
            }
            bindView(convertView, holder, position);

            return convertView;
        }

        private void bindView(View convertView, ViewHolder_Detail_All viewHolder, int position){
            ReceiptTable receipt = listReceipts.get(position);

            viewHolder.tvIndex.setText(String.valueOf(position + 1));
            viewHolder.tvDate.setText(Utils.convertDate(receipt.getPay_time(), Utils.DATE_FORMAT));
            viewHolder.tvTime.setText(Utils.convertDate(receipt.getPay_time(), Utils.TIME_FORMAT));
            String receiptID = "REC";
            if (receipt.getId() >= 1000)
                receiptID += String.valueOf(receipt.getId());
            else
                receiptID += String.format("%03d", receipt.getId());

            viewHolder.tvTrans.setText(receiptID);

            float total = receipt.getTotal() + receipt.getTrans();
            viewHolder.tvAmount.setText(String.format("%.2f", total));
            String strPayMode = GlobalVariable.getInstance(ItemDetailActivity.this).g_arrayPayMode.get(receipt.getPay_type());
            viewHolder.tvTransType.setText(strPayMode);

            viewHolder.tvTypeRef.setText(receipt.getType_ref());
            viewHolder.tvUser.setText(receipt.getUser_id());
            if (_nSelected != position) {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            else {
                convertView.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    public class DetailItemListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public DetailItemListAdapter(Context context) {
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
                convertView = inflater.inflate(R.layout.list_item_detail_item, parent, false);
                holder = ViewHolder_Sold_Item.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder_Sold_Item) convertView.getTag();
            }
            bindView(holder, position);

            return convertView;
        }

        private void bindView(ViewHolder_Sold_Item viewHolder, int position){
            SoldItemTable soldItem = listSoldItems.get(position);
            viewHolder.tvIndex.setText(String.valueOf(position + 1));
            viewHolder.tvCode.setText(soldItem.getCode());

            RealmQuery<MenuTable> queryMenus = POSApplication.mRealmDB.where(MenuTable.class);
            queryMenus.equalTo("code", soldItem.getCode());

            RealmResults<MenuTable> menus = queryMenus.findAll();
            float price = 0;
            if (menus.size() >= 1) {
                viewHolder.tvDesc.setText(menus.get(0).getDesc());
                price = menus.get(0).getPrice();
            }
            else {
                viewHolder.tvDesc.setText("** deleted menu **");
            }
            viewHolder.tvQty.setText(String.valueOf(soldItem.getQty()));
            viewHolder.tvPrice.setText(String.format("%.2f", price));
            viewHolder.tvTotal.setText(String.format("%.2f", price * soldItem.getQty()));
        }
    }

    static class ViewHolder_Detail_All {
        TextView tvIndex;
        TextView tvDate;
        TextView tvTime;
        TextView tvTrans;
        TextView tvAmount;
        TextView tvTransType;
        TextView tvTypeRef;
        TextView tvUser;
        public static ViewHolder_Detail_All getInstance(View view){
            ViewHolder_Detail_All holder = new ViewHolder_Detail_All();
            holder.tvIndex = (TextView) view.findViewById(R.id.tvIndex);
            holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            holder.tvTime = (TextView) view.findViewById(R.id.tvTime);
            holder.tvTrans = (TextView) view.findViewById(R.id.tvTrans);
            holder.tvAmount = (TextView) view.findViewById(R.id.tvAmount);
            holder.tvTransType = (TextView) view.findViewById(R.id.tvTransType);
            holder.tvTypeRef = (TextView) view.findViewById(R.id.tvTypeRef);
            holder.tvUser = (TextView) view.findViewById(R.id.tvUser);
            return holder;
        }
    }

    static class ViewHolder_Sold_Item {
        TextView tvIndex;
        TextView tvCode;
        TextView tvDesc;
        TextView tvQty;
        TextView tvPrice;
        TextView tvTotal;
        public static ViewHolder_Sold_Item getInstance(View view){
            ViewHolder_Sold_Item holder = new ViewHolder_Sold_Item();
            holder.tvIndex = (TextView) view.findViewById(R.id.tvIndex);
            holder.tvCode = (TextView) view.findViewById(R.id.tvCode);
            holder.tvDesc = (TextView) view.findViewById(R.id.tvDesc);
            holder.tvQty = (TextView) view.findViewById(R.id.tvQty);
            holder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            holder.tvTotal = (TextView) view.findViewById(R.id.tvTotal);
            return holder;
        }
    }
}
