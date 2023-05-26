package com.example.workoutroom.dataBase.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ExEntity.class, HistoryEntity.class, TrainingExCrossRef.class},
        version = 11,
        exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ExDatabase extends RoomDatabase {

    public abstract ExDao exDao();

    //public abstract ExDaoLimit exDaoLimit();

    public abstract HistoryDao historyDao();

    public abstract TrainingExCrossRefDao trainingExCrossRefDao();
    private static ExDatabase INSTANCE = null;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static Bitmap bitmap1;
    static Bitmap bitmap2;
    static Bitmap bitmap3;
    static Bitmap bitmap4;
    static Bitmap bitmap5;
    static Bitmap bitmap6;
    static Bitmap bitmap7;
    static Bitmap bitmap8;
    static Bitmap bitmap9;

    public static ExDatabase getDbInstance(Context context){

        if(INSTANCE == null){
            synchronized (ExDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ExDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                    bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img1);
                    bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img2);
                    bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img3);
                    bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img4);
                    bitmap5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img5);
                    bitmap6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img6);
                    bitmap7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img7);
                    bitmap8 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img8);
                    bitmap9 = BitmapFactory.decodeResource(context.getResources(), R.drawable.img9);
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                ExDao dao = INSTANCE.exDao();
                //dao.deleteAll();

                Log.d("DATABASE","start!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                ExEntity workoutEntity1 = new ExEntity("Push Ups","Description1",240, bitmap1, false);
                dao.insert(workoutEntity1);
                ExEntity workoutEntity2 = new ExEntity("Sit ups","Description2",10, bitmap2, false);
                dao.insert(workoutEntity2);
                ExEntity workoutEntity3 = new ExEntity("Crunches","Description3",10, bitmap3, false);
                dao.insert(workoutEntity3);
                ExEntity workoutEntity4 = new ExEntity("Side Bends","Description4",240, bitmap4, false);
                dao.insert(workoutEntity4);
                ExEntity workoutEntity5 = new ExEntity("Leg Lifts","Description5",60, bitmap5, false);
                dao.insert(workoutEntity5);
                ExEntity workoutEntity6 = new ExEntity("Weighted Push Ups","Description6",120, bitmap6, false);
                dao.insert(workoutEntity6);
                ExEntity workoutEntity7 = new ExEntity("Bicep Dumbbell Curl","Description7",120, bitmap7, false);
                dao.insert(workoutEntity7);
                ExEntity workoutEntity8 = new ExEntity("Exercise Ball Push Ups","Description8",10, bitmap8, false);
                dao.insert(workoutEntity8);
                ExEntity workoutEntity9 = new ExEntity("Tree Pose","Description9",10, bitmap9, false);
                dao.insert(workoutEntity9);
                Log.d("DATABASE","end!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            });
        }
    };

}
