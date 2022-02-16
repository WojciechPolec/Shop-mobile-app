package com.company.sklep.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.company.sklep.HomeListAdapter;
import com.company.sklep.R;
import com.company.sklep.database.ShopItem;
import com.company.sklep.viewmodel.HomeViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;

public class HomeFragment extends Fragment {

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
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

        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        navController = NavHostFragment.findNavController(this);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        RecyclerView recyclerView = view.findViewById(R.id.rv_item_list);
        toolbar.inflateMenu(R.menu.menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.profile) {
                    Bundle args = new Bundle();
                    args.putLong("userId", loggedUser);
                    navController.navigate(R.id.action_homeFragment_to_profileFragment, args);
                }

                return false;
            }
        });

        // tworzymy adapter i ustawiamy go dla RecycleView czyli naszej listy przedmiotów w sklepie
        HomeListAdapter adapter = new HomeListAdapter(requireContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        viewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<ShopItem>>() {
            @Override
            public void onChanged(List<ShopItem> shopItems) {
                adapter.submitList(shopItems);
            }
        });

        adapter.setClickListener((id)->{
            Bundle args = new Bundle();
            args.putInt("itemId", id);
            args.putLong("userId", loggedUser);
            navController.navigate(R.id.action_homeFragment_to_itemDetailFragment, args);
        });
    }
}
