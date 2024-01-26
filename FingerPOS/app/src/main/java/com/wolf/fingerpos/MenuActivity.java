package com.wolf.fingerpos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wolf.fingerpos.Dialog.DeleteAlertDialog;
import com.wolf.fingerpos.adapter.MenuListAdapter;
import com.wolf.fingerpos.database.MenuTable;
import com.wolf.fingerpos.database.SoldItemTable;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MenuActivity extends AppCompatActivity implements DeleteAlertDialog.DeleteAlertDialogListener {
    public final int REQUEST_MENU_MANAGE = 0x001;

    ListView lvMenus;
    public MenuListAdapter listAdapter;
    private RealmResults<MenuTable> resultMenus = null;
    private int nSelected = -1;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        initWidget();
    }

    void initWidget(){
        lvMenus = (ListView) findViewById(R.id.lvMenus);
//        ViewGroup headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.list_header_menu, lvMenus, false);
//        lvMenus.addHeaderView(headerView);
        listAdapter = new MenuListAdapter(this);
        lvMenus.setAdapter(listAdapter);
        lvMenus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                if (nSelected != pos) {
                    view.setBackgroundColor(Color.LTGRAY);
                    nSelected = pos;
                }
                else {
                    nSelected = -1;
                }
            }
        });

        etSearch = (EditText)findViewById(R.id.etSearch);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    onSearch(s.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        loadMenuInfo();
    }

    private void loadMenuInfo() {
        nSelected = -1;
        RealmQuery<MenuTable> queryMenu = POSApplication.mRealmDB.where(MenuTable.class);
        resultMenus = queryMenu.findAll();
        listAdapter.setMenus(resultMenus, nSelected);
        listAdapter.notifyDataSetChanged();
    }

    void onSearch(String strKey) {
        nSelected = -1;
        RealmQuery<MenuTable> queryMenus  = POSApplication.mRealmDB.where(MenuTable.class)
                .contains("code", strKey)
                .or()
                .contains("desc", strKey);

        resultMenus = queryMenus.findAll();
        listAdapter.setMenus(resultMenus, nSelected);
        listAdapter.notifyDataSetChanged();
    }

    public void onBack(View v) {
        finish();
    }

    public void onNew(View v) {
        Intent intent = new Intent(MenuActivity.this, MenuManageActivity.class);

        intent.putExtra(MenuManageActivity.MANAGE_TYPE, MenuManageActivity.NEW);
        startActivityForResult(intent, REQUEST_MENU_MANAGE);
        nSelected = -1;
        etSearch.setText("");
    }

    public void onAmend(View v) {
        if (nSelected == -1 || (resultMenus != null && nSelected >= resultMenus.size())) {
            Toast.makeText(this, "Please select menu item to amend", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MenuActivity.this, MenuManageActivity.class);
        intent.putExtra(MenuManageActivity.MANAGE_TYPE, MenuManageActivity.EDIT);
        intent.putExtra(MenuManageActivity.KEY_CODE, resultMenus.get(nSelected).getCode());
        startActivityForResult(intent, REQUEST_MENU_MANAGE);

        nSelected = -1;
        etSearch.setText("");
    }

    boolean checkMenuUsage(String code){
        RealmQuery<SoldItemTable> querySold = POSApplication.mRealmDB.where(SoldItemTable.class);
        querySold.equalTo("code", code);

        RealmResults<SoldItemTable> resultSolds = querySold.findAll();

        return resultSolds.size() > 0;
    }

    public void onDelete(View v) {
        if (nSelected == -1 || (resultMenus != null && nSelected >= resultMenus.size())) {
            Toast.makeText(this, "Please select menu item to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkMenuUsage(resultMenus.get(nSelected).getCode())){
            Toast.makeText(this, "Current item is already in use, cannot delete it.", Toast.LENGTH_LONG).show();
            return;
        }

        DeleteAlertDialog deleteDialog = new DeleteAlertDialog(this, this);
        deleteDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || requestCode == REQUEST_MENU_MANAGE) {
            loadMenuInfo();
        }
    }

    @Override
    public void onDeleteOk() {

        POSApplication.mRealmDB.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MenuTable menuItem = resultMenus.get(nSelected);
                menuItem.deleteFromRealm();
                loadMenuInfo();
            }
        });
    }
}
