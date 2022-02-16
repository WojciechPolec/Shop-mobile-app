package com.company.sklep.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import com.company.sklep.R;
import com.company.sklep.database.UserEntity;
import com.company.sklep.viewmodel.AuthViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LoginFragment extends Fragment {

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView dontHaveAcc = view.findViewById(R.id.tv_dontHaveAcc);
        Button loginButton = view.findViewById(R.id.button_login);
        EditText emailInput = view.findViewById(R.id.editText_email);
        EditText passwordInput = view.findViewById(R.id.editText_password);

        navController = NavHostFragment.findNavController(this);

        // ustawiamy viewModel dla tego fragmentu
        AuthViewModel viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // listener dotyku tekstu "nie mam konta" który przenosi do okna rejestracji
        dontHaveAcc.setOnClickListener((v)->{
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });

        loginButton.setOnClickListener((v) -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            if(viewModel.isLoginDataNotEmpty(email, password)){
                // hasło i email nie są puste więc sprawdzamy w bazie danych czy taki
                // użytkownik istnieje
                viewModel.checkLoginCredential(email, password, new AuthViewModel.AuthCredentialsListener<Boolean, UserEntity>() {
                    @Override
                    public void onFinishChecking(boolean isCredentialsOK, UserEntity user) {
                        // listener AuthCredentialsListener zwraca nam nie jako kod
                        // w wątku pobocznym (operacja na bazie danych) to musimy wymusic
                        // pokazanie dialogu lub otwarcie nowego nowego fragmentu w wątku główny w
                        // przeciwnym wypadku aplikacja sie zamknie z powodu próby wykonania
                        // akcji dotyczącej interfejsu użytkownika z wątka innego niż główny
                        getActivity().runOnUiThread(()->{
                            if(isCredentialsOK){
                                Bundle args = new Bundle();
                                args.putLong("userId", user.id);
                                navController.navigate(R.id.action_login_to_home, args);
                            }else{
                                MaterialAlertDialogBuilder alert = viewModel.createDialog(
                                        getContext(),
                                        getString(R.string.user_not_found),
                                        getString(R.string.login_error_title)
                                );
                                alert.show();
                            }
                        });
                    }
                });
            }else{
                // pokaż informację że pole email lub hasło nie może być puste
                MaterialAlertDialogBuilder alert = viewModel.createDialog(
                        getContext(),
                        getString(R.string.email_password_empty),
                        getString(R.string.login_error_title)
                );
                alert.show();
            }
        });
    }
}
