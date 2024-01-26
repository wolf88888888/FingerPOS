package com.wolf.fingerpos.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wolf.fingerpos.Dialog.AccountDetailDialog;
import com.wolf.fingerpos.Dialog.CashDetailDialog;
import com.wolf.fingerpos.Dialog.MPESADetailDialog;
import com.wolf.fingerpos.POSApplication;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.Type.SalesItem;
import com.wolf.fingerpos.adapter.MenuListAdapter;
import com.wolf.fingerpos.adapter.MenuListAdapter_payn;
import com.wolf.fingerpos.database.AccountTable;
import com.wolf.fingerpos.database.MenuTable;
import com.wolf.fingerpos.database.PostpaidTable;
import com.wolf.fingerpos.database.PrepayTable;
import com.wolf.fingerpos.database.ReceiptTable;
import com.wolf.fingerpos.database.Settings;
import com.wolf.fingerpos.database.SoldItemTable;
import com.wolf.fingerpos.utils.GlobalVariable;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class PayNFragment extends Fragment implements View.OnClickListener, CashDetailDialog.CashDetailDialogListener, MPESADetailDialog.MPESADetailDialogListener, AccountDetailDialog.AccountDetailDialogListener {

    ListView lvItems;
    ArrayList<SalesItem> items = new ArrayList<>();
    public SalesListAdapter listAdapter;

    TextView tvReceipt;
    TextView tvAccountID, tvAccountName, tvAccountType;
    TextView tvSummaryTime;
    TextView tvBalanceBefore, tvAccountTrans, tvBalanceAfter;
    TextView tvTotal, tvTransCharge, tvBalanceDue;
    TextView tvBalanceDue_, tvAmountTendered, tvChange;

    TextView tvInput;
    String _strInput = "";

    Button btnNum0, btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9;
    Button btnDot, btnKeyC, btnMul, btnCash, btnCredit, btnMPESA, btnAccount;
    Button btnNew;
    ImageButton btnEnter;
    ListView lvMenu;
    MenuListAdapter_payn mAdapter_menu;
    EditText etSearch;

    float _fTotal = 0.0f;
    float _fTrans = 0.0f;

    int nStatue_Saved = -1; //0 cash, 1 mpesa, 2 account, 3 credit;

    public static PayNFragment newInstance() {
        Bundle bundle = new Bundle();

        PayNFragment fragment = new PayNFragment();
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
        return inflater.inflate(R.layout.fragment_pay_n, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        lvItems = (ListView) view.findViewById(R.id.lvItems);
        ViewGroup headerView = (ViewGroup)getActivity().getLayoutInflater().inflate(R.layout.list_header_sales, lvItems, false);
        lvItems.addHeaderView(headerView);
        listAdapter = new SalesListAdapter(getContext());
        lvItems.setAdapter(listAdapter);

        initWidget(view);
    }

    void initWidget(View view) {
        tvReceipt = view.findViewById(R.id.tvReceipt);
        tvAccountID = view.findViewById(R.id.tvAccountID);
        tvAccountName = view.findViewById(R.id.tvAccountName);
        tvAccountType = view.findViewById(R.id.tvAccountType);

        tvSummaryTime = view.findViewById(R.id.tvSummaryTime);

        tvBalanceBefore = view.findViewById(R.id.tvBalanceBefore);
        tvAccountTrans = view.findViewById(R.id.tvAccountTrans);
        tvBalanceAfter = view.findViewById(R.id.tvBalanceAfter);

        tvTotal = view.findViewById(R.id.tvTotal);
        tvTransCharge = view.findViewById(R.id.tvTransCharge);
        tvBalanceDue = view.findViewById(R.id.tvBalanceDue);

        tvBalanceDue_ = view.findViewById(R.id.tvBalanceDue_);
        tvAmountTendered = view.findViewById(R.id.tvAmountTendered);
        tvChange = view.findViewById(R.id.tvChange);

        tvInput = view.findViewById(R.id.tvInput);

        btnCash = view.findViewById(R.id.btnCash);
        btnCash.setOnClickListener(this);
        btnCredit = view.findViewById(R.id.btnCredit);
        btnCredit.setOnClickListener(this);
        btnMPESA = view.findViewById(R.id.btnMPESA);
        btnMPESA.setOnClickListener(this);
        btnAccount = view.findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(this);

        btnNum0 = view.findViewById(R.id.btnNum0);
        btnNum0.setOnClickListener(this);
        btnNum1 = view.findViewById(R.id.btnNum1);
        btnNum1.setOnClickListener(this);
        btnNum2 = view.findViewById(R.id.btnNum2);
        btnNum2.setOnClickListener(this);
        btnNum3 = view.findViewById(R.id.btnNum3);
        btnNum3.setOnClickListener(this);
        btnNum4 = view.findViewById(R.id.btnNum4);
        btnNum4.setOnClickListener(this);
        btnNum5 = view.findViewById(R.id.btnNum5);
        btnNum5.setOnClickListener(this);
        btnNum6 = view.findViewById(R.id.btnNum6);
        btnNum6.setOnClickListener(this);
        btnNum7 = view.findViewById(R.id.btnNum7);
        btnNum7.setOnClickListener(this);
        btnNum8 = view.findViewById(R.id.btnNum8);
        btnNum8.setOnClickListener(this);
        btnNum9 = view.findViewById(R.id.btnNum9);
        btnNum9.setOnClickListener(this);
        btnNum0 = view.findViewById(R.id.btnNum0);
        btnNum0.setOnClickListener(this);
        btnDot = view.findViewById(R.id.btnDot);
        btnDot.setOnClickListener(this);
        btnKeyC = view.findViewById(R.id.btnKeyC);
        btnKeyC.setOnClickListener(this);
        btnMul = view.findViewById(R.id.btnMul);
        btnMul.setOnClickListener(this);
        btnEnter = view.findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(this);

        btnNew = view.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(this);
        resetWidget();

        lvMenu = (ListView)view.findViewById(R.id.lvMenu);
        mAdapter_menu = new MenuListAdapter_payn(getContext());
        mAdapter_menu.setMenus(resultMenus);
        lvMenu.setAdapter(mAdapter_menu);
        etSearch = (EditText)view.findViewById(R.id.etSearch);

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
    }

    RealmResults<MenuTable> resultMenus = null;
    void onSearch(String strKey) {
        RealmQuery<MenuTable> queryMenus  = POSApplication.mRealmDB.where(MenuTable.class)
                .contains("code", strKey)
                .or()
                .contains("desc", strKey);

        resultMenus = queryMenus.findAll();
        mAdapter_menu.setMenus(resultMenus);
        mAdapter_menu.notifyDataSetChanged();
    }

    void resetWidget() {
        items.clear();
        listAdapter.notifyDataSetChanged();
        tvReceipt.setText("");

        tvAccountID.setText("");
        tvAccountName.setText("");
        tvAccountType.setText("");

        tvBalanceBefore.setText("0.00");
        tvAccountTrans.setText("0.00");
        tvBalanceAfter.setText("0.00");

        tvTotal.setText("0.00");
        tvTransCharge.setText("0.00");
        tvBalanceDue.setText("0.00");

        tvBalanceDue_.setText("0.00");
        tvAmountTendered.setText("0.00");
        tvChange.setText("0.00");

        tvInput.setText("");

        _fTotal = 0.0f;
        _fTrans = 0.0f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void updateEditView() {
        tvInput.setText(_strInput);
    }

    //-1:empty, 0:invalid input, 1:ok, -2:cannot find menuitem,
    private int parseInput() {
        if (_strInput.isEmpty()) {
            Toast.makeText(getContext(), "Please input menu info first.", Toast.LENGTH_SHORT).show();
            return -1;
        }

        int index = _strInput.indexOf("*");
        String strCode;
        String strCnt;
        if (index == -1) {
            strCode = _strInput;
            strCnt = "1";
        }
        else if (index == 0) {
            Toast.makeText(getContext(), "Please input correctly.", Toast.LENGTH_SHORT).show();
            return 0;
        }
        else if (index == _strInput.length() - 1) {
            strCode = _strInput.substring(0, index);
            strCnt = "1";
        }
        else {
            strCode = _strInput.substring(0, index);
            strCnt = _strInput.substring(index + 1, _strInput.length());
        }

        int cnt = 1;
        try {
            int code = Integer.valueOf(strCode);
            cnt = Integer.valueOf(strCnt);
        }catch (NumberFormatException ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), "Please input correctly.", Toast.LENGTH_SHORT).show();
            return 0;
        }

        RealmQuery<MenuTable> queryMenus = POSApplication.mRealmDB.where(MenuTable.class);
        queryMenus.equalTo("code", strCode);
        RealmResults<MenuTable> resultMenus = queryMenus.findAll();
        if (resultMenus.size() == 0) {
            Toast.makeText(getContext(), "Cannot find item on menu. Please try again.", Toast.LENGTH_LONG).show();
            return -2;
        }
        MenuTable menu = resultMenus.get(0);
        SalesItem item = new SalesItem(menu.getCode(), menu.getDesc(), cnt, menu.getPrice(), cnt * menu.getPrice());
        this.items.add(item);
        listAdapter.notifyDataSetChanged();

        _fTotal += cnt * menu.getPrice();

        tvTotal.setText(String.format("%.2f", _fTotal));
        tvTransCharge.setText(String.format("%.2f", _fTrans));
        tvBalanceDue.setText(String.format("%.2f", _fTotal + _fTrans));

        _strInput = "";
        tvInput.setText("");
        return 1;
    }

    @Override
    public void onClick(View view) {
        String strLast;
        switch (view.getId()) {
            case R.id.btnNum0:
                _strInput += "0";
                break;
            case R.id.btnNum1:
                _strInput += "1";
                break;
            case R.id.btnNum2:
                _strInput += "2";
                break;
            case R.id.btnNum3:
                _strInput += "3";
                break;
            case R.id.btnNum4:
                _strInput += "4";
                break;
            case R.id.btnNum5:
                _strInput += "5";
                break;
            case R.id.btnNum6:
                _strInput += "6";
                break;
            case R.id.btnNum7:
                _strInput += "7";
                break;
            case R.id.btnNum8:
                _strInput += "8";
                break;
            case R.id.btnNum9:
                _strInput += "9";
                break;
            case R.id.btnKeyC:
                if (_strInput.length() < 1)
                    return;
                _strInput = _strInput.substring(0, _strInput.length() - 1);
                break;
            case R.id.btnDot:
//                if (_strInput.length() <= 0)
//                    return;
//                strLast = _strInput.substring(_strInput.length() - 1, _strInput.length());
//                if (strLast.equals("*") || strLast.equals("."))
//                    return;
//                _strInput += ".";
                break;
            case R.id.btnMul:
                if (_strInput.length() <= 0) {
//                    return;
                    _strInput += "*";
                }
                else if (_strInput.length() == 1 && _strInput.equals("*")){
                    String strPayment = "";
                    switch (nStatue_Saved) {
                        case -1:
                            _strInput = "";
                            tvInput.setText("");
                            Toast.makeText(getContext(), "You didn't pay current yet.", Toast.LENGTH_LONG).show();
                            return;
                        case 0:
                            strPayment = "Cash";
                            break;
                        case 1:
                            strPayment = "MPESA";
                            break;
                        case 2:
                            strPayment = "Account";
                            break;
                        case 3:
                            strPayment = "Credit";
                            break;
                    }
                    Toast.makeText(getContext(), "current receipt is already paid via " + strPayment + ".", Toast.LENGTH_LONG).show();
                    _strInput = "";
                }
                else {
                    strLast = _strInput.substring(_strInput.length() - 1, _strInput.length());
                    if (strLast.equals("*") || strLast.equals("."))
                        return;
                    _strInput += "*";
                }
                break;
            case R.id.btnEnter:
                nStatue_Saved = -1;
                parseInput();
                break;
            case R.id.btnCash: {
                if (_fTotal <= 0) {
                    Toast.makeText(getContext(), "Please select items to cash.", Toast.LENGTH_SHORT).show();
                    return;
                }
                CashDetailDialog dialog = new CashDetailDialog(getContext(), this);
                dialog.show();
                break;
            }
            case R.id.btnCredit: {
                if (_fTotal <= 0) {
                    Toast.makeText(getContext(), "Please select items to cash.", Toast.LENGTH_SHORT).show();
                    return;
                }
                float fBal = _fTotal + _fTrans;
                tvBalanceDue_.setText(String.format("%.2f", fBal));
                tvAmountTendered.setText(String.format("%.2f", fBal));
                tvChange.setText("0.00");

                POSApplication.mRealmDB.beginTransaction();

                Number maxId_receipt = POSApplication.mRealmDB.where(ReceiptTable.class).max("id");
                int nextId_receipt = (maxId_receipt == null) ? 1 : maxId_receipt.intValue() + 1;
                ReceiptTable receipt = POSApplication.mRealmDB.createObject(ReceiptTable.class, nextId_receipt);
                receipt.setTotal(_fTotal);
                receipt.setTrans(_fTrans);
                receipt.setPay_type(GlobalVariable.PAYMENT_TYPE.CREDIT.getValue());
                receipt.setPay_time(System.currentTimeMillis());
                receipt.setUser_id(GlobalVariable.getInstance(getContext()).g_userid);

                RealmList<SoldItemTable> soldItems = new RealmList<>();
                int receiptID = 0;
                for (int i = 0; i < items.size(); i ++) {
                    Number maxId_solditem = POSApplication.mRealmDB.where(SoldItemTable.class).max("id");
                    receiptID = (maxId_solditem == null) ? 1 : maxId_solditem.intValue() + 1;

                    SoldItemTable soldItem = POSApplication.mRealmDB.createObject(SoldItemTable.class, receiptID);
                    soldItem.setCode(items.get(i).code);
                    soldItem.setQty(items.get(i).qty);

                    soldItem.setReceipt_id(receipt.getId());
                    soldItem.setPay_time(receipt.getPay_time());
                    soldItem.setPay_type(receipt.getPay_type());
                    soldItem.setType_ref(receipt.getType_ref());
                    soldItem.setUser_id(receipt.getUser_id());

                    soldItems.add(soldItem);
                }

                receipt.setListSold(soldItems);

                POSApplication.mRealmDB.commitTransaction();
                if (receiptID < 1000)
                    tvReceipt.setText(String.format("REC%03d", receiptID));
                else
                    tvReceipt.setText(String.format("REC%d", receiptID));

                nStatue_Saved = 3;
                break;
            }
            case R.id.btnAccount: {
                if (_fTotal <= 0) {
                    Toast.makeText(getContext(), "Please select items to cash.", Toast.LENGTH_SHORT).show();
                    return;
                }
                AccountDetailDialog dialog = new AccountDetailDialog(getContext(), this);
                dialog.show();
                break;
            }
            case R.id.btnMPESA: {
                MPESADetailDialog dialog = new MPESADetailDialog(getContext(), this);
                dialog.show();
                break;
            }
            case R.id.btnNew: {
                resetWidget();
                break;
            }
        }
        updateEditView();
    }

    @Override
    public void onCashDialogOK(float amount) {
        float fBal = _fTotal + _fTrans;
        tvBalanceDue_.setText(String.format("%.2f", fBal));
        tvAmountTendered.setText(String.format("%.2f", amount));
        tvChange.setText(String.format("%.2f", amount - fBal));

        POSApplication.mRealmDB.beginTransaction();

//        int lastReceiptID = Settings.getLastReceipt(getContext()) + 1;
        Number maxId_receipt = POSApplication.mRealmDB.where(ReceiptTable.class).max("id");
        int nextId_receipt = (maxId_receipt == null) ? 1 : maxId_receipt.intValue() + 1;
        ReceiptTable receipt = POSApplication.mRealmDB.createObject(ReceiptTable.class, nextId_receipt);
        receipt.setTotal(_fTotal);
        receipt.setTrans(_fTrans);
        receipt.setPay_type(GlobalVariable.PAYMENT_TYPE.CASH.getValue());
        receipt.setPay_time(System.currentTimeMillis());
        receipt.setUser_id(GlobalVariable.getInstance(getContext()).g_userid);


        RealmList<SoldItemTable> soldItems = new RealmList<>();
        int receiptID = 0;
        for (int i = 0; i < items.size(); i ++) {
            Number maxId_solditem = POSApplication.mRealmDB.where(SoldItemTable.class).max("id");
            receiptID = (maxId_solditem == null) ? 1 : maxId_solditem.intValue() + 1;

            SoldItemTable soldItem = POSApplication.mRealmDB.createObject(SoldItemTable.class, receiptID);
//            SoldItemTable soldItem = POSApplication.mRealmDB.createObject(SoldItemTable.class, System.currentTimeMillis());
            soldItem.setCode(items.get(i).code);
            soldItem.setQty(items.get(i).qty);

            soldItem.setReceipt_id(receipt.getId());
            soldItem.setPay_time(receipt.getPay_time());
            soldItem.setPay_type(receipt.getPay_type());
            soldItem.setType_ref(receipt.getType_ref());
            soldItem.setUser_id(receipt.getUser_id());

            soldItems.add(soldItem);
        }

        receipt.setListSold(soldItems);

//        Settings.setLastReceipt(getContext(), lastReceiptID);
        POSApplication.mRealmDB.commitTransaction();

        if (receiptID < 1000)
            tvReceipt.setText(String.format("REC%03d", receiptID));
        else
            tvReceipt.setText(String.format("REC%d", receiptID));
        nStatue_Saved = 0;
    }

    @Override
    public void onMPESADetailDialogOK(String strTransactionNumber) {
        float fBal = _fTotal + _fTrans;
        tvBalanceDue_.setText(String.format("%.2f", fBal));
        tvAmountTendered.setText(String.format("%.2f", fBal));
        tvChange.setText("0.00");

        POSApplication.mRealmDB.beginTransaction();

        Number maxId_receipt = POSApplication.mRealmDB.where(ReceiptTable.class).max("id");
        int nextId_receipt = (maxId_receipt == null) ? 1 : maxId_receipt.intValue() + 1;
        ReceiptTable receipt = POSApplication.mRealmDB.createObject(ReceiptTable.class, nextId_receipt);
        receipt.setTotal(_fTotal);
        receipt.setTrans(_fTrans);
        receipt.setPay_type(GlobalVariable.PAYMENT_TYPE.MPESA.getValue());
        receipt.setPay_time(System.currentTimeMillis());
        receipt.setUser_id(GlobalVariable.getInstance(getContext()).g_userid);
        receipt.setType_ref(strTransactionNumber);

        int receiptID = 0;
        RealmList<SoldItemTable> soldItems = new RealmList<>();
        for (int i = 0; i < items.size(); i ++) {
            Number maxId_solditem = POSApplication.mRealmDB.where(SoldItemTable.class).max("id");
            receiptID = (maxId_solditem == null) ? 1 : maxId_solditem.intValue() + 1;

            SoldItemTable soldItem = POSApplication.mRealmDB.createObject(SoldItemTable.class, receiptID);
            soldItem.setCode(items.get(i).code);
            soldItem.setQty(items.get(i).qty);

            soldItem.setReceipt_id(receipt.getId());
            soldItem.setPay_time(receipt.getPay_time());
            soldItem.setPay_type(receipt.getPay_type());
            soldItem.setType_ref(receipt.getType_ref());
            soldItem.setUser_id(receipt.getUser_id());

            soldItems.add(soldItem);
        }

        receipt.setListSold(soldItems);

        POSApplication.mRealmDB.commitTransaction();

        if (receiptID < 1000)
            tvReceipt.setText(String.format("REC%03d", receiptID));
        else
            tvReceipt.setText(String.format("REC%d", receiptID));
        nStatue_Saved = 1;
    }

    @Override
    public void onAccountDetailDialogOK(String accountID, String pin) {
        if (accountID.isEmpty()) {
            Toast.makeText(getContext(), "Please input account number.", Toast.LENGTH_SHORT).show();
            return;
        }
        RealmQuery<AccountTable> queryAccounts = POSApplication.mRealmDB.where(AccountTable.class);
        queryAccounts.equalTo("id", accountID);
        RealmResults<AccountTable> resultAccount = queryAccounts.findAll();
        if (resultAccount == null || resultAccount.size() == 0) {
            Toast.makeText(getContext(), "Cannot find account. Please try again.", Toast.LENGTH_LONG).show();
            return;
        }

        AccountTable account = resultAccount.get(0);
        PrepayTable prepayInfo = null;
        PostpaidTable postpaidInfo = null;

        RealmQuery<PrepayTable> queryPrepay = POSApplication.mRealmDB.where(PrepayTable.class);
        queryPrepay.equalTo("account_id", accountID);
        RealmResults<PrepayTable> resultPrepay = queryPrepay.findAll();
        if (resultPrepay.size() != 0) {
            prepayInfo = resultPrepay.get(0);
        }

        RealmQuery<PostpaidTable> queryPostpaid = POSApplication.mRealmDB.where(PostpaidTable.class);
        queryPostpaid.equalTo("account_id", accountID);
        RealmResults<PostpaidTable> resultPostpaid = queryPostpaid.findAll();
        if (resultPostpaid.size() != 0) {
            postpaidInfo = resultPostpaid.get(0);
        }

        if (prepayInfo == null && postpaidInfo == null){
            Toast.makeText(getContext(), "Current account haven't any payment info", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean isPrepay = true;
        float fBal = _fTotal + _fTrans;
        float amount = 0.f;
        if (prepayInfo != null) {
            amount = prepayInfo.getAmount();

        }
        else {
            amount = postpaidInfo.getAmount();
            isPrepay = false;
        }
        if (amount < fBal) {
            Toast.makeText(getContext(), "Current account haven't enough amount to pay", Toast.LENGTH_SHORT).show();
            return;
        }

        tvBalanceDue_.setText(String.format("%.2f", fBal));
        tvAmountTendered.setText(String.format("%.2f", amount));
        tvChange.setText(String.format("%.2f", amount - fBal));
        tvBalanceBefore.setText(tvAmountTendered.getText());
        tvAccountTrans.setText(tvBalanceDue_.getText());
        tvBalanceAfter.setText(tvChange.getText());

        POSApplication.mRealmDB.beginTransaction();

        Number maxId_receipt = POSApplication.mRealmDB.where(ReceiptTable.class).max("id");
        int nextId_receipt = (maxId_receipt == null) ? 1 : maxId_receipt.intValue() + 1;
        ReceiptTable receipt = POSApplication.mRealmDB.createObject(ReceiptTable.class, nextId_receipt);
        receipt.setTotal(_fTotal);
        receipt.setTrans(_fTrans);
        if (isPrepay)
            receipt.setPay_type(GlobalVariable.PAYMENT_TYPE.ACC_PREPAY.getValue());
        else
            receipt.setPay_type(GlobalVariable.PAYMENT_TYPE.ACC_POSTPAID.getValue());
        receipt.setPay_time(System.currentTimeMillis());
        receipt.setUser_id(GlobalVariable.getInstance(getContext()).g_userid);
        receipt.setType_ref(accountID);

        RealmList<SoldItemTable> soldItems = new RealmList<>();
        int receiptID = 0;
        for (int i = 0; i < items.size(); i ++) {
            Number maxId_solditem = POSApplication.mRealmDB.where(SoldItemTable.class).max("id");
            receiptID = (maxId_solditem == null) ? 1 : maxId_solditem.intValue() + 1;

            SoldItemTable soldItem = POSApplication.mRealmDB.createObject(SoldItemTable.class, receiptID);
            soldItem.setCode(items.get(i).code);
            soldItem.setQty(items.get(i).qty);
            soldItem.setReceipt_id(receipt.getId());
            soldItem.setPay_time(receipt.getPay_time());
            soldItem.setPay_type(receipt.getPay_type());
            soldItem.setType_ref(receipt.getType_ref());
            soldItem.setUser_id(receipt.getUser_id());
            soldItems.add(soldItem);
        }

        receipt.setListSold(soldItems);

        ///account info

        if (isPrepay) {
            prepayInfo.setAmount(amount - fBal);
            tvAccountType.setText("PREPAID");
        }
        else {
            postpaidInfo.setAmount(amount - fBal);
            postpaidInfo.setPayment(postpaidInfo.getPayment() + fBal);
            tvAccountType.setText("POSTPAID");
        }

        POSApplication.mRealmDB.commitTransaction();

        if (receiptID < 1000)
            tvReceipt.setText(String.format("REC%03d", receiptID));
        else
            tvReceipt.setText(String.format("REC%d", receiptID));

        tvAccountID.setText(account.getId());
        tvAccountName.setText(account.getFirst_name() + " " + account.getLast_name());

        nStatue_Saved = 2;
    }

    public class SalesListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public SalesListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_sales, parent, false);
                holder = ViewHolder.getInstance(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            bindView(holder, position);

            return convertView;
        }

        private void bindView(ViewHolder viewHolder, int position){
            SalesItem item = items.get(position);
            viewHolder.tvItem.setText(item.desc);
            viewHolder.tvQty.setText(String.valueOf(item.qty));
            viewHolder.tvPrice.setText(String.format("%.2f", item.price));
            viewHolder.tvTotal.setText(String.format("%.2f", item.total));
        }
    }

    static class ViewHolder {
        TextView tvItem;
        TextView tvQty;
        TextView tvPrice;
        TextView tvTotal;
        public static ViewHolder getInstance(View view){
            ViewHolder holder = new ViewHolder();
            holder.tvItem = (TextView) view.findViewById(R.id.tvItem);
            holder.tvQty = (TextView) view.findViewById(R.id.tvQty);
            holder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            holder.tvTotal = (TextView) view.findViewById(R.id.tvTotal);
            return holder;
        }
    }
}
