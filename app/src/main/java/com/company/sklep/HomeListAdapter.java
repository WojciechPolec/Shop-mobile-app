package com.company.sklep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.company.sklep.database.ShopItem;

public class HomeListAdapter extends ListAdapter<ShopItem, HomeListAdapter.ItemViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private ClickListener mListener;

    // tworzymy interfejs do komunikacji z HomeFragment
    // będziemy go wywoływać gdy użytkownik kliknie w przedmiot na liście
    // a w MainActivity gdy event zostanie wywołany obsłużymy go wykonując operacje na bazie danych
    public interface ClickListener {
        void OnClick(Integer id);
    }

    public HomeListAdapter(Context context){
        super(DIFF_CALLBACK);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setClickListener(ClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.shop_item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ShopItem item = getItem(position);

        holder.title.setText(item.title);
        holder.body.setText(item.body);
        holder.image.setImageResource(item.photoId);
        holder.card.setOnClickListener((v)-> mListener.OnClick(item.id));
        holder.price.setText(item.price.toString());
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static final DiffUtil.ItemCallback<ShopItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ShopItem>() {
                @Override
                public boolean areItemsTheSame(
                        @NonNull ShopItem oldItem, @NonNull ShopItem newItem) {
                    return oldItem.id == newItem.id;
                }
                @Override
                public boolean areContentsTheSame(
                        @NonNull ShopItem oldItem, @NonNull ShopItem newItem) {
                    return oldItem.title.equals(newItem.title);
                }
            };

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView body;
        TextView price;
        ImageView image;
        CardView card;

        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            body = itemView.findViewById(R.id.tv_body);
            image = itemView.findViewById(R.id.imageView);
            card = itemView.findViewById(R.id.card);
            price = itemView.findViewById(R.id.tv_price);
        }
    }
}

