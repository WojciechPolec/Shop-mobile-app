package com.company.sklep.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.company.sklep.R;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserEntity.class, ShopItem.class, TransactionEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ShopItemDao shopItemDao();
    public abstract TransactionDao transactionDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_db")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // ta część kodu odpowiada za wypełnienie bazy danych przy pierwszym uruchomieniu
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriterExecutor.execute(()->{
                ShopItemDao dao = INSTANCE.shopItemDao();

                dao.createNewItem(new ShopItem(0, "test title", "test desc", R.drawable.image1, 14.99));
                dao.createNewItem(new ShopItem(1, "test title1", "test desc1", R.drawable.image1, 14.99));
                dao.createNewItem(new ShopItem(2, "test title2", "test desc2", R.drawable.image1, 14.99));
                dao.createNewItem(new ShopItem(3, "test title3", "test desc3", R.drawable.image1, 14.99));
                dao.createNewItem(new ShopItem(4, "test title4", "test desc4", R.drawable.image1, 14.99));
                dao.createNewItem(new ShopItem(5, "test title5", "test desc5", R.drawable.image1, 14.99));
                dao.createNewItem(new ShopItem(6, "test title6", "test desc6", R.drawable.image1, 14.99));
                dao.createNewItem(new ShopItem(7, "test title7", "test desc7", R.drawable.image1, 14.99));
            });
        }
    };
}

