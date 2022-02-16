package com.company.sklep.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.company.sklep.R;
import com.company.sklep.database.ShopItem;
import com.company.sklep.database.TransactionEntity;
import com.company.sklep.database.UserEntity;
import com.company.sklep.viewmodel.PaymentDetailsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PaymentDetailsFragment extends Fragment {

    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // odczytujemy id przedmiotu który został przesłany z HomeFragment
        // i odczytujemy id użytkownika, który jest teraz zalogowany
        final int itemId;
        final long userId;
        if(getArguments() != null){
            itemId = getArguments().getInt("itemId");
            userId = getArguments().getLong("userId");
        }else{
            itemId = -1;
            userId = -1L;
            // jeśli z jakiegoś powodu nie otrzymaliśmy informacji o przedmiocie, wyświetlamy info
            // i wracamy do poprzedniego ekranu
            ShowDialog(getString(R.string.error_title), getString(R.string.unexpected_error), true);
        }

        navController = NavHostFragment.findNavController(this);

        PaymentDetailsViewModel viewModel = new ViewModelProvider(this).get(PaymentDetailsViewModel.class);

        TextView itemName = view.findViewById(R.id.tv_item_name);
        TextView itemPrice = view.findViewById(R.id.tv_price);
        TextView userName = view.findViewById(R.id.tv_name);
        TextView surname = view.findViewById(R.id.tv_surname);
        TextView email = view.findViewById(R.id.tv_email);
        ImageView image = view.findViewById(R.id.imageView);
        EditText city = view.findViewById(R.id.et_city);
        EditText street = view.findViewById(R.id.et_street);
        EditText buildingNUmber = view.findViewById(R.id.et_building_number);
        EditText postalCode = view.findViewById(R.id.et_postal_code);
        Button buyButton = view.findViewById(R.id.bt_buy_it);
        Button cancelButton = view.findViewById(R.id.bt_cancel);

        viewModel.getItemAndUserData(itemId, userId, new PaymentDetailsViewModel.DataReadyListener() {
            @Override
            public void onDataReady(boolean isDataOk, UserEntity user, ShopItem item) {
                requireActivity().runOnUiThread(()->{
                    if(isDataOk){
                        itemName.setText(item.title);
                        itemPrice.setText(item.price.toString());
                        userName.setText(user.name);
                        surname.setText(user.surname);
                        email.setText(user.email);
                        image.setImageResource(item.photoId);
                    }else{
                        ShowDialog(getString(R.string.error_title), getString(R.string.unexpected_error), true);
                    }
                });
            }
        });

        buyButton.setOnClickListener((v)->{
            // sprawdzamy czy żadne pole nie pozostało puste
            if(viewModel.isShippingDataOk(
                    city.getText().toString(),
                    buildingNUmber.getText().toString(),
                    street.getText().toString(),
                    postalCode.getText().toString()
            )){
                //tworzymy obiekt transakcji
                TransactionEntity item = new TransactionEntity();
                item.buildingNumber = buildingNUmber.getText().toString();
                item.city = city.getText().toString();
                item.street = street.getText().toString();
                item.postCode = postalCode.getText().toString();
                item.userId = userId;
                item.itemName = itemName.getText().toString();
                item.price = itemPrice.getText().toString();

                // zapisujemy transakcje do bazy danych i przenosimy użytkownika do ekranu głównego
                viewModel.saveTransaction(item);
                Toast.makeText(requireContext(), R.string.item_has_been_bought, Toast.LENGTH_LONG).show();
                Bundle args = new Bundle();
                args.putLong("userId", userId);
                navController.navigate(R.id.action_paymentDetailsFragment_to_homeFragment, args);
            }else{
                ShowDialog(getString(R.string.error_title), getString(R.string.empty_fields), false);
            }
        });

        cancelButton.setOnClickListener((v)->{
            navController.popBackStack();
        });
    }

    public void ShowDialog(String title, String message, Boolean closeAfter){
        MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(requireContext());
        alertBuilder.setMessage(message);
        alertBuilder.setTitle(title);
        if(closeAfter){
            alertBuilder.setPositiveButton(R.string.ok, (v, i)->{
                navController.popBackStack();
            });
        }else{
            alertBuilder.setPositiveButton(R.string.ok, null);
        }

        alertBuilder.show();
    }
}
