package com.wolf.fingerpos.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wolf.fingerpos.ItemDetailActivity;
import com.wolf.fingerpos.POSApplication;
import com.wolf.fingerpos.R;
import com.wolf.fingerpos.database.MenuTable;
import com.wolf.fingerpos.database.ReceiptTable;
import com.wolf.fingerpos.utils.GlobalVariable;
import com.wolf.fingerpos.utils.Utils;

import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ReportsSummaryFragment extends Fragment implements View.OnClickListener {
    Button btnCashDetail, btnCCDetail, btnMpesaDetail, btnPrepaidDetail, btnPostpaidDetail, btnTotalDetail, btnPrint;
    TextView tvUser, tvDuration;
    TextView tvCash, tvCredit, tvMpesg, tvAccPrepay, tvAccPostpaid, tvTotal;

    public static ReportsSummaryFragment newInstance() {
        Bundle bundle = new Bundle();

        ReportsSummaryFragment fragment = new ReportsSummaryFragment();
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
        return inflater.inflate(R.layout.fragment_reports_summary, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnCashDetail = (Button)view.findViewById(R.id.btnCashDetail);
        btnCashDetail.setOnClickListener(this);
        btnCCDetail = (Button)view.findViewById(R.id.btnCCDetail);
        btnCCDetail.setOnClickListener(this);
        btnMpesaDetail = (Button)view.findViewById(R.id.btnMpesaDetail);
        btnMpesaDetail.setOnClickListener(this);
        btnPrepaidDetail = (Button)view.findViewById(R.id.btnPrepaidDetail);
        btnPrepaidDetail.setOnClickListener(this);
        btnPostpaidDetail = (Button)view.findViewById(R.id.btnPostpaidDetail);
        btnPostpaidDetail.setOnClickListener(this);
        btnTotalDetail = (Button)view.findViewById(R.id.btnTotalDetail);
        btnTotalDetail.setOnClickListener(this);
        btnPrint = (Button)view.findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(this);

        tvUser = (TextView)view.findViewById(R.id.tvUser);
        tvDuration = (TextView)view.findViewById(R.id.tvDuration);
        tvCash = (TextView)view.findViewById(R.id.tvCash);
        tvCredit = (TextView)view.findViewById(R.id.tvCredit);
        tvMpesg = (TextView)view.findViewById(R.id.tvMpesg);
        tvAccPrepay = (TextView)view.findViewById(R.id.tvAccPrepay);
        tvAccPostpaid = (TextView)view.findViewById(R.id.tvAccPostpaid);
        tvTotal = (TextView)view.findViewById(R.id.tvTotal);
        if (_from_duration == -1 || _to_duration == -1)
            return;
        refreshWidget();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), ItemDetailActivity.class);
        switch (view.getId()) {
            case R.id.btnCashDetail:
                intent.putExtra(ItemDetailActivity.KEY_SHOW_TYPE, GlobalVariable.PAYMENT_TYPE.CASH.getValue());
                break;
            case R.id.btnCCDetail:
                intent.putExtra(ItemDetailActivity.KEY_SHOW_TYPE, GlobalVariable.PAYMENT_TYPE.CREDIT.getValue());
                break;
            case R.id.btnMpesaDetail:
                intent.putExtra(ItemDetailActivity.KEY_SHOW_TYPE, GlobalVariable.PAYMENT_TYPE.MPESA.getValue());
                break;
            case R.id.btnPrepaidDetail:
                intent.putExtra(ItemDetailActivity.KEY_SHOW_TYPE, GlobalVariable.PAYMENT_TYPE.ACC_PREPAY.getValue());
                break;
            case R.id.btnPostpaidDetail:
                intent.putExtra(ItemDetailActivity.KEY_SHOW_TYPE, GlobalVariable.PAYMENT_TYPE.ACC_POSTPAID.getValue());
                break;
            case R.id.btnTotalDetail:
                intent.putExtra(ItemDetailActivity.KEY_SHOW_TYPE, GlobalVariable.PAYMENT_TYPE.ETC.getValue());
                break;
            case R.id.btnPrint:
                return;
        }
        startActivity(intent);
    }

    public static RealmResults<ReceiptTable> _resultCash, _resultCredit, _resultMpesa, _resultAccPre, _resultAccPost, _resultTotal;
    public static float _cash, _credit, _Mpesa, _AccPre, _AccPost, _total;
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
        RealmQuery<ReceiptTable> queryCash = POSApplication.mRealmDB.where(ReceiptTable.class);
        queryCash.between("pay_time", _from_duration, _to_duration);
        queryCash.equalTo("pay_type", GlobalVariable.PAYMENT_TYPE.CASH.getValue());

        _resultCash = queryCash.findAll();
        _cash = getTotal(_resultCash);

        RealmQuery<ReceiptTable> queryCredit = POSApplication.mRealmDB.where(ReceiptTable.class);
        queryCredit.between("pay_time", _from_duration, _to_duration);
        queryCredit.equalTo("pay_type", GlobalVariable.PAYMENT_TYPE.CREDIT.getValue());

        _resultCredit = queryCredit.findAll();
        _credit = getTotal(_resultCredit);

        RealmQuery<ReceiptTable> queryMpesa = POSApplication.mRealmDB.where(ReceiptTable.class);
        queryMpesa.between("pay_time", _from_duration, _to_duration);
        queryMpesa.equalTo("pay_type", GlobalVariable.PAYMENT_TYPE.MPESA.getValue());

        _resultMpesa = queryMpesa.findAll();
        _Mpesa = getTotal(_resultMpesa);


        RealmQuery<ReceiptTable> queryAccPre = POSApplication.mRealmDB.where(ReceiptTable.class);
        queryAccPre.between("pay_time", _from_duration, _to_duration);
        queryAccPre.equalTo("pay_type", GlobalVariable.PAYMENT_TYPE.ACC_PREPAY.getValue());

        _resultAccPre = queryAccPre.findAll();
        _AccPre = getTotal(_resultAccPre);


        RealmQuery<ReceiptTable> queryAccPost = POSApplication.mRealmDB.where(ReceiptTable.class);
        queryAccPost.between("pay_time", _from_duration, _to_duration);
        queryAccPost.equalTo("pay_type", GlobalVariable.PAYMENT_TYPE.ACC_POSTPAID.getValue());

        _resultAccPost = queryAccPost.findAll();
        _AccPost = getTotal(_resultAccPost);

        RealmQuery<ReceiptTable> queryTotal = POSApplication.mRealmDB.where(ReceiptTable.class);
        queryTotal.between("pay_time", _from_duration, _to_duration);

        _resultTotal = queryTotal.findAll();
        _total = getTotal(_resultTotal); //_cash + _credit + _Mpesa + _AccPre + _AccPost;

        tvCash.setText(String.format("%.2f", _cash));
        tvCredit.setText(String.format("%.2f", _credit));
        tvMpesg.setText(String.format("%.2f", _Mpesa));
        tvAccPrepay.setText(String.format("%.2f", _AccPre));
        tvAccPostpaid.setText(String.format("%.2f", _AccPost));
        tvTotal.setText(String.format("%.2f", _total));
    }

    float getTotal(RealmResults<ReceiptTable> receipts) {
        float result = 0.0f;
        for (int i = 0; i < receipts.size(); i ++) {
            result += receipts.get(i).getTotal();
            result += receipts.get(i).getTrans();
        }
        return result;
    }
}
