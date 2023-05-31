package com.example.workoutroom.dataBase.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ExEntity.class, HistoryEntity.class, TrainingExCrossRef.class},
        version = 26,
        exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ExDatabase extends RoomDatabase {

    public abstract ExDao exDao();

    public abstract HistoryDao historyDao();

    public abstract TrainingExCrossRefDao trainingExCrossRefDao();
    private static ExDatabase INSTANCE = null;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static Bitmap bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7, bitmap8, bitmap9;

    static String nameEx1, nameEx2, nameEx3, nameEx4, nameEx5, nameEx6, nameEx7, nameEx8, nameEx9, desEx1, desEx2, desEx3, desEx4, desEx5, desEx6, desEx7, desEx8, desEx9;

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

                    nameEx1 = context.getResources().getString(R.string.text_ex_1);
                    nameEx2 = context.getResources().getString(R.string.text_ex_2);
                    nameEx3 = context.getResources().getString(R.string.text_ex_3);
                    nameEx4 = context.getResources().getString(R.string.text_ex_4);
                    nameEx5 = context.getResources().getString(R.string.text_ex_5);
                    nameEx6 = context.getResources().getString(R.string.text_ex_6);
                    nameEx7 = context.getResources().getString(R.string.text_ex_7);
                    nameEx8 = context.getResources().getString(R.string.text_ex_8);
                    nameEx9 = context.getResources().getString(R.string.text_ex_9);

                    desEx1 = context.getResources().getString(R.string.text_des_1);
                    desEx2 = context.getResources().getString(R.string.text_des_2);
                    desEx3 = context.getResources().getString(R.string.text_des_3);
                    desEx4 = context.getResources().getString(R.string.text_des_4);
                    desEx5 = context.getResources().getString(R.string.text_des_5);
                    desEx6 = context.getResources().getString(R.string.text_des_6);
                    desEx7 = context.getResources().getString(R.string.text_des_7);
                    desEx8 = context.getResources().getString(R.string.text_des_8);
                    desEx9 = context.getResources().getString(R.string.text_des_9);
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @SuppressLint("NewApi")
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ExDao dao = INSTANCE.exDao();
                //dao.deleteAll();

                Log.d("DATABASE","start!");
                ExEntity workoutEntity1 = new ExEntity(nameEx1, desEx1,240, bitmap1, false);
                dao.insert(workoutEntity1);
                ExEntity workoutEntity2 = new ExEntity(nameEx2, desEx2,180, bitmap2, false);
                dao.insert(workoutEntity2);
                ExEntity workoutEntity3 = new ExEntity(nameEx3, desEx3,180, bitmap3, false);
                dao.insert(workoutEntity3);
                ExEntity workoutEntity4 = new ExEntity(nameEx4, desEx4,240, bitmap4, false);
                dao.insert(workoutEntity4);
                ExEntity workoutEntity5 = new ExEntity(nameEx5, desEx5,60, bitmap5, false);
                dao.insert(workoutEntity5);
                ExEntity workoutEntity6 = new ExEntity(nameEx6, desEx6,120, bitmap6, false);
                dao.insert(workoutEntity6);
                ExEntity workoutEntity7 = new ExEntity(nameEx7, desEx7,120, bitmap7, false);
                dao.insert(workoutEntity7);
                ExEntity workoutEntity8 = new ExEntity(nameEx8, desEx8,180, bitmap8, false);
                dao.insert(workoutEntity8);
                ExEntity workoutEntity9 = new ExEntity(nameEx9, desEx9,180, bitmap9, false);
                dao.insert(workoutEntity9);
                Log.d("DATABASE","end!");
            });
        }
    };
}
