package com.example.inventaristoko.Adapter.Pengunjung;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanAdapter;
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

public class PengunjungStatusAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "PengunjungStatusAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<Penjualan> mPengunjungStatusList;

    public PengunjungStatusAdapter(List<Penjualan> pengunjungList) {
        mPengunjungStatusList = pengunjungList;
    }

    public void setCallback(PengunjungStatusAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new PengunjungStatusAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengunjung_status, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new PengunjungStatusAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_kosong, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mPengunjungStatusList != null && mPengunjungStatusList.size() > 0) {
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
        if (mPengunjungStatusList != null && mPengunjungStatusList.size() > 0) {
            return mPengunjungStatusList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Penjualan> pengunjungList) {
        mPengunjungStatusList.addAll(pengunjungList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;

        @BindView(R.id.tvIdPengunjung)
        TextView tvIdPengunjung;

        @BindView(R.id.tvTanggalPengunjung)
        TextView tvTanggalPengunjung;

        @BindView(R.id.tvPengunjungStatus)
        TextView tvPengunjungStatus;

        @BindView(R.id.tvTotalHargaPengunjung)
        TextView tvTotalHargaPengunjung;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvId.setText("");
            tvIdPengunjung.setText("");
            tvTanggalPengunjung.setText("");
            tvPengunjungStatus.setText("");
            tvTotalHargaPengunjung.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Penjualan mPengunjung = mPengunjungStatusList.get(position);

            tvId.setText(mPengunjung.getId());
            tvIdPengunjung.setText(mPengunjung.getIdPenjualan());
            tvTotalHargaPengunjung.setText(CommonUtils.currencyFormat(mPengunjung.getTotalHargaPenjualan()));
            tvTanggalPengunjung.setText(mPengunjung.getTanggalTambahPenjualan());

            if (mPengunjung.getKodeStatusPenjualan() != null) {
                switch (mPengunjung.getKodeStatusPenjualan()) {
                    case MyConstants.ORDER_CODE:
                        tvPengunjungStatus.setText(MyConstants.ORDER_NAME);
                        tvPengunjungStatus.setTextColor(MyConstants.ORDER_COLOR);
                        break;
                    case MyConstants.GOING_CODE:
                        tvPengunjungStatus.setText(MyConstants.GOING_NAME);
                        tvPengunjungStatus.setTextColor(MyConstants.GOING_COLOR);
                        break;
                    case MyConstants.FINISH_CODE:
                        tvPengunjungStatus.setText(MyConstants.FINISH_NAME);
                        tvPengunjungStatus.setTextColor(MyConstants.FINISH_COLOR);
                        break;
                }
            }
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
