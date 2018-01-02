package com.tsimbalyukstudio.fksm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserCabinet extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private SharedPreferences sPref;
    public static FirebaseUser userFB;
    FirebaseDatabase database;
    DatabaseReference myRef;
    USER user;
    boolean connected = false;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            connected = true;
        }
        else
            connected = false;

        if(connected) {
            final ProgressDialog progdial = ProgressDialog.show(this, "Загрузка профиля...", "Ожидайте", true);
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users");

            try {
                sPref = PreferenceManager.getDefaultSharedPreferences(UserCabinet.this);
                String s = sPref.getString("TOKEN", "");
                mAuth.signInWithCustomToken(s);
                currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.child(currentUser.getUid()).getValue(USER.class);
                            String firstName = user.getFirstName();
                            String secondName = user.getSecondName();
                            String thirdName = user.getThirdName();
                            String pasportID = user.getPasportID();
                            String pasportNum = user.getPasportNum();
                            String INN = user.getINN();
                            String phone = user.getPhone();
                            String phonePoruch = user.getPhonePoruch();
                            String ccHolder = user.getCcHolder();
                            String creditCard = user.getCc();
                            String ccValid = user.getCcValid();
                            String CVV = user.getCVV();
                            String birthday = user.getBirthday();
                            status = user.getCharacterStatus();
                            int i = Integer.parseInt(status);
                            if (i == 1){
                                Button b = (Button) findViewById( R.id.change_user_photos);
                                b.setVisibility(View.INVISIBLE);
                            }


                            char[] crCa = creditCard.toCharArray();

                            char[] crVa = ccValid.toCharArray();

                            char[] crCVV = CVV.toCharArray();

                            TextView fn = (TextView) findViewById(R.id.first_name);
                            TextView sn = (TextView) findViewById(R.id.second_name);
                            TextView tn = (TextView) findViewById(R.id.third_name);
                            TextView pID = (TextView) findViewById(R.id.pasp_serie);
                            TextView pNu = (TextView) findViewById(R.id.pasp_num);
                            TextView inn = (TextView) findViewById(R.id.inn);
                            TextView ph = (TextView) findViewById(R.id.phone);
                            TextView phTwo = (TextView) findViewById(R.id.phone_two);
                            TextView ccH = (TextView) findViewById(R.id.card_holder);
                            TextView cc = (TextView) findViewById(R.id.card_num);
                            TextView ccV = (TextView) findViewById(R.id.card_valid);
                            TextView cvv = (TextView) findViewById(R.id.card_cvv);
                            TextView bi = (TextView) findViewById(R.id.birth_day);


                            fn.setText(fn.getText() + " " + firstName);
                            sn.setText(sn.getText() + " " + secondName);
                            tn.setText(tn.getText() + " " + thirdName);
                            pID.setText(pID.getText() + " " + pasportID);
                            pNu.setText(pNu.getText() + " " + pasportNum);
                            inn.setText(inn.getText() + " " + INN);
                            ph.setText(ph.getText() + " " + phone);
                            phTwo.setText(phTwo.getText() + " " + phonePoruch);
                            ccH.setText(ccH.getText() + " " + ccHolder);
                            try{
                            cc.setText(cc.getText() + " ****-****-****-" + crCa[crCa.length - 4] + "" + crCa[crCa.length - 3] + "" + crCa[crCa.length - 2] + "" + crCa[crCa.length - 1]);
                                ccV.setText(ccV.getText() + " **/" + crVa[crVa.length - 2] + "" + crVa[crVa.length - 1]);
                                cvv.setText(cvv.getText() + " **" + crCVV[crCVV.length - 1]);
                            }
                            catch(Exception e){
                                cc.setText(cc.getText()+"");
                                ccV.setText(ccV.getText()+"");
                                cvv.setText(cvv.getText()+"");
                            }
                            bi.setText(bi.getText() + " " + birthday);


                            final ImageView img_user = (ImageView) findViewById(R.id.selfie);
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(currentUser.getUid()).child("selfie.jpg");
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.with(UserCabinet.this).load(uri).centerCrop().resize(75, 75).transform(new CircleTransform()).into(img_user);
                                }
                            });
                            progdial.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(UserCabinet.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            progdial.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                progdial.dismiss();
                Toast.makeText(this, "Ошибка загрузки профиля!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserCabinet.this, Login.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Отсутствует интернет соединение!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserCabinet.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onExit(View v) {
        Intent intent = new Intent(UserCabinet.this, MainActivity.class);
        startActivity(intent);
    }

    public void changeUserPhotos (View view){
        Intent intent  = new Intent(UserCabinet.this, UserCreatePhotos.class);
        userFB = mAuth.getCurrentUser();
        intent.putExtra("UserID", userFB.getUid());
        intent.putExtra("Email", user.getEmail());
        intent.putExtra("Password", user.getPassword());
        intent.putExtra("Changes", true);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserCabinet.this, MainActivity.class);
        startActivity(intent);
    }
}
