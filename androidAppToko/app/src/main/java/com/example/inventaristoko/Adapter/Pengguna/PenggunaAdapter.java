package com.example.inventaristoko.Adapter.Pengguna;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanAdapter;
import com.example.inventaristoko.Model.Pengguna.Pengguna;
import com.example.inventaristoko.Model.Penjualan.Penjualan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Pengguna.PenggunaDetailActivity;
import com.example.inventaristoko.Screens.Penjualan.PenjualanDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PenggunaAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "PenggunaAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private PenggunaAdapter.Callback mCallback;
    private List<Pengguna> mPenggunaList;

    public PenggunaAdapter(List<Pengguna> penggunaList) {
        mPenggunaList = penggunaList;
    }

    public void setCallback(PenggunaAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new PenggunaAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengguna, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new PenggunaAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mPenggunaList != null && mPenggunaList.size() > 0) {
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
        if (mPenggunaList != null && mPenggunaList.size() > 0) {
            return mPenggunaList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Pengguna> penggunaList) {
        mPenggunaList.addAll(penggunaList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvUserNo)
        TextView tvUserNo;

        @BindView(R.id.tvFullName)
        TextView tvFullName;

        @BindView(R.id.tvUserName)
        TextView tvUserName;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvUserNo.setText("");
            tvFullName.setText("");
            tvUserName.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Pengguna mPengguna = mPenggunaList.get(position);

            tvUserNo.setText(mPengguna.getUserNo());
            tvFullName.setText(mPengguna.getFullName());
            tvUserName.setText(mPengguna.getUserName());

            itemView.setOnClickListener(v -> {
                if (mPengguna.getFullName() != null) {
                    try {
                        Intent intent = new Intent (v.getContext(), PenggunaDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("fullName", mPengguna.getFullName());
                        bundle.putString("userName", mPengguna.getUserName());
                        bundle.putString("email", mPengguna.getEmail());
                        bundle.putString("phoneNumber", mPengguna.getPhoneNumber());
                        bundle.putString("birthDate", mPengguna.getBirthDate());
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
