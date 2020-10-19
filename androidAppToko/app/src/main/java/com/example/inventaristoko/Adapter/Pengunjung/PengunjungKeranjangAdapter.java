package com.example.inventaristoko.Adapter.Pengunjung;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Adapter.Penjualan.PenjualanDetailAdapter;
import com.example.inventaristoko.Model.Penjualan.PenjualanDetail;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Pengunjung.PengunjungActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.Preferences;
import com.example.inventaristoko.Utils.VolleyAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PengunjungKeranjangAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "PengunjungKeranjangAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<PenjualanDetail> mPenjualanDetailList;

    public PengunjungKeranjangAdapter(List<PenjualanDetail> penjualanDetailList) {
        mPenjualanDetailList = penjualanDetailList;
    }

    public void setCallback(PengunjungKeranjangAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new PengunjungKeranjangAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pengunjung_cart, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new PengunjungKeranjangAdapter.EmptyViewHolder(
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
        @BindView(R.id.tvJumlahDetailPengunjung)
        TextView tvJumlahDetailPengunjung;

        @BindView(R.id.tvNamaDetailPengunjung)
        TextView tvNamaDetailPengunjung;

        @BindView(R.id.tvCatatanDetailPengunjung)
        TextView tvCatatanDetailPengunjung;

        @BindView(R.id.tvTotalHargaDetailPengunjung)
        TextView tvTotalHargaDetailPengunjung;

        @BindView(R.id.btnHapusKeranjang)
        Button btnHapusKeranjang;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvJumlahDetailPengunjung.setText("");
            tvNamaDetailPengunjung.setText("");
            tvCatatanDetailPengunjung.setText("");
            tvTotalHargaDetailPengunjung.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final PenjualanDetail mPenjualanDetail = mPenjualanDetailList.get(position);

            tvNamaDetailPengunjung.setText(mPenjualanDetail.getNamaDetailPenjualan());
            tvJumlahDetailPengunjung.setText(mPenjualanDetail.getJumlahDetailPenjualan() + "x");
            tvCatatanDetailPengunjung.setText(mPenjualanDetail.getCatatanDetailPenjualan());

            int amount = Integer.parseInt(mPenjualanDetail.getJumlahDetailPenjualan());
            int price = Integer.parseInt(mPenjualanDetail.getHargaDetailMakanan());
            int totalPrice = amount * price;
            mPenjualanDetail.setTotalHargaDetailPenjualan(String.valueOf(totalPrice));

            tvTotalHargaDetailPengunjung.setText(CommonUtils.currencyFormat(mPenjualanDetail.getTotalHargaDetailPenjualan()));

            btnHapusKeranjang.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.confirmation_dialog_delete);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.label_yes, (dialog, which) -> {
                    VolleyAPI volleyAPI = new VolleyAPI(v.getContext());

                    Map<String, String> params = new HashMap<>();
                    params.put("cart_id", mPenjualanDetail.getIdDetailPenjualan());

                    volleyAPI.putRequest(MyConstants.PENGUNJUNG_DELETE_SELECTED_CART_ACTION, params, result -> {
                        try {
                            JSONObject resultJSON = new JSONObject(result);
                            mPenjualanDetailList.remove(position);
                            notifyDataSetChanged();
                            CommonUtils.showToast(v.getContext(), resultJSON.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                });
                builder.setNegativeButton(R.string.label_no, (dialog, which) -> {
                });
                builder.show();
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
