package com.company.sklep.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "items")
public class ShopItem {
    @PrimaryKey(autoGenerate = true) public Integer id;
    public String title;
    public String body;
    public Integer photoId;
    public Double price;

    public ShopItem(){}

    public ShopItem(Integer id, String title, String body, Integer photoPath, Double price) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.photoId = photoPath;
        this.price = price;
    }
}
