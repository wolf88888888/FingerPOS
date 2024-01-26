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

import com.wolf.fingerpos.Dialog.DeleteAlertDialog;
import com.wolf.fingerpos.POSApplication;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.UserManageActivity;
import com.wolf.fingerpos.database.UserTable;
import com.wolf.fingerpos.utils.GlobalVariable;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class UsersSettingFragment extends Fragment implements View.OnClickListener, DeleteAlertDialog.DeleteAlertDialogListener {
    public final int REQUEST_USER_MANAGE = 0x001;

    ListView lvUsers;
//    ArrayList<UserInfoItem> items = new ArrayList<>();
    public UserListAdapter listAdapter;
    private RealmResults<UserTable> resultUsers = null;

    private int nSelectedUser = -1;

    public static UsersSettingFragment newInstance() {
        Bundle bundle = new Bundle();

        UsersSettingFragment fragment = new UsersSettingFragment();
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
        return inflater.inflate(R.layout.fragment_user_setting, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvUsers = (ListView) view.findViewById(R.id.lvUsers);
        ViewGroup headerView = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.list_header_user, lvUsers, false);
        lvUsers.addHeaderView(headerView);
        listAdapter = new UserListAdapter(getContext());
        lvUsers.setAdapter(listAdapter);

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                if (nSelectedUser != pos - 1) {
                    view.setBackgroundColor(Color.LTGRAY);
                    nSelectedUser = pos - 1;
                }
                else {
                    nSelectedUser = -1;
                }
            }
        });

        view.findViewById(R.id.btnNew).setOnClickListener(this);
        view.findViewById(R.id.btnAmend).setOnClickListener(this);
        view.findViewById(R.id.btnDelete).setOnClickListener(this);
        view.findViewById(R.id.btnPermission).setOnClickListener(this);

        loadUsers();
    }

    private void loadUsers() {
        nSelectedUser = -1;
        RealmQuery<UserTable> queryUsers = POSApplication.mRealmDB.where(UserTable.class);
        resultUsers = queryUsers.findAll();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || requestCode == REQUEST_USER_MANAGE) {
            loadUsers();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNew: {
                Intent intent = new Intent(getContext(), UserManageActivity.class);
                intent.putExtra(UserManageActivity.MANAGE_TYPE, UserManageActivity.NEW_USER);
                startActivityForResult(intent, REQUEST_USER_MANAGE);

                nSelectedUser = -1;
                break;
            }
            case R.id.btnAmend: {
                if (nSelectedUser == -1 || (resultUsers != null && nSelectedUser >= resultUsers.size())) {
                    Toast.makeText(getContext(), "Please select user to edit", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getContext(), UserManageActivity.class);
                intent.putExtra(UserManageActivity.MANAGE_TYPE, UserManageActivity.EDIT_USER);
                intent.putExtra(UserManageActivity.KEY_USERID, resultUsers.get(nSelectedUser).getId());
                startActivityForResult(intent, REQUEST_USER_MANAGE);

                nSelectedUser = -1;
                break;
            }
            case R.id.btnDelete: {
                if (nSelectedUser == -1 || (resultUsers != null && nSelectedUser >= resultUsers.size())) {
                    Toast.makeText(getContext(), "Please select user to delete", Toast.LENGTH_SHORT).show();
                    return;
                }
                DeleteAlertDialog dialog = new DeleteAlertDialog(getContext(), this);
                dialog.show();
                break;
            }
            case R.id.btnPermission: {
//                Toast.makeText(getContext(), String.format("%d", lvUsers.getSelectedItemPosition()), Toast.LENGTH_LONG).show();
//                DeleteAlertDialog dialog = new DeleteAlertDialog(getContext());
//                dialog.show();
                break;
            }
        }
    }

    @Override
    public void onDeleteOk() {
        POSApplication.mRealmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                UserTable user = resultUsers.get(nSelectedUser);
                user.deleteFromRealm();
                loadUsers();
            }
        });
    }

    public void onSearch(String strKey) {
        nSelectedUser = -1;
        RealmQuery<UserTable> queryUsers = POSApplication.mRealmDB.where(UserTable.class)
                .contains("id", strKey)
                .or()
                .contains("name", strKey);
        resultUsers = queryUsers.findAll();
        listAdapter.notifyDataSetChanged();
    }

    public class UserListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public UserListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (resultUsers == null)
                return 0;
            return resultUsers.size();
        }

        @Override
        public Object getItem(int position) {
            return resultUsers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_user, parent, false);
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
            viewHolder.tvID.setText(resultUsers.get(position).getId());
            viewHolder.tvName.setText(resultUsers.get(position).getName());

            viewHolder.tvType.setText(GlobalVariable.getInstance(getContext()).g_arrayUserType.get(resultUsers.get(position).getType()));
            viewHolder.tvStatus.setText(GlobalVariable.getInstance(getContext()).g_arrayStatus.get(resultUsers.get(position).getStatus()));
            if (resultUsers.get(position).getSuper_user()) {
                viewHolder.tvSuper.setText("YES");
            }
            else {
                viewHolder.tvSuper.setText(resultUsers.get(position).getId());
                viewHolder.tvSuper.setText("NO");
            }
            if (nSelectedUser != position) {
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
        TextView tvSuper;
        public static ViewHolder getInstance(View view){
            ViewHolder holder = new ViewHolder();
            holder.tvIndex = (TextView) view.findViewById(R.id.tvIndex);
            holder.tvID = (TextView) view.findViewById(R.id.tvID);
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvType = (TextView) view.findViewById(R.id.tvType);
            holder.tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            holder.tvSuper = (TextView) view.findViewById(R.id.tvSuper);

            return holder;
        }
    }
}
