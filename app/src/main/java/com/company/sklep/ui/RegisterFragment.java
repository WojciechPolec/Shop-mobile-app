package com.company.sklep.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.company.sklep.R;
import com.company.sklep.database.UserEntity;
import com.company.sklep.viewmodel.AuthViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RegisterFragment extends Fragment {

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        // ustawiamy viewModel dla tego fragmentu aby podczas np. obrotu ekranu dane zostały zachowane
        AuthViewModel viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        TextView haveAccountText = view.findViewById(R.id.tv_haveAcc);
        Button registerButton = view.findViewById(R.id.button_register);
        EditText nameText = view.findViewById(R.id.editText_name);
        EditText surname = view.findViewById(R.id.editText_surname);
        EditText email = view.findViewById(R.id.editText_email);
        EditText password = view.findViewById(R.id.editText_password);
        EditText repeatedPassword = view.findViewById(R.id.editText_passwordRepeat);

        // listener dotyku tekstu "mam konto" który przenosi do okna logowania
        haveAccountText.setOnClickListener((v) ->{
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });

        // przycisk rejestracji
        registerButton.setOnClickListener((v) ->{
            // najpierw sprawdzamy czy żadne pole nie było puste
            if(viewModel.isRegisterDataNotEmpty(
                    nameText.getText().toString(),
                    surname.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString(),
                    repeatedPassword.getText().toString()
            )){
                // potem sprawdzamy czy pole hasło i powtórz hasło zgadza się
                if(viewModel.isPasswordsSame(password.getText().toString(), repeatedPassword.getText().toString())){
                    UserEntity user = new UserEntity(
                            nameText.getText().toString(),
                            surname.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString()
                    );
                    // rejestrujemy użytkownika (czyli dodajemy go do bazy danych)
                    viewModel.registerNewUser(user);
                    Toast.makeText(getContext(), R.string.account_createed, Toast.LENGTH_SHORT).show();
                    // przenosimy użytkownika do okna logowania aby zalogował się do utworzonego konta
                    navController.navigate(R.id.action_registerFragment_to_loginFragment);
                }else{
                    MaterialAlertDialogBuilder dialog = viewModel.createDialog(
                            getContext(),
                            getString(R.string.passwords_are_not_same),
                            getString(R.string.register_error)
                    );
                    dialog.show();
                }
            }else{
                MaterialAlertDialogBuilder dialog = viewModel.createDialog(
                        getContext(),
                        getString(R.string.empty_fields),
                        getString(R.string.register_error)
                );
                dialog.show();
            }
        });

    }
}
