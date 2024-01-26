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

import com.wolf.fingerpos.AccountPostpaidActivity;
import com.wolf.fingerpos.Dialog.CancelAlertDialog;
import com.wolf.fingerpos.POSApplication;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.database.AccountTable;
import com.wolf.fingerpos.database.PostpaidTable;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class PostpaidFragment extends Fragment implements View.OnClickListener, CancelAlertDialog.CancelAlertDialogListener {
    public final int REQUEST_NEW_POSTPAID = 0x001;
    ListView lvPostpayments;
    public PostpaymentsListAdapter listAdapter;

    private RealmResults<PostpaidTable> resultPostpaids = null;
    private List<AccountTable> accountInfos = new ArrayList<>();
    private int nSelected = -1;

    public static PostpaidFragment newInstance() {
        Bundle bundle = new Bundle();

        PostpaidFragment fragment = new PostpaidFragment();
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
        return inflater.inflate(R.layout.fragment_postpayments, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvPostpayments = (ListView) view.findViewById(R.id.lvPostpayments);
        ViewGroup headerView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.list_header_postpaid, lvPostpayments, false);
        lvPostpayments.addHeaderView(headerView);
        listAdapter = new PostpaymentsListAdapter(getContext());
        lvPostpayments.setAdapter(listAdapter);
        lvPostpayments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        view.findViewById(R.id.btnSettle).setOnClickListener(this);
        view.findViewById(R.id.btnCancel).setOnClickListener(this);

        loadPostpaidInfo();
    }

    private void loadPostpaidInfo() {
        accountInfos.clear();
        nSelected = -1;
        RealmQuery<PostpaidTable> queryPostpaid = POSApplication.mRealmDB.where(PostpaidTable.class);
        resultPostpaids = queryPostpaid.findAll();
        for (int i = 0; i < resultPostpaids.size(); i ++) {
            RealmQuery<AccountTable> queryAccount = POSApplication.mRealmDB.where(AccountTable.class);
            queryAccount.equalTo("id", resultPostpaids.get(i).getAccount_id());
            RealmResults<AccountTable> resultAccounts = queryAccount.findAll();
            AccountTable account_info = null;
            if (resultAccounts.size() != 0) {
                account_info = resultAccounts.get(0);
            }
            accountInfos.add(account_info);
        }
        accountInfos_search.clear();
        accountInfos_search.addAll(accountInfos);
        postpaidInfos_search.clear();
        postpaidInfos_search.addAll(resultPostpaids);

        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || requestCode == REQUEST_NEW_POSTPAID) {
            loadPostpaidInfo();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNew: {
                Intent intent = new Intent(getContext(), AccountPostpaidActivity.class);
                startActivityForResult(intent, REQUEST_NEW_POSTPAID);
                break;
            }
            case R.id.btnSettle: {
                break;
            }
            case R.id.btnCancel: {
                if (nSelected == -1 || (resultPostpaids != null && nSelected >= resultPostpaids.size())) {
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
                PostpaidTable postpaid = resultPostpaids.get(nSelected);
                postpaid.deleteFromRealm();
                loadPostpaidInfo();
            }
        });
    }

    public void refresh() {
        loadPostpaidInfo();
    }

    private List<AccountTable> accountInfos_search = new ArrayList<>();
    private List<PostpaidTable> postpaidInfos_search = new ArrayList<>();
    public void onSearch(String strKey) {
        postpaidInfos_search.clear();
        accountInfos_search.clear();
        for (int i = 0; i < accountInfos.size(); i ++) {
            if (accountInfos.get(i).getId().contains(strKey)) {
                postpaidInfos_search.add(resultPostpaids.get(i));
                accountInfos_search.add(accountInfos.get(i));
            }
            else if (accountInfos.get(i).getFirst_name().contains(strKey)) {
                postpaidInfos_search.add(resultPostpaids.get(i));
                accountInfos_search.add(accountInfos.get(i));
            }
            else if (accountInfos.get(i).getLast_name().contains(strKey)) {
                postpaidInfos_search.add(resultPostpaids.get(i));
                accountInfos_search.add(accountInfos.get(i));
            }
        }

        listAdapter.notifyDataSetChanged();
    }

    public class PostpaymentsListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public PostpaymentsListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (postpaidInfos_search == null)
                return 0;
            return postpaidInfos_search.size();
        }

        @Override
        public Object getItem(int position) {
            return postpaidInfos_search.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_postpaid, parent, false);
                holder = ViewHolder.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bindView(convertView, holder, position);

            return convertView;
        }

        private void bindView(View convertView, ViewHolder viewHolder, int position) {
            PostpaidTable postpaid = postpaidInfos_search.get(position);
            AccountTable accountInfo = accountInfos_search.get(position);
            viewHolder.tvIndex.setText(String.format("%d", position + 1));
            viewHolder.tvID.setText(postpaid.getAccount_id());
            if (accountInfo != null) {
                viewHolder.tvName.setText(accountInfo.getFirst_name() + " " + accountInfo.getLast_name());
                viewHolder.tvType.setText(GlobalVariable.getInstance(getContext()).g_arrayCustomType.get(accountInfo.getType()));
            }
            viewHolder.tvACType.setText(GlobalVariable.getInstance(getContext()).g_arrayAccountType.get(postpaid.getType()));
            viewHolder.tvFrom.setText(Utils.convertDate(postpaid.getFrom(), Utils.DATE_FORMAT));
            viewHolder.tvTo.setText(Utils.convertDate(postpaid.getTo(), Utils.DATE_FORMAT));
            viewHolder.tvMaxAmount.setText(String.format("%.2f", postpaid.getAmount()));
            viewHolder.tvCredits.setText(String.format("%.2f", postpaid.getCredit()));
            viewHolder.tvPayments.setText(String.format("%.2f", postpaid.getPayment()));
            viewHolder.tvBal.setText(String.format("%.2f", postpaid.getCredit() - postpaid.getPayment()));;
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
        TextView tvType;
        TextView tvACType;
        TextView tvFrom;
        TextView tvTo;
        TextView tvMaxAmount;
        TextView tvCredits;
        TextView tvPayments;
        TextView tvBal;

        public static ViewHolder getInstance(View view) {
            ViewHolder holder = new ViewHolder();
            holder.tvIndex = (TextView) view.findViewById(R.id.tvIndex);
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvType = (TextView) view.findViewById(R.id.tvType);
            holder.tvACType = (TextView) view.findViewById(R.id.tvACType);
            holder.tvFrom = (TextView) view.findViewById(R.id.tvFrom);
            holder.tvTo = (TextView) view.findViewById(R.id.tvTo);
            holder.tvMaxAmount = (TextView) view.findViewById(R.id.tvMaxAmount);
            holder.tvCredits = (TextView) view.findViewById(R.id.tvCredits);
            holder.tvPayments = (TextView) view.findViewById(R.id.tvPayments);
            holder.tvBal = (TextView) view.findViewById(R.id.tvBal);

            return holder;
        }
    }
}