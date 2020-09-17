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
        @BindView(R.id.tvJumlahDetailBahanPokok)
        TextView tvJumlahDetailBahanPokok;

        @BindView(R.id.tvNamaTokoDetailBahanPokok)
        TextView tvNamaTokoDetailBahanPokok;

        @BindView(R.id.tvHargaDetailBahanPokok)
        TextView tvHargaDetailBahanPokok;

        @BindView(R.id.tvTanggalDetailBahanPokok)
        TextView tvTanggalDetailBahanPokok;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvJumlahDetailBahanPokok.setText("");
            tvNamaTokoDetailBahanPokok.setText("");
            tvHargaDetailBahanPokok.setText("");
            tvTanggalDetailBahanPokok.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final BahanPokokHistory mBahanPokokHistory = mBahanPokokHistoryList.get(position);

            if((mBahanPokokHistory.getAksiDetailBahanPokok()).equalsIgnoreCase(MyConstants.INCREASE_CODE)) {
                tvJumlahDetailBahanPokok.setText("+ " + mBahanPokokHistory.getJumlahDetailBahanPokok());
                tvJumlahDetailBahanPokok.setBackgroundResource(R.drawable.ic_circle_success_background);
            } else if (((mBahanPokokHistory.getAksiDetailBahanPokok()).equalsIgnoreCase(MyConstants.DECREASE_CODE))) {
                tvJumlahDetailBahanPokok.setText("- " + mBahanPokokHistory.getJumlahDetailBahanPokok());
                tvJumlahDetailBahanPokok.setBackgroundResource(R.drawable.ic_circle_failed_background);
            }

            tvNamaTokoDetailBahanPokok.setText(mBahanPokokHistory.getNamaTokoDetailBahanPokok());
            tvHargaDetailBahanPokok.setText(CommonUtils.currencyFormat(mBahanPokokHistory.getHargaDetailBahanPokok()));
            tvTanggalDetailBahanPokok.setText(mBahanPokokHistory.getTanggalTambahDetailBahanPokok());
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
