package com.company.sklep.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ShopItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewItem(ShopItem item);

    @Query("SELECT * FROM items WHERE id = :id")
    List<ShopItem> getItem(Integer id);

    @Query("SELECT * FROM items")
    LiveData<List<ShopItem>> getAllItems();
}
