package com.example.inventaristoko.Adapter.Makanan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Makanan.MakananBahanPokok;
import com.example.inventaristoko.R;

import java.util.ArrayList;

public class MakananBahanPokokAdapter extends RecyclerView.Adapter<MakananBahanPokokAdapter.RvViewHolder> {
    Context context;
    ArrayList<MakananBahanPokok> makananBahanPokoks;
    Onclick onclick;
    View view;

    public interface Onclick {
        void onEvent(MakananBahanPokok makananBahanPokok, int pos);
    }

    public MakananBahanPokokAdapter(Context context, ArrayList<MakananBahanPokok> makananBahanPokoks, Onclick onclick) {
        this.context = context;
        this.makananBahanPokoks = makananBahanPokoks;
        this.onclick = onclick;
    }

    @Override
    public MakananBahanPokokAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.list_makanan_bahan_pokok, parent, false);
        RvViewHolder rvViewHolder = new MakananBahanPokokAdapter.RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(MakananBahanPokokAdapter.RvViewHolder holder, final int position) {
        final MakananBahanPokok makananBahanPokok = makananBahanPokoks.get(position);

        if (makananBahanPokok.getName() != null) {
            holder.tvNama.setText(makananBahanPokok.getName());
            holder.tvJumlah.setText(makananBahanPokok.getJumlah());
            holder.tvSatuan.setText(makananBahanPokok.getSatuan());
        }

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makananBahanPokoks.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick.onEvent(makananBahanPokok, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return makananBahanPokoks.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        TextView tvNama;
        TextView tvJumlah;
        TextView tvSatuan;
        Button btnHapus;

        public RvViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvValueNamaMakananBahanPokok);
            tvJumlah = itemView.findViewById(R.id.tvValueJumlahMakananBahanPokok);
            tvSatuan = itemView.findViewById(R.id.tvValueSatuanMakananBahanPokok);
            btnHapus = itemView.findViewById(R.id.btnTambahMakananBahanPokok);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}