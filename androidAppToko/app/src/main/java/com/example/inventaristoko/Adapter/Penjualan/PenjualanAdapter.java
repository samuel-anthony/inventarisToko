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
import com.example.inventaristoko.Screens.Resi.ResiDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;

import java.util.List;

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
                                .inflate(R.layout.list_kosong, parent, false));
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
        @BindView(R.id.tvId)
        TextView tvId;

        @BindView(R.id.tvIdPenjualan)
        TextView tvIdPenjualan;

        @BindView(R.id.tvStatusPenjualan)
        TextView tvStatusPenjualan;

        @BindView(R.id.tvTotalHargaPenjualan)
        TextView tvTotalHargaPenjualan;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvId.setText("");
            tvIdPenjualan.setText("");
            tvStatusPenjualan.setText("");
            tvTotalHargaPenjualan.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Penjualan mPenjualan = mPenjualanList.get(position);

            tvId.setText(mPenjualan.getId());
            tvIdPenjualan.setText(mPenjualan.getIdPenjualan());
            tvTotalHargaPenjualan.setText(CommonUtils.currencyFormat(mPenjualan.getTotalHargaPenjualan()));

            if (mPenjualan.getKodeStatusPenjualan() != null) {
                switch (mPenjualan.getKodeStatusPenjualan()) {
                    case MyConstants.ORDER_CODE:
                        tvStatusPenjualan.setText(MyConstants.ORDER_NAME);
                        tvStatusPenjualan.setTextColor(MyConstants.ORDER_COLOR);
                        break;
                    case MyConstants.GOING_CODE:
                        tvStatusPenjualan.setText(MyConstants.GOING_NAME);
                        tvStatusPenjualan.setTextColor(MyConstants.GOING_COLOR);
                        break;
                    case MyConstants.FINISH_CODE:
                        tvStatusPenjualan.setText(MyConstants.FINISH_NAME);
                        tvStatusPenjualan.setTextColor(MyConstants.FINISH_COLOR);
                        break;
                }
            }

            itemView.setOnClickListener(v -> {
                if (mPenjualan.getIdPenjualan() != null) {
                    try {
                        if(String.valueOf(mPenjualan.getKodeStatusPenjualan()).equals(MyConstants.PAID_CODE)) {
                            Intent intent = new Intent(v.getContext(), ResiDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("idPenjualan", mPenjualan.getIdPenjualan());
                            bundle.putString("tanggalTambah", mPenjualan.getTanggalTambahPenjualan());
                            bundle.putString("kodeStatus", mPenjualan.getKodeStatusPenjualan());
                            bundle.putString("totalHarga", CommonUtils.currencyFormat(mPenjualan.getTotalHargaPenjualan()));
                            intent.putExtras(bundle);
                            v.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(v.getContext(), PenjualanDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("idPenjualan", mPenjualan.getIdPenjualan());
                            bundle.putString("tanggalTambah", mPenjualan.getTanggalTambahPenjualan());
                            bundle.putString("kodeStatus", mPenjualan.getKodeStatusPenjualan());
                            bundle.putString("totalHarga", CommonUtils.currencyFormat(mPenjualan.getTotalHargaPenjualan()));
                            intent.putExtras(bundle);
                            v.getContext().startActivity(intent);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onClick: Url is not correct");
                    }
                }
            });
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {
        }
    }
}