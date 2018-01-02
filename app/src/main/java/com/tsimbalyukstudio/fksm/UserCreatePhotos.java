package com.tsimbalyukstudio.fksm;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.ArrayList;
import java.util.List;

public class UserCreatePhotos extends AppCompatActivity {

    private ImageView selfie;
    private ImageView paspFirst;
    private ImageView paspSec;
    private ImageView paspThird;
    private ImageView inn;
    private ImageView ccFirst;
    private ImageView ccSec;
    private ImageView spDoc;
    private String userID;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    public static String email;
    public static String password;
    private static final int MY_REQUEST_CODE_CAMERA = 1;
    private static final int MY_REQUEST_CODE_WRITE = 2;
    private static final int MY_REQUEST_CODE_READ = 3;
    private static final int MY_REQUEST_CODE_PHONE_STATE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_photos);
        selfie = (ImageView) findViewById(R.id.image_selfie);
        paspFirst = (ImageView) findViewById(R.id.image_pasp_first);
        paspSec = (ImageView) findViewById(R.id.image_pasp_sec);
        paspThird = (ImageView) findViewById(R.id.image_pasp_third);
        inn = (ImageView) findViewById(R.id.image_inn);
        ccFirst = (ImageView) findViewById(R.id.image_cc_first);
        ccSec = (ImageView) findViewById(R.id.image_cc_sec);
        spDoc = (ImageView) findViewById(R.id.image_spdoc);
        email = getIntent().getExtras().get("Email").toString();
        password = getIntent().getExtras().get("Password").toString();

        userID = getIntent().getExtras().get("UserID").toString();

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CAMERA},
                        MY_REQUEST_CODE_CAMERA);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_WRITE);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_REQUEST_CODE_READ);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        MY_REQUEST_CODE_PHONE_STATE);
            }
        }

    }

    Uri outputFileUri;
    List<Uri> listUrl = new ArrayList<>();
    String[] names = {"selfie", "paspFirst", "paspSec", "paspThird", "INN", "ccFirst", "ccSec", "spDoc"};
    View view;

    public void makePhoto(View view) {
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        int temp = Integer.parseInt(view.getTag().toString());
        String file = dir + names[temp] + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

        outputFileUri = Uri.fromFile(newfile);
        if (listUrl.contains(outputFileUri)) {
            listUrl.set(temp, outputFileUri);
        } else {
            listUrl.add(temp, outputFileUri);
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        this.view = view;
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

    }

    int TAKE_PHOTO_CODE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
            view.setBackgroundResource(R.drawable.yes);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void saveCharacterPhotos(View view) {

        final ProgressDialog progdial = ProgressDialog.show(this, "Создание профиля...", "Ожидайте", true);
        final USER user = new USER();
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
        myRef.child(userID).setValue(user);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int x = 0; x < listUrl.size(); x++) {

                    StorageReference mountainsRef = storageRef.child(userID + "/" + names[x] + ".jpg");

                    try {
                        File imgFile = new File(listUrl.get(x).getPath());
                        Bitmap myBitmap;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if (imgFile.exists()) {
                            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            myBitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                        }
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = mountainsRef.putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast.makeText(UserCreatePhotos.this, "Photo NOT uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(UserCreatePhotos.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        progdial.dismiss();
                        Toast.makeText(UserCreatePhotos.this, "Ошибка загрузки, попробуйте снова!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).start();

        progdial.dismiss();

        Toast.makeText(this, "Сохранено!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(UserCreatePhotos.this, Login.class);
        intent.putExtra("Text", "Введите повторно Ваш логин и пароль");
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean ch = false;
        try {
            ch = getIntent().getExtras().getBoolean("Changes");
        } catch (Exception e) {
        }
        if (ch == false) {
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, password);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });

                        }
                    });
        }
        Intent intent = new Intent(UserCreatePhotos.this, MainActivity.class);
        startActivity(intent);
    }
}
