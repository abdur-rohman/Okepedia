package com.bootcamp.okepedia.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bootcamp.okepedia.R;
import com.bootcamp.okepedia.databinding.ItemProductBinding;
import com.bootcamp.okepedia.entities.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<ProductEntity> products = new ArrayList<>();

    public interface OnProductListener {
        void onEdit(ProductEntity product);

        void onDelete(ProductEntity product);
    }

    private OnProductListener onProductListener;

    public void setOnProductListener(OnProductListener onProductListener) {
        this.onProductListener = onProductListener;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_product,
                parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdapter.ViewHolder holder, int position) {
        holder.bindData(products.get(position), onProductListener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;

        public ViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bindData(ProductEntity model, OnProductListener onProductListener) {
            binding.setModel(model);
            binding.ivEdit.setOnClickListener(view -> onProductListener.onEdit(model));
            binding.ivDelete.setOnClickListener(view -> onProductListener.onDelete(model));
        }
    }
}
