package com.company.sklep.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "transactions")
public class TransactionEntity {
    @PrimaryKey(autoGenerate = true) public Long id;
    public String city = "";
    public String postCode = "";
    public String street = "";
    public String buildingNumber;
    public Long userId;
    public String itemName = "";
    public String price = "";
}
