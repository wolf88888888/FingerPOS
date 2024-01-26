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

import com.wolf.fingerpos.AccountManageActivity;
import com.wolf.fingerpos.Dialog.DeleteAlertDialog;
import com.wolf.fingerpos.POSApplication;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.database.AccountTable;
import com.wolf.fingerpos.database.PrepayTable;
import com.wolf.fingerpos.database.SoldItemTable;
import com.wolf.fingerpos.utils.GlobalVariable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class AccountsInfoFragment extends Fragment implements View.OnClickListener, DeleteAlertDialog.DeleteAlertDialogListener {
    public final int REQUEST_ACCOUNT_MANAGE = 0x001;

    ListView lvAccountInfo;
    private RealmResults<AccountTable> resultAccounts = null;
    private List<PrepayTable> prepayInfos = new ArrayList<>();
    public AccountInfoListAdapter listAdapter;

    private int nSelectedAccount = -1;

    public static AccountsInfoFragment newInstance() {
        Bundle bundle = new Bundle();

        AccountsInfoFragment fragment = new AccountsInfoFragment();
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
        return inflater.inflate(R.layout.fragment_accounts_info, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvAccountInfo = (ListView) view.findViewById(R.id.lvAccountInfo);
        ViewGroup headerView = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.list_header_account_info, lvAccountInfo, false);
        lvAccountInfo.addHeaderView(headerView);
        listAdapter = new AccountInfoListAdapter(getContext());
        lvAccountInfo.setAdapter(listAdapter);

        lvAccountInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                if (nSelectedAccount != pos - 1) {
                    view.setBackgroundColor(Color.LTGRAY);
                    nSelectedAccount = pos - 1;
                }
                else {
                    nSelectedAccount = -1;
                }
            }
        });

        view.findViewById(R.id.btnNew).setOnClickListener(this);
        view.findViewById(R.id.btnAmend).setOnClickListener(this);
        view.findViewById(R.id.btnDelete).setOnClickListener(this);
        loadAccounts();
    }

    private void loadAccounts() {
        prepayInfos.clear();
        nSelectedAccount = -1;
        RealmQuery<AccountTable> queryAccount = POSApplication.mRealmDB.where(AccountTable.class);
        resultAccounts = queryAccount.findAll();
        for (int i = 0; i < resultAccounts.size(); i ++) {
            RealmQuery<PrepayTable> queryPrepaid = POSApplication.mRealmDB.where(PrepayTable.class);
            queryPrepaid.equalTo("account_id", resultAccounts.get(i).getId());
            RealmResults<PrepayTable> resultPrepaid = queryPrepaid.findAll();
            PrepayTable prepay_info = null;
            if (resultPrepaid.size() != 0) {
                prepay_info = resultPrepaid.get(0);
            }
            prepayInfos.add(prepay_info);
        }

        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || requestCode == REQUEST_ACCOUNT_MANAGE) {
            loadAccounts();
        }
    }

    public boolean checkUsage(String account_id){
        RealmQuery<SoldItemTable> querySold = POSApplication.mRealmDB.where(SoldItemTable.class);
        querySold.equalTo("type_ref", account_id);

        RealmResults<SoldItemTable> resultSolds = querySold.findAll();

        return resultSolds.size() > 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNew: {
                Intent intent = new Intent(getContext(), AccountManageActivity.class);
                intent.putExtra(AccountManageActivity.MANAGE_TYPE, AccountManageActivity.NEW_ACCOUNT);
                startActivityForResult(intent, REQUEST_ACCOUNT_MANAGE);

                nSelectedAccount = -1;
                break;
            }
            case R.id.btnAmend: {
                if (nSelectedAccount == -1 || (resultAccounts != null && nSelectedAccount >= resultAccounts.size())) {
                    Toast.makeText(getContext(), "Please select account to edit", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), AccountManageActivity.class);
                intent.putExtra(AccountManageActivity.MANAGE_TYPE, AccountManageActivity.EDIT_ACCOUNT);
                intent.putExtra(AccountManageActivity.KEY_ACCOUNTID, resultAccounts.get(nSelectedAccount).getId());
                startActivityForResult(intent, REQUEST_ACCOUNT_MANAGE);

                nSelectedAccount = -1;
                break;
            }
            case R.id.btnDelete: {
                if (nSelectedAccount == -1 || (resultAccounts != null && nSelectedAccount >= resultAccounts.size())) {
                    Toast.makeText(getContext(), "Please select user to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkUsage(resultAccounts.get(nSelectedAccount).getId())){
                    Toast.makeText(getContext(), "Cannot delete permanently, current user is used already.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DeleteAlertDialog dialog = new DeleteAlertDialog(getContext(), this);
                dialog.show();
                break;
            }
        }
    }

    @Override
    public void onDeleteOk() {
        POSApplication.mRealmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AccountTable account = resultAccounts.get(nSelectedAccount);
                account.deleteFromRealm();
                loadAccounts();
            }
        });
    }

    public void refresh() {
        loadAccounts();
    }

    public void onSearch(String strKey) {
        prepayInfos.clear();
        nSelectedAccount = -1;
        RealmQuery<AccountTable> queryAccount = POSApplication.mRealmDB.where(AccountTable.class)
                .contains("first_name", strKey)
                .or()
                .contains("last_name", strKey)
                .or()
                .contains("id", strKey);
        resultAccounts = queryAccount.findAll();
        for (int i = 0; i < resultAccounts.size(); i ++) {
            RealmQuery<PrepayTable> queryPrepaid = POSApplication.mRealmDB.where(PrepayTable.class);
            queryPrepaid.equalTo("account_id", resultAccounts.get(i).getId());
            RealmResults<PrepayTable> resultPrepaid = queryPrepaid.findAll();
            PrepayTable prepay_info = null;
            if (resultPrepaid.size() != 0) {
                prepay_info = resultPrepaid.get(0);
            }
            prepayInfos.add(prepay_info);
        }

        listAdapter.notifyDataSetChanged();
    }

    public class AccountInfoListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public AccountInfoListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (resultAccounts == null)
                return 0;
            return resultAccounts.size();
        }

        @Override
        public Object getItem(int position) {
            return resultAccounts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_account_info, parent, false);
                holder = ViewHolder.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bindView(convertView, holder, position);

            return convertView;
        }

        private void bindView(View convertView, ViewHolder viewHolder, int position){
            viewHolder.tvIndex.setText(String.format("%d", position + 1));
            viewHolder.tvID.setText(resultAccounts.get(position).getId());
            viewHolder.tvName.setText(resultAccounts.get(position).getFirst_name() + " " + resultAccounts.get(position).getLast_name());
            viewHolder.tvType.setText(GlobalVariable.getInstance(getContext()).g_arrayCustomType.get(resultAccounts.get(position).getType()));
            viewHolder.tvStatus.setText(GlobalVariable.getInstance(getContext()).g_arrayStatus.get(resultAccounts.get(position).getStatus()));

            viewHolder.tvFinger.setText("YES");
            viewHolder.tvVein.setText("YES");
            viewHolder.tvReason.setText("");

            if (prepayInfos.get(position) != null) {
                viewHolder.tvACType.setText(GlobalVariable.getInstance(getContext()).g_arrayAccountType.get(prepayInfos.get(position).getType()));
                viewHolder.tvBal.setText(String.format("%.2f", prepayInfos.get(position).getAmount()));
            }
            else {
                viewHolder.tvACType.setText(GlobalVariable.getInstance(getContext()).g_arrayAccountType.get(0));
                viewHolder.tvBal.setText("0.00");
            }
            if (nSelectedAccount != position) {
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
        TextView tvStatus;
        TextView tvReason;
        TextView tvACType;
        TextView tvFinger;
        TextView tvVein;
        TextView tvBal;
        public static ViewHolder getInstance(View view){
            ViewHolder holder = new ViewHolder();
            holder.tvIndex = (TextView) view.findViewById(R.id.tvIndex);
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvType = (TextView) view.findViewById(R.id.tvType);
            holder.tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            holder.tvReason = (TextView) view.findViewById(R.id.tvReason);
            holder.tvACType = (TextView) view.findViewById(R.id.tvACType);
            holder.tvFinger = (TextView) view.findViewById(R.id.tvFinger);
            holder.tvVein = (TextView) view.findViewById(R.id.tvVein);
            holder.tvBal = (TextView) view.findViewById(R.id.tvBal);

            return holder;
        }
    }
}
