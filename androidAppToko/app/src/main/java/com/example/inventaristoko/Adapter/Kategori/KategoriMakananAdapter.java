package com.example.inventaristoko.Adapter.Kategori;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Kategori.KategoriMakanan;
import com.example.inventaristoko.R;

import java.util.ArrayList;

public class KategoriMakananAdapter extends RecyclerView.Adapter<KategoriMakananAdapter.RvViewHolder> {
    Context context;
    ArrayList<KategoriMakanan> kategoryMakanans;
    Onclick onclick;
    View view;

    public interface Onclick {
        void onEvent(KategoriMakanan kategoriMakanan, int pos);
    }

    public KategoriMakananAdapter(Context context, ArrayList<KategoriMakanan> kategoryMakanans, Onclick onclick) {
        this.context = context;
        this.kategoryMakanans = kategoryMakanans;
        this.onclick = onclick;
    }

    @Override
    public KategoriMakananAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.list_makanan_kategori, parent, false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(KategoriMakananAdapter.RvViewHolder holder, final int position) {
        final KategoriMakanan kategoriMakanan = kategoryMakanans.get(position);

        if (kategoriMakanan.getName() != null) {
            holder.tvNama.setText(kategoriMakanan.getName());
        }

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kategoryMakanans.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onEvent(kategoriMakanan, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kategoryMakanans.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        TextView tvNama;
        Button btnHapus;

        public RvViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvValueNamaMakananKategori);
            btnHapus = itemView.findViewById(R.id.btnTambahMakananKategori);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
