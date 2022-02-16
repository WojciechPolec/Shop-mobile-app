package com.company.sklep.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.company.sklep.R;
import com.company.sklep.TransactionListAdapter;
import com.company.sklep.database.TransactionEntity;
import com.company.sklep.database.UserEntity;
import com.company.sklep.viewmodel.ProfileViewModel;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // odczytujemy dane użytkownika przesłane z okna logowania
        final Long loggedUser;
        if(getArguments() != null){
            // jako że przesłalismy obiekt zserializowany to przy odczycie aby otrzymać
            // obiekt klasy musimy użyć castowania na klasę UserEntity
            loggedUser = getArguments().getLong("userId");
        }else{
            loggedUser = -1L;
            // jeśli z jakiegoś powodu nie otrzymaliśmy informacji o użytkowniku, zamykamy aplikację
            requireActivity().finish();
        }

        RecyclerView rvListOfBoughtItems = view.findViewById(R.id.rv_bought_items);
        TextView name = view.findViewById(R.id.tv_name);
        TextView surname = view.findViewById(R.id.tv_surname);
        TextView email = view.findViewById(R.id.tv_email);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener((v)->{
            NavHostFragment.findNavController(this).popBackStack();
        });

        rvListOfBoughtItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        TransactionListAdapter adapter = new TransactionListAdapter(requireContext());
        rvListOfBoughtItems.setAdapter(adapter);

        ProfileViewModel viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getUserData(loggedUser, new ProfileViewModel.DataReadyListener() {
            @Override
            public void onDataReady(UserEntity user) {
                requireActivity().runOnUiThread(()->{
                    name.setText(user.name);
                    surname.setText(user.surname);
                    email.setText(user.email);
                });
            }
        });

        viewModel.getAllTransactions(loggedUser).observe(getViewLifecycleOwner(), new Observer<List<TransactionEntity>>() {
            @Override
            public void onChanged(List<TransactionEntity> transactionEntities) {
                adapter.submitList(transactionEntities);
            }
        });
    }
}
