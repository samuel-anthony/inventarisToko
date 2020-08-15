package com.example.inventaristoko.Adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Penjualan;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.HomeActivity;
import com.example.inventaristoko.Screens.PenjualanDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PenjualanAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "PenjualanAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<Penjualan> mPenjualanList;

    public PenjualanAdapter(List<Penjualan> penjualanList) {
        mPenjualanList = penjualanList;
    }

    public void setCallback(PenjualanAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_empty_view, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mPenjualanList != null && mPenjualanList.size() > 0) {
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
        if (mPenjualanList != null && mPenjualanList.size() > 0) {
            return mPenjualanList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Penjualan> penjualanList) {
        mPenjualanList.addAll(penjualanList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.urutan)
        TextView urutanTextView;

        @BindView(R.id.title)
        TextView titleTextView;

        @BindView(R.id.subTitle)
        TextView subTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            titleTextView.setText("");
            subTextView.setText("");
            urutanTextView.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Penjualan mPenjualan = mPenjualanList.get(position);

            urutanTextView.setText(mPenjualan.getUrutan());

            if (mPenjualan.getRefNo() != null) {
                titleTextView.setText(mPenjualan.getRefNo());
            }

            if (mPenjualan.getStatus() != null) {
                subTextView.setText(mPenjualan.getStatus());
            }

            itemView.setOnClickListener(v -> {
                if (mPenjualan.getRefNo() != null) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(mPenjualan.getRefNo()));
                        itemView.getContext().startActivity(intent);
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