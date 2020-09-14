package com.example.inventaristoko.Adapter.BahanPokok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inventaristoko.Model.BahanPokok.BahanPokokHistory;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BahanPokokHistoryAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "BahanPokokHistoryAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private BahanPokokHistoryAdapter.Callback mCallback;
    private List<BahanPokokHistory> mBahanPokokHistoryList;

    public BahanPokokHistoryAdapter(List<BahanPokokHistory> bahanPokokHistoryList) {
        mBahanPokokHistoryList = bahanPokokHistoryList;
    }

    public void setCallback(BahanPokokHistoryAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BahanPokokHistoryAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bahan_pokok_history, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new BahanPokokHistoryAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_kosong, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBahanPokokHistoryList != null && mBahanPokokHistoryList.size() > 0) {
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
        if (mBahanPokokHistoryList != null && mBahanPokokHistoryList.size() > 0) {
            return mBahanPokokHistoryList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<BahanPokokHistory> bahanPokokHistoryList) {
        mBahanPokokHistoryList.addAll(bahanPokokHistoryList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvBahanPokokDetailJumlah)
        TextView tvBahanPokokDetailJumlah;

        @BindView(R.id.tvBahanPokokDetailNamaToko)
        TextView tvBahanPokokDetailNamaToko;

        @BindView(R.id.tvBahanPokokDetailHarga)
        TextView tvBahanPokokDetailHarga;

        @BindView(R.id.tvBahanPokokDetailTanggal)
        TextView tvBahanPokokDetailTanggal;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvBahanPokokDetailJumlah.setText("");
            tvBahanPokokDetailNamaToko.setText("");
            tvBahanPokokDetailHarga.setText("");
            tvBahanPokokDetailTanggal.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final BahanPokokHistory mBahanPokokHistory = mBahanPokokHistoryList.get(position);

            if((mBahanPokokHistory.getStapleDetailAction()).equalsIgnoreCase(MyConstants.INCREASE_CODE)) {
                tvBahanPokokDetailJumlah.setText("+ " + mBahanPokokHistory.getStapleDetailAmount());
                tvBahanPokokDetailJumlah.setBackgroundResource(R.drawable.ic_circle_success_background);
            } else if (((mBahanPokokHistory.getStapleDetailAction()).equalsIgnoreCase(MyConstants.DECREASE_CODE))) {
                tvBahanPokokDetailJumlah.setText("- " + mBahanPokokHistory.getStapleDetailAmount());
                tvBahanPokokDetailJumlah.setBackgroundResource(R.drawable.ic_circle_failed_background);
            }

            tvBahanPokokDetailNamaToko.setText(mBahanPokokHistory.getStapleDetailStoreName());
            tvBahanPokokDetailHarga.setText(CommonUtils.currencyFormat(mBahanPokokHistory.getStapleDetailPrice()));
            tvBahanPokokDetailTanggal.setText(mBahanPokokHistory.getStapleDetailCreatedAt());
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {
        @BindView(R.id.btnCobaUlang)
        TextView btnCobaUlang;

        EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnCobaUlang.setOnClickListener(v -> mCallback.onEmptyViewRetryClick());
        }

        @Override
        protected void clear() {
        }
    }
}
