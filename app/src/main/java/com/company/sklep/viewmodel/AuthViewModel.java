package com.company.sklep.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.company.sklep.R;
import com.company.sklep.database.AppDatabase;
import com.company.sklep.database.UserDao;
import com.company.sklep.database.UserEntity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class AuthViewModel extends AndroidViewModel {
    private final UserDao userDao;

    public interface AuthCredentialsListener<Boolean, UserEntity> {
        void onFinishChecking(boolean isCredentialsOK, UserEntity user);
    }

    public AuthViewModel(@NonNull Application application) {
        super(application);

        // zainicjowanie bazy danych i utworzenie referencji do taskDao który zajmuję się
        // komunikacją z bazą danych
        AppDatabase mDatabase = AppDatabase.getDatabase(application);
        userDao = mDatabase.userDao();
    }

    // zwraca true jeśli email i hasło nie są puste
    public boolean isLoginDataNotEmpty(String email, String password){
        return !email.isEmpty() && !password.isEmpty();
    }

    public boolean isRegisterDataNotEmpty(String name, String surname, String email, String password, String repeatedPassword){
        return !name.isEmpty() && !surname.isEmpty() && !email.isEmpty() && !password.isEmpty() && !repeatedPassword.isEmpty();
    }

    // sprawdza dane wprowadzone podczas logowania
    public void checkLoginCredential(String email, String password, AuthCredentialsListener<Boolean, UserEntity> listener){
        // do operacji na bazie danych używamy ExecutorService o nazwie databaseWriterExecutor
        // zaimplementowany w klasie AppDatabase który pozwoli nam operować na bazie danych
        // w wątku pobocznym, dzięki czemu ui nie zostanie zawieszone
        AppDatabase.databaseWriterExecutor.execute(()->{
                List<UserEntity> userList = userDao.getUser(email, password);
                if(userList.size() < 1){
                    System.out.println(userList.size());
                    // baza zwróciła pustą listę, a to oznacza że nie ma użytkownika o podanych danych
                    // wywołujemy listener który poinformuje fragment o wynikach przeszukiwania bazy danych
                    listener.onFinishChecking(false, null);
                }else{
                    // baza zwróciła użytkownika, następuje logowanie do sklepu
                    // wywołujemy listener który poinformuje fragment o wynikach przeszukiwania bazy danych
                    listener.onFinishChecking(true, userList.get(0));
                }
            }
        );
    }

    // zwraca MaterialAlertDialogBuilder który umożliwia pokazanie na ekranie okna z wiadomością
    public MaterialAlertDialogBuilder createDialog(Context context, String message, String title){
        MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(context);
        alertBuilder.setMessage(message);
        alertBuilder.setTitle(title);
        // za listener przekazujemy null ponieważ oczekujemy że ten diaog zrobi rzecz domyślną
        // czyli sam się zamknie i nic ponad to
        alertBuilder.setPositiveButton(R.string.ok, null);
        return alertBuilder;
    }

    public void registerNewUser(UserEntity user){
        AppDatabase.databaseWriterExecutor.execute(()->{
            userDao.createNewUser(user);
        });
    }

    // zwraca true jeśli hasło i hasło powtórzone są takie same
    public boolean isPasswordsSame(String password, String repeatedPassword){
        return password.equals(repeatedPassword);
    }
}
