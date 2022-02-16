package com.company.sklep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.company.sklep.database.TransactionEntity;

public class TransactionListAdapter extends ListAdapter<TransactionEntity, TransactionListAdapter.TransactionViewHolder> {

    private final LayoutInflater mLayoutInflater;

    public TransactionListAdapter(Context context){
        super(DIFF_CALLBACK);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.transaction_item_layout, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionEntity item = getItem(position);

        holder.itemName.setText(item.itemName);
        holder.price.setText(item.price.toString());
        holder.city.setText(item.city + ", " + item.postCode);
        holder.street.setText(item.street + ", "+item.buildingNumber);
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static final DiffUtil.ItemCallback<TransactionEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<TransactionEntity>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull TransactionEntity oldItem, @NonNull TransactionEntity newItem) {
                    return oldItem.id == newItem.id;
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull TransactionEntity oldItem, @NonNull TransactionEntity newItem) {
                    return oldItem.itemName.equals(newItem.itemName);
                }
            };

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView itemName;
        TextView street;
        TextView price;
        TextView city;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tv_item_name);
            street = itemView.findViewById(R.id.tv_street);
            price = itemView.findViewById(R.id.tv_price);
            city = itemView.findViewById(R.id.tv_city);
        }
    }
}
