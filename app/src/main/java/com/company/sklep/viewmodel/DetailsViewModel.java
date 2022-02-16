package com.company.sklep.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.company.sklep.database.AppDatabase;
import com.company.sklep.database.ShopItem;
import com.company.sklep.database.ShopItemDao;

import java.util.List;

public class DetailsViewModel extends AndroidViewModel {

    public interface ItemReadyListener{
        void onReady(boolean isExist, ShopItem item);
    }

    private final ShopItemDao shopItemDao;
    public DetailsViewModel(@NonNull Application application) {
        super(application);

        // zainicjowanie bazy danych i utworzenie referencji do ShopItemDao który zajmuję się
        // komunikacją z bazą danych
        AppDatabase mDatabase = AppDatabase.getDatabase(application);
        shopItemDao = mDatabase.shopItemDao();
    }

    // pobiera szczegóły produktu
    public void getItemDetails(Integer id, ItemReadyListener listener){
        AppDatabase.databaseWriterExecutor.execute(()->{
            List<ShopItem> list = shopItemDao.getItem(id);
            if(list.size() < 1){
                listener.onReady(false, null);
            }else{
                listener.onReady(true, list.get(0));
            }
        });
    }
}
