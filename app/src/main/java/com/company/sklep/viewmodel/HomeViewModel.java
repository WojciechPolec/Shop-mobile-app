package com.company.sklep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.company.sklep.database.AppDatabase;
import com.company.sklep.database.ShopItem;
import com.company.sklep.database.ShopItemDao;
import com.company.sklep.database.UserDao;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final ShopItemDao shopItemDao;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        // zainicjowanie bazy danych i utworzenie referencji do taskDao który zajmuję się
        // komunikacją z bazą danych
        AppDatabase mDatabase = AppDatabase.getDatabase(application);
        shopItemDao = mDatabase.shopItemDao();
    }

    public LiveData<List<ShopItem>> getAllItems(){
        return shopItemDao.getAllItems();
    }
}
