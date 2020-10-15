package com.example.inventaristoko.Adapter.Pengunjung;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Makanan.Makanan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Makanan.MakananDetailActivity;
import com.example.inventaristoko.Screens.Pengunjung.PengunjungActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;
import com.example.inventaristoko.Utils.MyConstants;
import com.example.inventaristoko.Utils.VolleyAPI;
import com.example.inventaristoko.Utils.VolleyCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PengunjungMakananAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "PengunjungMakananAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private Context context;

    private PengunjungMakananAdapter.Callback mCallback;
    private List<Makanan> mMakananList;

    public PengunjungMakananAdapter(List<Makanan> pengunjungMakananList) {
        mMakananList = pengunjungMakananList;
    }

    public void setCallback(PengunjungMakananAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                context = parent.getContext();

                return new PengunjungMakananAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_makanan_pengunjung, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new PengunjungMakananAdapter.EmptyViewHolder(
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

    public void addItems(List<Makanan> pengunjungMakananList) {
        mMakananList.addAll(pengunjungMakananList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvNamaMakananPengunjung)
        TextView tvNamaMakananPengunjung;

        @BindView(R.id.tvHargaMakananPengunjung)
        TextView tvHargaMakananPengunjung;

        @BindView(R.id.ivGambarMakananPengunjung)
        ImageView ivGambarMakananPengunjung;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvNamaMakananPengunjung.setText("");
            tvHargaMakananPengunjung.setText("");
            ivGambarMakananPengunjung.setImageBitmap(null);
        }

        public void onBind(int position) {
            super.onBind(position);

            final Makanan mMakanan = mMakananList.get(position);

            tvNamaMakananPengunjung.setText(mMakanan.getNamaMakanan());
            tvHargaMakananPengunjung.setText(CommonUtils.currencyFormat(mMakanan.getHargaMakanan()));

            VolleyAPI volleyAPI = new VolleyAPI(context);
            Map<String, String> params = new HashMap<>();
            params.put("makanan_id", mMakanan.getIdMakanan());

            volleyAPI.getRequest(MyConstants.MAKANAN_GET_IMAGE_DETAIL_ACTION, params, result -> {
                String decodeImage = result;
                if (decodeImage != null) {
                    byte[] decodedString = Base64.decode(decodeImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivGambarMakananPengunjung.setImageBitmap(decodedByte);
                }
            });

            itemView.setOnClickListener(v -> {
                if (mMakanan.getNamaMakanan() != null) {
//                    try {
//                        Intent intent = new Intent (v.getContext(), PengunjungActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("idMakanan", mMakanan.getIdMakanan());
//                        bundle.putString("namaMakanan", mMakanan.getNamaMakanan());
//                        bundle.putString("hargaMakanan", mMakanan.getHargaMakanan());
//                        intent.putExtras(bundle);
//                        v.getContext().startActivity(intent);
//                    } catch (Exception e) {
//                        Log.e(TAG, "onClick: Url is not correct");
//                    }
                    CommonUtils.showToast(v.getContext(), mMakanan.getNamaMakanan());
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