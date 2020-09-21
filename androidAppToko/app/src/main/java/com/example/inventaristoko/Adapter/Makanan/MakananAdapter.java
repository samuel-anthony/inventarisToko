package com.example.inventaristoko.Adapter.Makanan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Makanan.Makanan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Makanan.MakananDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MakananAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "MakananAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private MakananAdapter.Callback mCallback;
    private List<Makanan> mMakananList;

    public MakananAdapter(List<Makanan> makananList) {
        mMakananList = makananList;
    }

    public void setCallback(MakananAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MakananAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_makanan, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new MakananAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_kosong, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMakananList != null && mMakananList.size() > 0) {
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
        if (mMakananList != null && mMakananList.size() > 0) {
            return mMakananList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Makanan> makananList) {
        mMakananList.addAll(makananList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;

        @BindView(R.id.tvNamaMakanan)
        TextView tvNamaMakanan;

        @BindView(R.id.tvHargaMakanan)
        TextView tvHargaMakanan;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvId.setText("");
            tvNamaMakanan.setText("");
            tvHargaMakanan.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Makanan mMakanan = mMakananList.get(position);

            tvId.setText(mMakanan.getId());
            tvNamaMakanan.setText(mMakanan.getNamaMakanan());
            tvHargaMakanan.setText(CommonUtils.currencyFormat(mMakanan.getHargaMakanan()));

            itemView.setOnClickListener(v -> {
                if (mMakanan.getIdMakanan() != null) {
                    try {
                        Intent intent = new Intent (v.getContext(), MakananDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("idMakanan", mMakanan.getIdMakanan());
                        bundle.putString("namaMakanan", mMakanan.getNamaMakanan());
                        bundle.putString("hargaMakanan", mMakanan.getHargaMakanan());
                        bundle.putString("gambarMakanan", mMakanan.getGambarMakanan());
//                        bundle.putString("bahanMakanan", mMakanan.getBahanMakanan());
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
