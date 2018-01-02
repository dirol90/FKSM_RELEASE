package com.tsimbalyukstudio.fksm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
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

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private SharedPreferences sPref;
    FirebaseDatabase database;
    DatabaseReference myRef;
    USER user;
    int days;
    boolean flag = false;
    int sum = 0;
    Spinner refoundSpinner;
    List loans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // firebase configs
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        // try to find shared preferences with users token to connect to some user in firebase
        try {
            sPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String s = sPref.getString("TOKEN", "");
            mAuth.signInWithCustomToken(s);
            currentUser = mAuth.getCurrentUser();
        } catch (Exception e) {}

        // Nav drawer configs
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (currentUser != null) {
            View hView = navigationView.getHeaderView(0);
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_exit);
            item.setTitle("Выйти");
            final TextView nav_user = (TextView) hView.findViewById(R.id.characterName);

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                    user = dataSnapshot.child(currentUser.getUid()).getValue(USER.class);
                    String s = user.getFirstName();
                    String d = user.getSecondName();
                    nav_user.setText(s + " " + d);
                    } catch (Exception e){}
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            TextView nav_email = (TextView) hView.findViewById(R.id.characterMail);
            nav_email.setText(currentUser.getEmail());

            final ImageView img_user = (ImageView) hView.findViewById(R.id.characterImage);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(currentUser.getUid()).child("selfie.jpg");
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(MainActivity.this).load(uri).centerCrop().resize(75,75).transform(new CircleTransform()).into(img_user);
                }
            });
        } else {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_exit);
            item.setTitle("Войти");
        }

        // Tab view configs
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("TAG1");

        tabSpec.setContent(R.id.one);
        tabSpec.setIndicator("ПОЛУЧИТЬ ЗАЙМ");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("TAG2");
        tabSpec.setContent(R.id.TWO);
        tabSpec.setIndicator("ПОГАСИТЬ ЗАЙМ");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        // seek bar and other text vies and etc configs
        final TextView sumText = (TextView) findViewById(R.id.sumCount);

        final TextView textDays = (TextView) findViewById(R.id.editText7);
        final TextView textSum = (TextView) findViewById(R.id.editText);
        final EditText textSumRefound = (EditText) findViewById(R.id.editTextRefound);
        textSumRefound.setText("1000");

        final DiscreteSeekBar sk2 = (DiscreteSeekBar) findViewById(R.id.discreteSeekBar3);
        final DiscreteSeekBar sk = (DiscreteSeekBar) findViewById(R.id.discreteSeekBar2);

        // Spinned in tab view confis (Loans Part)
        Spinner spin_category = (Spinner) findViewById(R.id.spiner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_category.setAdapter(adapter);

        spin_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sk2.setMax(20000);
                    sk2.setProgress(1000);
                    sum = 1000;
                    sumText.setText("" + (int) (sk2.getProgress() + (sk2.getProgress() * sk.getProgress() * 0.0185)));
                    int value = sk2.getProgress() / 500;
                    value = value * 500;
                    textSum.setText("" + value);
                } else if (position == 1) {
                    sk2.setMax(100000);
                    sk2.setProgress(1000);
                    sum = 1000;
                    sumText.setText("" + (int) (sk2.getProgress() + (sk2.getProgress() * sk.getProgress() * 0.0185)));
                    int value = sk2.getProgress() / 500;
                    value = value * 500;
                    textSum.setText("" + value);
                } else if (position == 2) {
                    sk2.setMax(200000);
                    sk2.setProgress(1000);
                    sum = 1000;
                    sumText.setText("" + (int) (sk2.getProgress() + (sk2.getProgress() * sk.getProgress() * 0.0185)));
                    int value = sk2.getProgress() / 500;
                    value = value * 500;
                    textSum.setText("" + value);
                } else if (position == 3) {
                    sk2.setMax(50000);
                    sk2.setProgress(1000);
                    sum = 1000;
                    sumText.setText("" + (int) (sk2.getProgress() + (sk2.getProgress() * sk.getProgress() * 0.0185)));
                    int value = sk2.getProgress() / 500;
                    value = value * 500;
                    textSum.setText("" + value);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sk2.setMax(20000);
            }
        });


        // view flipper configs, photos with some deals are located here
        final ViewFlipper MyViewFlipper = (ViewFlipper) findViewById(R.id.galery_main);
        MyViewFlipper.setFlipInterval(5000);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        MyViewFlipper.setAnimation(in);
        MyViewFlipper.setAnimation(out);
        MyViewFlipper.setAutoStart(true);

        sk.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                textDays.setText("" + value);
                sumText.setText("" + (int) (sk2.getProgress() + (sk2.getProgress() * sk.getProgress() * 0.0185)));
                days = value;
                return value;
            }
        });

        sk2.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                value = value / 500;
                value = value * 500;
                textSum.setText("" + value);
                int tempProgress = sk2.getProgress() / 500;
                tempProgress = tempProgress * 500;
                sumText.setText("" + (int) (tempProgress + (tempProgress * sk.getProgress() * 0.0185)));
                sum = value;
                return value;
            }
        });


        // spinned with custom info from firebase (refounds part)
        if (currentUser != null) {
            loans = new ArrayList<>();
            myRef.child(currentUser.getUid()).child("loan").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    loans.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Loan loan = postSnapshot.getValue(Loan.class);
                        if (loan.getStatus() == 1) {
                            loans.add(loan);
                        }
                    }

                    String[] cutedLoans = new String[loans.size()];

                    for (int x = 0; x < loans.size(); x++) {
                        Loan temp = (Loan) loans.get(x);
                        char[] c = temp.getDate().toCharArray();
                        String s = "";
                        int y = 0;
                        while (c[y] != '_') {
                            if (y == 4 || y == 6) {
                                s += "-";
                            }
                            s += c[y++];
                        }
                        cutedLoans[x] = temp.getSum() + "грн., от: " + s;
                    }

                    refoundSpinner = (Spinner) findViewById(R.id.loans_spinner);

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, cutedLoans);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    refoundSpinner.setAdapter(spinnerArrayAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                }
            });
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // here i try to add service to show some notifications for users

        //Intent serviceIntent = new Intent(PushService.class.getName());
        //serviceIntent.putExtra("UserID", currentUser.getUid());
        //this.startService(serviceIntent);


    }

    /**
     * Method helps us to grub sum of the loan wich user would like to take, add to it unique id,
     * add period of the loan and send it to some user ID in firebase
     * @param view
     */
    public void moveToLoan(View view) {
        if (currentUser != null) {
            if (sum == 20000 && flag == false) {
                flag = true;
                sum = 1000;
            }
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Новый займ")
                    .setMessage("Подтвердите получение нового займа")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, sum + "", Toast.LENGTH_SHORT).show();
                            Loan loan = new Loan(sum, days, UUID.randomUUID().toString());
                            myRef.child(currentUser.getUid()).child("loan").child(loan.getLoanID()).setValue(loan);
                            Toast.makeText(MainActivity.this, "Ваша заявка отправлена на рассмотрение", Toast.LENGTH_LONG).show();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }

    /**
     * Method helps to grab the sum from EditText and find out one of the loans wish user would
     * like pay. To firebase we send only sum and loans ID, payment details catches from user details.
     */
    String loanID;
    int pos;
    public void moveToRefound(View view) {

        if (currentUser != null) {
            EditText et = (EditText) findViewById(R.id.editTextRefound);
            refoundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pos = position;
                    Loan temp = (Loan) loans.get(position);
                    loanID = temp.getLoanID();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    pos = 0;
                    }
            });

                final Loan temp = (Loan) loans.get(pos);
                loanID = temp.getLoanID();

            if (et.getText().toString().matches("[0-9]+")) {
                final int sumRefo = Integer.parseInt(et.getText().toString());
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Погасить займ")
                        .setMessage("Подтвердите погашение текущего займа")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(sumRefo <= temp.getSum() && sumRefo > 0){
                                Toast.makeText(MainActivity.this, sumRefo + "", Toast.LENGTH_SHORT).show();
                                Refound refo = new Refound(sumRefo, UUID.randomUUID().toString(), loanID);
                                myRef.child(currentUser.getUid()).child("refound").child(refo.getRefoundID()).setValue(refo);
                                Toast.makeText(MainActivity.this, "Ваша заявка отправлена на рассмотрение", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Введенная сумма больше суммы займа!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                Toast.makeText(this, "НЕ ВЕРНАЯ СУММА!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    }


    /**
     * Unusuble methods for this moment, helps to work with MENU (three point), this menu is
     * hiden for this moment
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * First button is for entering to users cabinet, look for some info and photos.
     * Second button is for entering to the loans info page, user can find new (int 3),
     * canceled(int 0), confirmed(int 1), etc loans and del. some of thm if they are marked
     * as new (int 3)
     * Third button is for opening WEB site of company
     * Forth button is for opening contacts info of offices of company
     * Fifth button is for opening call activity, for fast coll to office
     * Six button is for exit from users cabinet or for entering to it
     * @param item selected menu button
     * @return always true
     */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cabinet) {
            if (currentUser != null) {
            Intent intent = new Intent(this, UserCabinet.class);
            startActivity(intent);
            } else {
                Toast.makeText(this, "Вы не в системе", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_list) {
            if (currentUser != null) {
            Intent intent = new Intent(this, LoansInfo.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Вы не в системе", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
        } else if (id == R.id.nav_news) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fksm.com.ua/ru"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(MainActivity.this, Contacts.class);
            startActivity(intent);
        } else if (id == R.id.nav_call) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+380676943145", null)));
        } else if (id == R.id.nav_exit) {
            if (currentUser != null) {
                try {
                    sPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString("TOKEN", null);
                    ed.commit();
                    finish();
                    startActivity(getIntent());
                } catch (Exception e) {

                }
                Toast.makeText(this, "Вы вышли из системы", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Вы не в системе", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Override of back pressed method, if once pressed button user have 3 sec for second tap
     * to exit from the app.
     **/
    private static long back_pressed;
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 3000 > System.currentTimeMillis()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    this.finishAffinity();
                } else {
                    this.finish();
                    System.exit(0);
                }
            } else {
                Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
            }
        }
        back_pressed = System.currentTimeMillis();
    }


}

