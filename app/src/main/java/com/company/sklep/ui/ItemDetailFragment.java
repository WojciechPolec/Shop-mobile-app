package com.company.sklep.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.company.sklep.R;
import com.company.sklep.database.ShopItem;
import com.company.sklep.viewmodel.DetailsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.w3c.dom.Text;

public class ItemDetailFragment extends Fragment {

    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // odczytujemy id przedmiotu który został przesłany z HomeFragment
        // i odczytujemy id użytkownika, który jest teraz zalogowany
        final int itemId;
        final Long userId;
        if(getArguments() != null){
            itemId = getArguments().getInt("itemId");
            userId = getArguments().getLong("userId");
        }else{
            itemId = -1;
            userId = -1L;
            // jeśli z jakiegoś powodu nie otrzymaliśmy informacji o przedmiocie, powracamy
            // do poprzedniego ekranu
            ShowDialog(getString(R.string.error_title), getString(R.string.unexpected_error));
        }

        navController = NavHostFragment.findNavController(this);

        TextView title = view.findViewById(R.id.tv_title);
        TextView price = view.findViewById(R.id.tv_price);
        ImageView image = view.findViewById(R.id.imageView);
        TextView body = view.findViewById(R.id.tv_body);
        Button buyButton = view.findViewById(R.id.bt_buy);

        DetailsViewModel viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        viewModel.getItemDetails(itemId, new DetailsViewModel.ItemReadyListener() {
            @Override
            public void onReady(boolean isExist, ShopItem item) {
                requireActivity().runOnUiThread(()->{
                    if(isExist){
                        title.setText(item.title);
                        price.setText(item.price.toString());
                        image.setImageResource(item.photoId);
                        body.setText(item.body);
                    }else{
                        ShowDialog(getString(R.string.error_title), getString(R.string.unexpected_error));
                    }
                });
            }
        });

        buyButton.setOnClickListener((v)->{
            Bundle args = new Bundle();
            args.putInt("itemId", itemId);
            args.putLong("userId", userId);
            NavHostFragment.findNavController(this).navigate(R.id.action_itemDetailFragment_to_paymentDetailsFragment, args);
        });
    }

    public void ShowDialog(String title, String message){
        MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(requireContext());
        alertBuilder.setMessage(message);
        alertBuilder.setTitle(title);
        alertBuilder.setPositiveButton(R.string.ok, (v, i)->{
            navController.popBackStack();
        });
        alertBuilder.show();
    }
}
