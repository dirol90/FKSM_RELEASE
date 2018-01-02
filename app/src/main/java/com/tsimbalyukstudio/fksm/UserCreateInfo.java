package com.tsimbalyukstudio.fksm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class UserCreateInfo extends AppCompatActivity {

    public static EditText firstName;
    public static EditText secondName;
    public static EditText thirdName;
    public static EditText pasportID;
    public static EditText pasportNum;
    public static EditText INN;
    public static EditText phone;
    public static EditText phonePoruch;
    public static EditText ccHolder;
    public static EditText ccFirst;
    public static EditText ccSecond;
    public static EditText ccThird;
    public static EditText ccForth;
    public static EditText ccMonth;
    public static EditText ccYear;
    public static EditText CVV;
    public static Spinner spin_day;
    public static Spinner spin_month;
    public static Spinner spin_year;
    public static String email;
    public static String password;

    String userID;
    DatabaseReference myRef;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        email = getIntent().getExtras().get("Email").toString();
        password = getIntent().getExtras().get("Password").toString();
        userID = getIntent().getExtras().get("UserID").toString();
        Toast.makeText(this, userID+"", Toast.LENGTH_SHORT).show();
         spin_day = (Spinner) findViewById(R.id.spin_day);
         spin_month = (Spinner) findViewById(R.id.spin_month);
         spin_year = (Spinner) findViewById(R.id.spin_year);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.day, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_day.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_month.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_year.setAdapter(adapter3);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        firstName = (EditText) findViewById(R.id.edit_firstname);
        secondName = (EditText) findViewById(R.id.edit_secondname);
        thirdName = (EditText) findViewById(R.id.edit_thirdname);
        pasportID = (EditText) findViewById(R.id.edit_pasp_serie);
        pasportNum = (EditText) findViewById(R.id.edit_pasp_id);
        INN = (EditText) findViewById(R.id.edit_inn_id);
        phone = (EditText) findViewById(R.id.edit_cell_phone);
        phonePoruch = (EditText) findViewById(R.id.edit_cell_phone_poruchitel);
        ccHolder = (EditText) findViewById(R.id.edir_cc_cardholder);
        ccFirst = (EditText) findViewById(R.id.edit_cc_field_one);
        ccSecond = (EditText) findViewById(R.id.edit_cc_field_two);
        ccThird = (EditText) findViewById(R.id.edit_cc_field_three);
        ccForth = (EditText) findViewById(R.id.edit_cc_field_fore);
        ccMonth = (EditText) findViewById(R.id.edit_cc_month);
        ccYear = (EditText) findViewById(R.id.edit_cc_year);
        CVV = (EditText) findViewById(R.id.edit_cc_cvv);
    }

    Uri outputFileUri;
    int photoCounter = 0;
    public void makePhoto(View view) {
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        String file = dir + photoCounter++ + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

        outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

    }

    int TAKE_PHOTO_CODE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView img_user = (ImageView) findViewById(R.id.selfie);
            Toast.makeText(this, outputFileUri+"", Toast.LENGTH_SHORT).show();
                    Picasso.with(UserCreateInfo.this).load(outputFileUri).transform(new CircleTransform()).into(img_user);

        }
    }

    public void saveCharacterInfo(View view) {

// IF ALL FILLED!!!! NEED CHECKING!!!!

        if (!firstName.equals("")&& !secondName.equals("")
                && !thirdName.equals("")&& !pasportID.equals("")
                &&!pasportNum.equals("")&&!INN.equals("")
                &&!phone.equals("")&&!phonePoruch.equals("")
                &&!ccFirst.equals("") && !ccSecond.equals("")
                &&!ccThird.equals("") && !ccForth.equals("")
                &&!ccHolder.equals("") && !ccMonth.equals("")
                &&!ccYear.equals("") && !CVV.equals("")
                && outputFileUri != null
                )
        {

            final USER user = new USER(0);

///////////////
            myRef.child(userID).setValue(user);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            StorageReference mountainsRef = storageRef.child(userID + "/selfie.jpg");

            try {
                File imgFile = new File(outputFileUri.getPath());
                Bitmap myBitmap;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (imgFile.exists()) {
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                }
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UserCreateInfo.this, "Photo NOT uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UserCreateInfo.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
            }


            Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserCreateInfo.this, Login.class);
            intent.putExtra("Text","Введите повторно Ваш логин и пароль");
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "НЕОБХОДИМО ЗАПОЛНИТЬ ВСЕ ПОЛЯ!", Toast.LENGTH_SHORT).show();
        }
    }

}
