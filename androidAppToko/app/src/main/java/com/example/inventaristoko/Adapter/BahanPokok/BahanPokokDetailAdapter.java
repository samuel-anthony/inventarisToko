package com.example.inventaristoko.Adapter.BahanPokok;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inventaristoko.Model.BahanPokok.BahanPokokDetail;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.BahanPokok.BahanPokokDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BahanPokokDetailAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "BahanPokokDetailAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private BahanPokokDetailAdapter.Callback mCallback;
    private List<BahanPokokDetail> mBahanPokokDetailList;

    public BahanPokokDetailAdapter(List<BahanPokokDetail> bahanPokokDetailList) {
        mBahanPokokDetailList = bahanPokokDetailList;
    }

    public void setCallback(BahanPokokDetailAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BahanPokokDetailAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bahan_pokok_detail, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new BahanPokokDetailAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBahanPokokDetailList != null && mBahanPokokDetailList.size() > 0) {
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
        if (mBahanPokokDetailList != null && mBahanPokokDetailList.size() > 0) {
            return mBahanPokokDetailList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<BahanPokokDetail> bahanPokokDetailList) {
        mBahanPokokDetailList.addAll(bahanPokokDetailList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvDetailId)
        TextView tvId;

        @BindView(R.id.tvBahanPokokDetailNama)
        TextView tvBahanPokokDetailNama;

        @BindView(R.id.tvBahanPokokDetailNamaToko)
        TextView tvBahanPokokDetailNamaToko;

        @BindView(R.id.tvBahanPokokDetailJumlah)
        TextView tvBahanPokokDetailJumlah;

        @BindView(R.id.tvBahanPokokDetailHarga)
        TextView tvBahanPokokDetailHarga;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvId.setText("");
            tvBahanPokokDetailNama.setText("");
            tvBahanPokokDetailNamaToko.setText("");
            tvBahanPokokDetailJumlah.setText("");
            tvBahanPokokDetailHarga.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final BahanPokokDetail mBahanPokokDetail = mBahanPokokDetailList.get(position);

            tvId.setText(mBahanPokokDetail.getDetailId());
            tvBahanPokokDetailNama.setText(mBahanPokokDetail.getStapleDetailName());
            tvBahanPokokDetailNamaToko.setText(mBahanPokokDetail.getStapleDetailStoreName());
            tvBahanPokokDetailJumlah.setText(mBahanPokokDetail.getStapleDetailAmount());
            tvBahanPokokDetailHarga.setText(mBahanPokokDetail.getStapleDetailPrice());

            itemView.setOnClickListener(v -> {
                if (mBahanPokokDetail.getStapleDetailId() != null) {
                    try {
                        Intent intent = new Intent (v.getContext(), BahanPokokDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("stapleDetailId", mBahanPokokDetail.getStapleDetailId());
                        bundle.putString("stapleDetailName", mBahanPokokDetail.getStapleDetailName());
                        bundle.putString("stapleDetailStoreName", mBahanPokokDetail.getStapleDetailStoreName());
                        bundle.putString("stapleDetailAmount", mBahanPokokDetail.getStapleDetailAmount());
                        bundle.putString("stapleDetailPrice", mBahanPokokDetail.getStapleDetailPrice());
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
