package com.example.workoutroom.exercises;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.workoutroom.R;
import com.example.workoutroom.dataBase.Converters;
import com.example.workoutroom.dataBase.data.ExEntity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class AddNewExerciseActivity extends AppCompatActivity {

    private TextInputLayout tvNameEx;
    private TextInputLayout tvDescriptionEx;
    private TextInputLayout tvTimeEx;
    private TextInputEditText tiEditTextN;
    private TextInputEditText tiEditTextD;
    private TextInputEditText tiEditTextT;
    private int timeEx;
    private Button imageButton;
    private ImageView imageView;
    private String nameExCh;
    private String descrExCh;
    private String timeExCh;
    private Bitmap imageExCh;
    Bitmap bitmap;
    ExViewModel exViewModel;

    String requiredText;

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


    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_exercise);

        requiredText = this.getResources().getString(R.string.text_required);
        tvNameEx = findViewById(R.id.nameEx);
        tvDescriptionEx = findViewById(R.id.descriptionEx);
        tvTimeEx = findViewById(R.id.timeEx);
        imageButton = findViewById(R.id.imageButton);
        tiEditTextN = findViewById(R.id.TextInputEditTextName);
        tiEditTextD = findViewById(R.id.TextInputEditTextDescr);
        tiEditTextT = findViewById(R.id.TextInputEditTextTime);
        imageView = findViewById(R.id.imageViewEx);
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

        Intent intent = getIntent();
        if (intent.hasExtra("id") && intent.hasExtra("name") && intent.hasExtra("descr") && intent.hasExtra("time") && intent.hasExtra("image")) { //если передали
            saveButton.setText(this.getResources().getString(R.string.btn_update_ex));

            nameExCh = intent.getStringExtra("name");
            descrExCh = intent.getStringExtra("descr");
            timeExCh = Integer.toString(intent.getIntExtra("time", 0));
            imageExCh = Converters.toBitmap(intent.getByteArrayExtra("image"));

            tiEditTextN.setText(nameExCh);
            tiEditTextD.setText(descrExCh);
            tiEditTextT.setText(timeExCh);
            imageView.setImageBitmap(imageExCh);

            saveButton.setOnClickListener(view ->{
                if (tvNameEx.getEditText().getText().toString().equals("") && tvTimeEx.getEditText().getText().toString().equals("")) { //если не введены все значения
                    tvNameEx.setError(requiredText); //надпись Обязательно
                    tvTimeEx.setError(requiredText);

                } else if (tvNameEx.getEditText().getText().toString().equals("") || tvTimeEx.getEditText().getText().toString().equals("0")) { //если не введено name
                    tvNameEx.setError(requiredText);
                    tvTimeEx.setError(null);

                } else if (tvNameEx.getEditText().getText().toString().equals("0") || tvTimeEx.getEditText().getText().toString().equals("")) { //если не введено time
                    tvNameEx.setError(null);
                    tvTimeEx.setError(requiredText);
                }
                else if (bitmap == null) { //если нет картинки
                    bitmap = imageExCh;
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
                        workoutEntity.idEx = intent.getLongExtra("id", 0);
                        exViewModel.update(workoutEntity);
                        setResult(RESULT_OK, replyIntent);
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.ex_saved, //изменить
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });

        } else {
            saveButton.setText(this.getResources().getString(R.string.btn_save_ex));
            imageView.setVisibility(View.GONE);
            tiEditTextN.getText().clear();
            tiEditTextD.getText().clear();
            tiEditTextT.getText().clear();
            saveButton.setOnClickListener(view ->{
                if (tvNameEx.getEditText().getText().toString().equals("") && tvTimeEx.getEditText().getText().toString().equals("")) { //если не введены все значения
                    tvNameEx.setError(requiredText); //надпись Обязательно
                    tvTimeEx.setError(requiredText);

                } else if (tvNameEx.getEditText().getText().toString().equals("") || tvTimeEx.getEditText().getText().toString().equals("0")) { //если не введено name
                    tvNameEx.setError(requiredText);
                    tvTimeEx.setError(null);

                } else if (tvNameEx.getEditText().getText().toString().equals("0") || tvTimeEx.getEditText().getText().toString().equals("")) { //если не введено time
                    tvNameEx.setError(null);
                    tvTimeEx.setError(requiredText);
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
