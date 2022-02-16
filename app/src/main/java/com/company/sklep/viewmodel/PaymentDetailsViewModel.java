package com.company.sklep.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.company.sklep.database.AppDatabase;
import com.company.sklep.database.ShopItem;
import com.company.sklep.database.ShopItemDao;
import com.company.sklep.database.TransactionDao;
import com.company.sklep.database.TransactionEntity;
import com.company.sklep.database.UserDao;
import com.company.sklep.database.UserEntity;

import java.util.List;

public class PaymentDetailsViewModel extends AndroidViewModel {
    private final ShopItemDao shopItemDao;
    private final UserDao userDao;
    private final TransactionDao transactionDao;

    public interface DataReadyListener{
        void onDataReady(boolean isDataOk, UserEntity user, ShopItem item);
    }

    public PaymentDetailsViewModel(@NonNull Application application) {
        super(application);

        // zainicjowanie bazy danych i utworzenie referencji do taskDao który zajmuję się
        // komunikacją z bazą danych
        AppDatabase mDatabase = AppDatabase.getDatabase(application);
        shopItemDao = mDatabase.shopItemDao();
        userDao = mDatabase.userDao();
        transactionDao = mDatabase.transactionDao();
    }

    public void getItemAndUserData(Integer itemId, Long userId, DataReadyListener listener){
        AppDatabase.databaseWriterExecutor.execute(()->{
            List<ShopItem> itemList = shopItemDao.getItem(itemId);
            List<UserEntity> userList = userDao.getUser(userId);

            if(itemList.size() < 1 || userList.size() < 1){
                listener.onDataReady(false, null, null);
            }else{
                listener.onDataReady(true, userList.get(0), itemList.get(0));
            }
        });
    }

    public void saveTransaction(TransactionEntity item){
        AppDatabase.databaseWriterExecutor.execute(()->{
            transactionDao.createTransaction(item);
        });
    }

    public Boolean isShippingDataOk(String city, String buildingNumber, String street, String postalCode){
        return !city.isEmpty() && !buildingNumber.isEmpty() && !street.isEmpty() && !postalCode.isEmpty();
    }
}
