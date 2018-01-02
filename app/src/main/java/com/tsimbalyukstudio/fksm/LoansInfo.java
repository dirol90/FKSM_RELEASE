package com.tsimbalyukstudio.fksm;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class LoansInfo extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private SharedPreferences sPref;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private USER user;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    boolean connected = false;
    List loans = new ArrayList<Loan>();
    List refounds = new ArrayList<Refound>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loans);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            connected = true;
        } else
            connected = false;


        if (connected) {
            final ProgressDialog progdial = ProgressDialog.show(this, "Загрузка профиля...", "Ожидайте", true);
            mAuth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users");

            try {
                sPref = PreferenceManager.getDefaultSharedPreferences(LoansInfo.this);
                String s = sPref.getString("TOKEN", "");
                mAuth.signInWithCustomToken(s);
                currentUser = mAuth.getCurrentUser();

                /////

                myRef.child(currentUser.getUid()).child("loan").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        loans.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Loan loan = postSnapshot.getValue(Loan.class);
                            loans.add(loan);
                        }


                        Collections.sort(loans, new Comparator<Loan>() {
                            String empty;
                            public int compare(Loan o1, Loan o2) {
                                return o1.getDate(empty).compareTo(o2.getDate(empty));
                            }
                        });
                         Collections.reverse(loans);
                        refresh();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoansInfo.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                    }
                });

                myRef.child(currentUser.getUid()).child("refound").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        refounds.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Refound refo = postSnapshot.getValue(Refound.class);
                            refounds.add(refo);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LoansInfo.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                    }
                });


/////

                if (currentUser != null) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.child(currentUser.getUid()).getValue(USER.class);

                            String status = user.getCharacterStatus();
                            TextView stat = (TextView) findViewById(R.id.text_status);

                            switch (status) {
                                case ("1"):
                                    stat.setText("ПРОВЕРЕН");
                                    stat.setTextColor(GREEN);
                                    break;
                                case ("2"):
                                    stat.setText("ОТКЛОНЕН");
                                    stat.setTextColor(RED);
                                    break;
                                case ("3"):
                                    stat.setText("НА ПРОВЕРКЕ");
                                    stat.setTextColor(BLUE);
                                    break;
                                case (""):
                                    stat.setText("ОШИБКА");
                                    stat.setTextColor(BLACK);
                                    break;
                                default:
                                    stat.setText("ОШИБКА");
                                    stat.setTextColor(BLACK);
                                    break;
                            }


                            progdial.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(LoansInfo.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            progdial.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                progdial.dismiss();
                Toast.makeText(this, "Ошибка загрузки профиля!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoansInfo.this, Login.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, "Отсутствует интернет соединение!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoansInfo.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onExit(View v) {
        Intent intent = new Intent(LoansInfo.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LoansInfo.this, MainActivity.class);
        startActivity(intent);
    }



    public void refresh(){
        mRecyclerView = (RecyclerView) findViewById(R.id.loans_recycler);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(LoansInfo.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyLoansAdapter(loans, refounds, myRef, currentUser);
        mRecyclerView.setAdapter(mAdapter);
    }
}
