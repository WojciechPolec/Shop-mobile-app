package com.company.sklep.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.company.sklep.database.AppDatabase;
import com.company.sklep.database.TransactionDao;
import com.company.sklep.database.TransactionEntity;
import com.company.sklep.database.UserDao;
import com.company.sklep.database.UserEntity;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {

    private final UserDao userDao;
    private final TransactionDao transactionDao;

    public interface DataReadyListener{
        void onDataReady(UserEntity user);
    }

    public ProfileViewModel(@NonNull Application application) {
        super(application);

        AppDatabase mDatabase = AppDatabase.getDatabase(application);
        userDao = mDatabase.userDao();
        transactionDao = mDatabase.transactionDao();
    }

    public LiveData<List<TransactionEntity>> getAllTransactions(Long userId){
        return transactionDao.getAllItems(userId);
    }

    public void getUserData(Long userId, DataReadyListener listener){
        AppDatabase.databaseWriterExecutor.execute(()->{
            List<UserEntity> userList = userDao.getUser(userId);
            if(userList.size() > 0){
                listener.onDataReady(userList.get(0));
            }
        });
    }
}
