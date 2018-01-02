package com.tsimbalyukstudio.fksm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    public EditText email;
    public EditText password1;
    public EditText password2;
    public FirebaseAuth mAuth;
    public static FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_login);
        mAuth = FirebaseAuth.getInstance();
    }

    public void registerWithEmail(View view) {
        email = (EditText) findViewById(R.id.emailloginCreate);
        password1 = (EditText) findViewById(R.id.passwordCreateFirst);
        password2 = (EditText) findViewById(R.id.passwordCreateSecond);
        if (password1.getText().toString().equals(password2.getText().toString())&& password1.getText().toString().length()>5) {
            if (!TextUtils.isEmpty(email.getText().toString()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                setContentView(R.layout.activity_main);
                final ProgressDialog progdial = ProgressDialog.show(this, "Регистрация...", "Ожидайте", true);
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password1.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progdial.dismiss();
                                    Toast.makeText(Registration.this, "Успешно", Toast.LENGTH_SHORT).show();
                                    user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(Registration.this, UserCreatePhotos.class);
                                    intent.putExtra("Email", email.getText().toString());
                                    intent.putExtra("Password", password1.getText().toString());
                                    intent.putExtra("UserID", user.getUid());
                                    startActivity(intent);
                                } else {
                                    progdial.dismiss();
                                    Toast.makeText(Registration.this, "ОШИБКА, попробуйте снова", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Registration.this, Registration.class);
                                    startActivity(intent);
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "Почта не верна", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Пароль должен быть более 5 символов и должен совпадать с полем подтверждения пароля", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Registration.this, MainActivity.class);
        startActivity(intent);
    }
}
