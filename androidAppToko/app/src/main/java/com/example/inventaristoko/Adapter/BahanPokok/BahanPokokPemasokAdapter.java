package com.example.inventaristoko.Adapter.BahanPokok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.BahanPokok.BahanPokokPemasok;
import com.example.inventaristoko.R;

import java.util.ArrayList;

public class BahanPokokPemasokAdapter extends RecyclerView.Adapter<BahanPokokPemasokAdapter.RvViewHolder> {
    Context context;
    ArrayList<BahanPokokPemasok> bahanPokokPemasoks;
    Onclick onclick;
    View view;

    public interface Onclick {
        void onEvent(BahanPokokPemasok bahanPokokPemasok, int pos);
    }

    public BahanPokokPemasokAdapter(Context context, ArrayList<BahanPokokPemasok> bahanPokokPemasoks, Onclick onclick) {
        this.context = context;
        this.bahanPokokPemasoks = bahanPokokPemasoks;
        this.onclick = onclick;
    }

    @Override
    public BahanPokokPemasokAdapter.RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.list_pemasok_bahan_pokok, parent, false);
        RvViewHolder rvViewHolder = new RvViewHolder(view);
        return rvViewHolder;
    }

    @Override
    public void onBindViewHolder(BahanPokokPemasokAdapter.RvViewHolder holder, final int position) {
        final BahanPokokPemasok bahanPokokPemasok = bahanPokokPemasoks.get(position);

        if (bahanPokokPemasok.getName() != null) {
            holder.tvNama.setText(bahanPokokPemasok.getName());
        }

        holder.btnHapus.setOnClickListener(view -> {
            bahanPokokPemasoks.remove(position);
            notifyDataSetChanged();
        });

        holder.llItem.setOnClickListener(view -> onclick.onEvent(bahanPokokPemasok, position));
    }

    @Override
    public int getItemCount() {
        return bahanPokokPemasoks.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;
        TextView tvNama;
        Button btnHapus;

        public RvViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvValueNamaPemasokBahanPokok);
            btnHapus = itemView.findViewById(R.id.btnHapusPemasokBahanPokok);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
