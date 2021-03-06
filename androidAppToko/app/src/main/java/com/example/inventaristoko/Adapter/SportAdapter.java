package com.example.inventaristoko.Adapter;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.Sport;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Screens.Penjualan.PenjualanDetailActivity;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SportAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "SportAdapter";
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    private Callback mCallback;
    private List<Sport> mSportList;

    public SportAdapter(List<Sport> sportList) {
        mSportList = sportList;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_penjualan, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new EmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kosong, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mSportList != null && mSportList.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public int getItemCount() {
        if (mSportList != null && mSportList.size() > 0) {
            return mSportList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<Sport> sportList) {
        mSportList.addAll(sportList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {

//        @BindView(R.id.thumbnail)
//        ImageView coverImageView;

        @BindView(R.id.tvId)
        TextView titleTextView;

        @BindView(R.id.tvStatusPenjualan)
        TextView newsTextView;
//
//        @BindView(R.id.newsInfo)
//        TextView infoTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
//            coverImageView.setImageDrawable(null);
            titleTextView.setText("");
            newsTextView.setText("");
//            infoTextView.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final Sport mSport = mSportList.get(position);

//            if (mSport.getImageUrl() != null) {
//                Glide.with(itemView.getContext())
//                        .load(mSport.getImageUrl())
//                        .into(coverImageView);
//            }

            if (mSport.getTitle() != null) {
                titleTextView.setText(mSport.getTitle());
            }

            if (mSport.getSubTitle() != null) {
                newsTextView.setText(mSport.getSubTitle());
            }

//            if (mSport.getInfo() != null) {
//                infoTextView.setText(mSport.getInfo());
//            }

            itemView.setOnClickListener(v -> {
                if (mSport.getTitle() != null) {
                    cobaPindah(v, mSport.getTitle());
                }
            });
        }
    }

    public class EmptyViewHolder extends BaseViewHolder {
//        @BindView(R.id.btnCobaUlang)
//        TextView btnCobaUlang;
//
//        EmptyViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            btnCobaUlang.setOnClickListener(v -> mCallback.onEmptyViewRetryClick());
//        }
//
//        @Override
//        protected void clear() {
//        }

        EmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {
        }
    }

    private void cobaPindah(View v, String refNo) {
        CommonUtils.showLoading(v.getContext());
        new Handler().postDelayed(() -> {
            Intent intent = new Intent (v.getContext(), PenjualanDetailActivity.class);
            v.getContext().startActivity(intent);

            Toast.makeText(v.getContext(), refNo, Toast.LENGTH_SHORT).show();
        }, 2000);
    }
}
