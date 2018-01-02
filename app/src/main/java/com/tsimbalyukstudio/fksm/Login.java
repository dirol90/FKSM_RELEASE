package com.tsimbalyukstudio.fksm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {


    public EditText email;
    public EditText password1;
    public FirebaseAuth mAuth;
    public static FirebaseUser user;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();
        try{
        Intent intent = getIntent();
        String s = intent.getStringExtra("Text");
        TextView tv = (TextView) findViewById(R.id.intentText);
        tv.setVisibility(View.VISIBLE);
        tv.setText(s);}
        catch(Exception e){

        }

    }

    public void enterWithLogin(View view) {
        email = (EditText) findViewById(R.id.emailLogin);
        password1 = (EditText) findViewById(R.id.passwordLogin);
        final ProgressDialog progdial = ProgressDialog.show(this, "Проверка...", "Ожидайте", true);

        Log.i("FIRST", email.getText().toString());
        Log.i("SECOND", password1.getText().toString());

        if (!email.getText().toString().equals("") && !password1.getText().toString().equals("")) {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progdial.dismiss();
                                user = mAuth.getCurrentUser();
                                FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                                mUser.getToken(true)
                                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                if (task.isSuccessful()) {
                                                    String idToken = task.getResult().getToken();
                                                    sPref = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                                    SharedPreferences.Editor ed = sPref.edit();
                                                    ed.putString("TOKEN", idToken);
                                                    ed.commit();
                                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                Toast.makeText(Login.this, "Успешно", Toast.LENGTH_SHORT).show();
                            } else {
                                progdial.dismiss();
                                Toast.makeText(Login.this, "ОШИБКА, попробуйте снова", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {
            Toast.makeText(this, "Ошибка, попробуйте снова!", Toast.LENGTH_SHORT).show();
            progdial.dismiss();
        }
    }

    public void moveToRegistration(View view) {
        Intent intent = new Intent (Login.this, Registration.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }

}
