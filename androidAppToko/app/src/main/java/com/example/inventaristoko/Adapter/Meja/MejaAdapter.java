package com.example.inventaristoko.Adapter.Meja;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Meja.Meja;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Meja.MejaDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MejaAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "MejaAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private MejaAdapter.Callback mCallback;
    private List<Meja> mMejaList;

    public MejaAdapter(List<Meja> mejaList) {
        mMejaList = mejaList;
    }

    public void setCallback(MejaAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new MejaAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_meja, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new MejaAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_kosong, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mMejaList != null && mMejaList.size() > 0) {
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
        if (mMejaList != null && mMejaList.size() > 0) {
            return mMejaList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Meja> kategoriList) {
        mMejaList.addAll(kategoriList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvIdMeja)
        TextView tvIdMeja;

        @BindView(R.id.tvNamaMeja)
        TextView tvNamaMeja;

        @BindView(R.id.ivQrCode)
        ImageView ivQrCode;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvIdMeja.setText("");
            tvNamaMeja.setText("");
            ivQrCode.setImageBitmap(null);
        }

        public void onBind(int position) {
            super.onBind(position);

            final Meja mMeja = mMejaList.get(position);

            tvNamaMeja.setText(mMeja.getNamaMeja());
            tvIdMeja.setText(mMeja.getIdMeja());

            String text = tvIdMeja.getText().toString();
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,100,100);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                ivQrCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(v -> {
                if (mMeja.getIdMeja() != null) {
                    try {
                        Intent intent = new Intent (v.getContext(), MejaDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("idMeja", mMeja.getIdMeja());
                        bundle.putString("namaMeja", mMeja.getNamaMeja());
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
