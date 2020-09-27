package com.example.inventaristoko.Adapter.Kategori;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Kategori.Kategori;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Kategori.KategoriDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KategoriAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "KategoriAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private KategoriAdapter.Callback mCallback;
    private List<Kategori> mKategoriList;

    public KategoriAdapter(List<Kategori> mejaList) {
        mKategoriList = mejaList;
    }

    public void setCallback(KategoriAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new KategoriAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kategori, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new KategoriAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_kosong, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mKategoriList != null && mKategoriList.size() > 0) {
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
        if (mKategoriList != null && mKategoriList.size() > 0) {
            return mKategoriList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Kategori> kategoriList) {
        mKategoriList.addAll(kategoriList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvId)
        TextView tvId;

        @BindView(R.id.tvNamaKategori)
        TextView tvNamaKategori;

        @BindView(R.id.tvIdKategori)
        TextView tvIdKategori;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvId.setText("");
            tvNamaKategori.setText("");
            tvIdKategori.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Kategori mKategori = mKategoriList.get(position);

            tvId.setText(mKategori.getId());
            tvNamaKategori.setText(mKategori.getNamaKategori());
            tvIdKategori.setText(mKategori.getIdKategori());

            itemView.setOnClickListener(v -> {
                if (mKategori.getIdKategori() != null) {
                    try {
                        Intent intent = new Intent (v.getContext(), KategoriDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", mKategori.getId());
                        bundle.putString("namaKategori", mKategori.getNamaKategori());
                        bundle.putString("idKategori", mKategori.getIdKategori());
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
        EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {
        }
    }
}
