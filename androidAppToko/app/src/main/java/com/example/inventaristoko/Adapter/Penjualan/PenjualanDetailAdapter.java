package com.example.inventaristoko.Adapter.Penjualan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Penjualan.PenjualanDetail;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenjualanDetailAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "PenjualanDetailAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<PenjualanDetail> mPenjualanDetailList;

    public PenjualanDetailAdapter(List<PenjualanDetail> penjualanDetailList) {
        mPenjualanDetailList = penjualanDetailList;
    }

    public void setCallback(PenjualanDetailAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_penjualan_detail, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_kosong, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mPenjualanDetailList != null && mPenjualanDetailList.size() > 0) {
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
        if (mPenjualanDetailList != null && mPenjualanDetailList.size() > 0) {
            return mPenjualanDetailList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<PenjualanDetail> penjualanDetailList) {
        mPenjualanDetailList.addAll(penjualanDetailList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvJumlahDetailPenjualan)
        TextView tvJumlahDetailPenjualan;

        @BindView(R.id.tvNamaDetailPenjualan)
        TextView tvNamaDetailPenjualan;

        @BindView(R.id.tvCatatanDetailPenjualan)
        TextView tvCatatanDetailPenjualan;

        @BindView(R.id.tvTotalHargaDetailPenjualan)
        TextView tvTotalHargaDetailPenjualan;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvJumlahDetailPenjualan.setText("");
            tvNamaDetailPenjualan.setText("");
            tvCatatanDetailPenjualan.setText("");
            tvTotalHargaDetailPenjualan.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final PenjualanDetail mPenjualanDetail = mPenjualanDetailList.get(position);

            tvNamaDetailPenjualan.setText(mPenjualanDetail.getNamaDetailPenjualan());
            tvJumlahDetailPenjualan.setText(mPenjualanDetail.getJumlahDetailPenjualan() + "x");
            tvCatatanDetailPenjualan.setText(mPenjualanDetail.getCatatanDetailPenjualan());

            int amount = Integer.parseInt(mPenjualanDetail.getJumlahDetailPenjualan());
            int price = Integer.parseInt(mPenjualanDetail.getHargaDetailMakanan());
            int totalPrice = amount * price;
            mPenjualanDetail.setTotalHargaDetailPenjualan(String.valueOf(totalPrice));

            tvTotalHargaDetailPenjualan.setText(CommonUtils.currencyFormat(mPenjualanDetail.getTotalHargaDetailPenjualan()));
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
