package com.example.inventaristoko.Adapter.Penjualan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Penjualan.Penjualan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Penjualan.PenjualanDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenjualanAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "PenjualanAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<Penjualan> mPenjualanList;

    public PenjualanAdapter(List<Penjualan> penjualanList) {
        mPenjualanList = penjualanList;
    }

    public void setCallback(PenjualanAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_penjualan, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mPenjualanList != null && mPenjualanList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (mPenjualanList != null && mPenjualanList.size() > 0) {
            return mPenjualanList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Penjualan> penjualanList) {
        mPenjualanList.addAll(penjualanList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvOrderNo)
        TextView tvOrderNo;

        @BindView(R.id.tvRefNo)
        TextView tvRefNo;

        @BindView(R.id.tvStatus)
        TextView tvStatus;

        @BindView(R.id.tvTotalPrice)
        TextView tvTotalPrice;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvOrderNo.setText("");
            tvRefNo.setText("");
            tvStatus.setText("");
            tvTotalPrice.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Penjualan mPenjualan = mPenjualanList.get(position);

            tvOrderNo.setText(mPenjualan.getOrderNo());
            tvRefNo.setText(mPenjualan.getRefNo());
            tvTotalPrice.setText(CommonUtils.currencyFormat(mPenjualan.getTotalPrice()));

            if (mPenjualan.getStatusCode() != null) {
                switch (mPenjualan.getStatusCode()) {
                    case MyConstants.ORDER_CODE:
                        tvStatus.setText(MyConstants.ORDER_NAME);
                        tvStatus.setTextColor(MyConstants.ORDER_COLOR);
                        break;
                    case MyConstants.GOING_CODE:
                        tvStatus.setText(MyConstants.GOING_NAME);
                        tvStatus.setTextColor(MyConstants.GOING_COLOR);
                        break;
                    case MyConstants.FINISH_CODE:
                        tvStatus.setText(MyConstants.FINISH_NAME);
                        tvStatus.setTextColor(MyConstants.FINISH_COLOR);
                        break;
                }
            }

            itemView.setOnClickListener(v -> {
                if (mPenjualan.getRefNo() != null) {
                    try {
                        Map<String, String> params = new HashMap<>();
                        params.put("refNo",  mPenjualan.getRefNo());
                        params.put("createdAt", mPenjualan.getCreatedAt());
                        params.put("statusCode", mPenjualan.getStatusCode());
                        params.put("totalHarga", CommonUtils.currencyFormat(mPenjualan.getTotalPrice()));

                        Intent intent = new Intent (v.getContext(), PenjualanDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("refNo", params.get("refNo"));
                        bundle.putString("createdAt", params.get("createdAt"));
                        bundle.putString("statusCode", params.get("statusCode"));
                        bundle.putString("totalHarga", params.get("totalHarga"));
                        intent.putExtras(bundle);
                        v.getContext().startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: Url is not correct");
                    }
                }
            });
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {
        @BindView(R.id.tv_message)
        TextView messageTextView;
        @BindView(R.id.buttonRetry)
        TextView buttonRetry;

        EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            buttonRetry.setOnClickListener(v -> mCallback.onEmptyViewRetryClick());
        }

        @Override
        protected void clear() {
        }
    }
}