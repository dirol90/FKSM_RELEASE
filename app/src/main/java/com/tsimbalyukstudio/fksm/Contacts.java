package com.tsimbalyukstudio.fksm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Contacts extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }

    public void onClickOdessaFirst (View view){
        try{
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.470015,30.732517,16");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);}
        catch (Exception e){
            Toast.makeText(this, "К сожалению не поддерживаются Google карты", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickOdessaSecond (View view){
        try{
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.4709139,30.7406287,19");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);}
        catch (Exception e){
            Toast.makeText(this, "К сожалению не поддерживаются Google карты", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickOdessaThird (View view){
        try{
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.5827702,30.8007977,19");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);}
        catch (Exception e){
            Toast.makeText(this, "К сожалению не поддерживаются Google карты", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickOdessaForth (View view){
        try{
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.399925,30.724642,15");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);}
        catch (Exception e){
            Toast.makeText(this, "К сожалению не поддерживаются Google карты", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickOdessaFifth (View view){
        try{
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.470491,30.72241,15");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);}
        catch (Exception e){
            Toast.makeText(this, "К сожалению не поддерживаются Google карты", Toast.LENGTH_SHORT).show();
        }
    }

    //50.43019,30.548345
    public void onClickKievFirtst (View view){
        try{
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=50.43019,30.548345");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);}
        catch (Exception e){
            Toast.makeText(this, "К сожалению не поддерживаются Google карты", Toast.LENGTH_SHORT).show();
        }
    }

    //50.411633,30.643102
    public void onClickKievSecond (View view){
        try{
            Uri gmmIntentUri = Uri.parse("google.streetview:cbll=50.4120054,30.6428758,18");

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);}
        catch (Exception e){
            Toast.makeText(this, "К сожалению не поддерживаются Google карты", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Contacts.this, MainActivity.class);
        startActivity(intent);
    }

}
