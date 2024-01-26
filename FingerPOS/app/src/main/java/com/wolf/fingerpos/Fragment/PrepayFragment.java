package com.wolf.fingerpos.Fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.wolf.fingerpos.AccountPrepayActivity;
import com.wolf.fingerpos.Dialog.CancelAlertDialog;
import com.wolf.fingerpos.POSApplication;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.database.AccountTable;
import com.wolf.fingerpos.database.PrepayTable;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class PrepayFragment extends Fragment implements View.OnClickListener, CancelAlertDialog.CancelAlertDialogListener {
    public final int REQUEST_NEW_PREPAY = 0x001;
    ListView lvPrepay;
    public PrepaymentsListAdapter listAdapter;

    private RealmResults<PrepayTable> resultPrepays = null;
    private List<AccountTable> accountInfos = new ArrayList<>();
    private int nSelected = -1;

    public static PrepayFragment newInstance() {
        Bundle bundle = new Bundle();

        PrepayFragment fragment = new PrepayFragment();
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
        return inflater.inflate(R.layout.fragment_prepayments, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvPrepay = (ListView) view.findViewById(R.id.lvPrepayments);
        ViewGroup headerView = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.list_header_prepay, lvPrepay, false);
        lvPrepay.addHeaderView(headerView);
        listAdapter = new PrepaymentsListAdapter(getContext());
        lvPrepay.setAdapter(listAdapter);
        lvPrepay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                if (nSelected != pos - 1) {
                    view.setBackgroundColor(Color.LTGRAY);
                    nSelected = pos - 1;
                }
                else {
                    nSelected = -1;
                }
            }
        });

        view.findViewById(R.id.btnNew).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);
        loadPrepayInfo();
    }

    private void loadPrepayInfo() {
        accountInfos.clear();
        nSelected = -1;
        RealmQuery<PrepayTable> queryPrepay = POSApplication.mRealmDB.where(PrepayTable.class);
        resultPrepays = queryPrepay.findAll();
        for (int i = 0; i < resultPrepays.size(); i ++) {
            RealmQuery<AccountTable> queryAccount = POSApplication.mRealmDB.where(AccountTable.class);
            queryAccount.equalTo("id", resultPrepays.get(i).getAccount_id());
            RealmResults<AccountTable> resultAccounts = queryAccount.findAll();
            AccountTable account_info = null;
            if (resultAccounts.size() != 0) {
                account_info = resultAccounts.get(0);
            }
            accountInfos.add(account_info);
        }

        accountInfos_search.clear();
        accountInfos_search.addAll(accountInfos);
        prepayInfos_search.clear();
        prepayInfos_search.addAll(resultPrepays);

        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || requestCode == REQUEST_NEW_PREPAY) {
            loadPrepayInfo();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNew: {
                Intent intent = new Intent(getContext(), AccountPrepayActivity.class);
                startActivityForResult(intent, REQUEST_NEW_PREPAY);
                break;
            }
            case R.id.btnCancel: {
                if (nSelected == -1 || (resultPrepays != null && nSelected >= resultPrepays.size())) {
                    Toast.makeText(getContext(), "Please select user to delete", Toast.LENGTH_SHORT).show();
                    return;
                }
                CancelAlertDialog dialog = new CancelAlertDialog(getContext(), this);
                dialog.show();
                break;
            }
        }
    }

    @Override
    public void onCancelOk() {
        POSApplication.mRealmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PrepayTable prepay = resultPrepays.get(nSelected);
                prepay.deleteFromRealm();
                loadPrepayInfo();
            }
        });
    }

    public void refresh() {
        loadPrepayInfo();
    }

    private List<AccountTable> accountInfos_search = new ArrayList<>();
    private List<PrepayTable> prepayInfos_search = new ArrayList<>();
    public void onSearch(String strKey) {
        prepayInfos_search.clear();
        accountInfos_search.clear();
        for (int i = 0; i < accountInfos.size(); i ++) {
            if (accountInfos.get(i).getId().contains(strKey)) {
                prepayInfos_search.add(resultPrepays.get(i));
                accountInfos_search.add(accountInfos.get(i));
            }
            else if (accountInfos.get(i).getFirst_name().contains(strKey)) {
                prepayInfos_search.add(resultPrepays.get(i));
                accountInfos_search.add(accountInfos.get(i));
            }
            else if (accountInfos.get(i).getLast_name().contains(strKey)) {
                prepayInfos_search.add(resultPrepays.get(i));
                accountInfos_search.add(accountInfos.get(i));
            }
        }

        listAdapter.notifyDataSetChanged();
    }

    public class PrepaymentsListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public PrepaymentsListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (prepayInfos_search == null)
                return 0;
            return prepayInfos_search.size();
        }

        @Override
        public Object getItem(int position) {
            return prepayInfos_search.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_prepay, parent, false);
                holder = ViewHolder.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bindView(convertView, holder, position);

            return convertView;
        }

        private void bindView(View convertView, ViewHolder viewHolder, int position){
            PrepayTable prepay = prepayInfos_search.get(position);
            viewHolder.tvIndex.setText(String.format("%d", position + 1));
            viewHolder.tvID.setText(prepay.getAccount_id());
            if (accountInfos_search.get(position) != null) {
                viewHolder.tvName.setText(accountInfos_search.get(position).getFirst_name() + " " + accountInfos_search.get(position).getLast_name());
            }
            viewHolder.tvACType.setText(GlobalVariable.getInstance(getContext()).g_arrayAccountType.get(prepay.getType()));
            viewHolder.tvFrom.setText(Utils.convertDate(prepay.getFrom(), Utils.DATE_FORMAT));
            viewHolder.tvTo.setText(Utils.convertDate(prepay.getTo(), Utils.DATE_FORMAT));
            viewHolder.tvAmount.setText(String.format("%.2f", prepay.getAmount()));
            viewHolder.tvPayMode.setText("CASH");
            viewHolder.tvRef.setText("");
            viewHolder.tvDate.setText(Utils.convertDate(prepay.getCreate_date(), Utils.DATE_FORMAT));
            viewHolder.tvReceipt.setText("");
            if (nSelected != position) {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
            else {
                convertView.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    static class ViewHolder {
        TextView tvIndex;
        TextView tvID;
        TextView tvName;
        TextView tvACType;
        TextView tvFrom;
        TextView tvTo;
        TextView tvAmount;
        TextView tvPayMode;
        TextView tvRef;
        TextView tvDate;
        TextView tvReceipt;
        public static ViewHolder getInstance(View view){
            ViewHolder holder = new ViewHolder();
            holder.tvIndex = (TextView) view.findViewById(R.id.tvIndex);
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvACType = (TextView) view.findViewById(R.id.tvACType);
            holder.tvFrom = (TextView) view.findViewById(R.id.tvFrom);
            holder.tvTo = (TextView) view.findViewById(R.id.tvTo);
            holder.tvAmount = (TextView) view.findViewById(R.id.tvAmount);
            holder.tvPayMode = (TextView) view.findViewById(R.id.tvPayMode);
            holder.tvRef = (TextView) view.findViewById(R.id.tvRef);
            holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            holder.tvReceipt = (TextView) view.findViewById(R.id.tvReceipt);

            return holder;
        }
    }
}
