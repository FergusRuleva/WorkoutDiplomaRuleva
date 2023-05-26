package com.example.workoutroom.exercises;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class AddNewExerciseActivity extends AppCompatActivity {

    private TextInputLayout tvNameEx;
    private TextInputLayout tvDescriptionEx;
    private TextInputLayout tvTimeEx;
    private int timeEx;
    private ImageButton imageButton;
    Bitmap bitmap;
    ExViewModel exViewModel;

    // Registers a photo picker activity launcher in single-select mode.
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the photo picker.
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_exercise);

        tvNameEx = findViewById(R.id.nameEx);
        tvDescriptionEx = findViewById(R.id.descriptionEx);
        tvTimeEx = findViewById(R.id.timeEx);
        imageButton = findViewById(R.id.imageButton);
        final Button saveButton = findViewById(R.id.saveButton);
        exViewModel = new ViewModelProvider(this).get(ExViewModel.class);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPickMedia();
                Toast.makeText(
                        getApplicationContext(),
                        R.string.image_added,
                        Toast.LENGTH_LONG).show();
                //imageButton.setImageBitmap(bitmap);
            }
        });

        saveButton.setOnClickListener(view ->{
            if (tvNameEx.getEditText().getText().toString().equals("") && tvTimeEx.getEditText().getText().toString().equals("")) { //если не введены все значения
                tvNameEx.setError("Required"); //надпись Обязательно
                tvTimeEx.setError("Required");

            } else if (tvNameEx.getEditText().getText().toString().equals("") || tvTimeEx.getEditText().getText().toString().equals("0")) { //если не введено name
                tvNameEx.setError("Required");
                tvTimeEx.setError(null);

            } else if (tvNameEx.getEditText().getText().toString().equals("0") || tvTimeEx.getEditText().getText().toString().equals("")) { //если не введено time
                tvNameEx.setError(null);
                tvTimeEx.setError("Required");
            }
            else if (bitmap == null) { //если нет картинки
                Toast.makeText(
                        getApplicationContext(),
                        R.string.image_not_added,
                        Toast.LENGTH_LONG).show();
            }
            else {
                tvNameEx.setError(null);
                tvTimeEx.setError(null);
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(tvNameEx.getEditText().getText()) && TextUtils.isEmpty(tvTimeEx.getEditText().getText()) && bitmap == null){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String nameEx = tvNameEx.getEditText().getText().toString();
                    String descriptionEx = tvDescriptionEx.getEditText().getText().toString();
                    timeEx = Integer.parseInt(tvTimeEx.getEditText().getText().toString());

                    ExEntity workoutEntity = new ExEntity(nameEx, descriptionEx, timeEx, bitmap, false);
                    exViewModel.insert(workoutEntity);
                    setResult(RESULT_OK, replyIntent);
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.ex_saved,
                            Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    public void clickPickMedia(){

        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK && data != null)
        {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
