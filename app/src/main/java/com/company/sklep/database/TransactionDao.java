package com.company.sklep.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createTransaction(TransactionEntity item);

    @Query("SELECT * FROM transactions WHERE id = :id")
    List<TransactionEntity> getItem(Long id);

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    LiveData<List<TransactionEntity>> getAllItems(Long userId);
}
