package com.example.inventaristoko.Adapter.BahanPokok;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.inventaristoko.Model.BahanPokok.BahanPokok;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.BahanPokok.BahanPokokDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BahanPokokAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "BahanPokokAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private BahanPokokAdapter.Callback mCallback;
    private List<BahanPokok> mBahanPokokList;

    public BahanPokokAdapter(List<BahanPokok> bahanPokokList) {
        mBahanPokokList = bahanPokokList;
    }

    public void setCallback(BahanPokokAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BahanPokokAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bahan_pokok, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new BahanPokokAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBahanPokokList != null && mBahanPokokList.size() > 0) {
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
        if (mBahanPokokList != null && mBahanPokokList.size() > 0) {
            return mBahanPokokList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<BahanPokok> bahanPokokList) {
        mBahanPokokList.addAll(bahanPokokList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;

        @BindView(R.id.tvBahanPokokNama)
        TextView tvBahanPokokNama;

        @BindView(R.id.tvBahanPokokJumlah)
        TextView tvBahanPokokJumlah;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvId.setText("");
            tvBahanPokokNama.setText("");
            tvBahanPokokJumlah.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final BahanPokok mBahanPokok = mBahanPokokList.get(position);

            tvId.setText(mBahanPokok.getId());
            tvBahanPokokNama.setText(mBahanPokok.getStapleName());
            tvBahanPokokJumlah.setText(mBahanPokok.getStapleAmount() + " " + mBahanPokok.getStapleUnit());

            itemView.setOnClickListener(v -> {
                if (mBahanPokok.getStapleId() != null) {
                    try {
                        Intent intent = new Intent (v.getContext(), BahanPokokDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("stapleId", mBahanPokok.getStapleId());
                        bundle.putString("stapleName", mBahanPokok.getStapleName());
                        bundle.putString("stapleAmount", mBahanPokok.getStapleAmount());
                        bundle.putString("stapleUnit", mBahanPokok.getStapleUnit());
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
