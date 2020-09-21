package com.example.inventaristoko.Adapter.BahanPokok;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventaristoko.Model.BahanPokok.BahanPokokFood;
import com.example.inventaristoko.R;
import com.example.inventaristoko.Utils.BaseViewHolder;
import com.example.inventaristoko.Utils.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BahanPokokFoodAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "BahanPokokFoodAdapter";
    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private BahanPokokFoodAdapter.Callback mCallback;
    private List<BahanPokokFood> mBahanPokokFoodList;

    public BahanPokokFoodAdapter(List<BahanPokokFood> bahanPokokFoodList) {
        mBahanPokokFoodList = bahanPokokFoodList;
    }

    public void setCallback(BahanPokokFoodAdapter.Callback callback) {
        mCallback = callback;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new BahanPokokFoodAdapter.ViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bahan_pokok_makanan, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new BahanPokokFoodAdapter.EmptyViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_kosong_detail, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mBahanPokokFoodList != null && mBahanPokokFoodList.size() > 0) {
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
        if (mBahanPokokFoodList != null && mBahanPokokFoodList.size() > 0) {
            return mBahanPokokFoodList.size();
        } else {
            return 1;
        }
    }

    public void addItems(List<BahanPokokFood> bahanPokokFoodList) {
        mBahanPokokFoodList.addAll(bahanPokokFoodList);
        notifyDataSetChanged();
    }

    public interface Callback {
        void onEmptyViewRetryClick();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tvBahanPokokId)
        TextView tvBahanPokokId;

        @BindView(R.id.tvBahanPokokNamaMakanan)
        TextView tvBahanPokokNamaMakanan;

        @BindView(R.id.tvBahanPokokHargaMakanan)
        TextView tvBahanPokokHargaMakanan;

        @BindView(R.id.tvBahanPokokTanggalMakanan)
        TextView tvBahanPokokTanggalMakanan;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
            tvBahanPokokId.setText("");
            tvBahanPokokNamaMakanan.setText("");
            tvBahanPokokHargaMakanan.setText("");
            tvBahanPokokTanggalMakanan.setText("");
        }

        public void onBind(int position) {
            super.onBind(position);

            final BahanPokokFood mBahanPokokFood = mBahanPokokFoodList.get(position);

            tvBahanPokokId.setText(mBahanPokokFood.getId());
            tvBahanPokokNamaMakanan.setText(mBahanPokokFood.getStapleFoodName());
            tvBahanPokokHargaMakanan.setText(CommonUtils.currencyFormat(mBahanPokokFood.getStapleFoodPrice()));
            tvBahanPokokTanggalMakanan.setText(mBahanPokokFood.getStapleFoodCreatedAt());

//            itemView.setOnClickListener(v -> {
//                if (mBahanPokokFood.getStapleFoodId() != null) {
//                    Toast.makeText(v.getContext(), mBahanPokokFood.getStapleFoodName(), Toast.LENGTH_SHORT).show();
//                }
//            });
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
