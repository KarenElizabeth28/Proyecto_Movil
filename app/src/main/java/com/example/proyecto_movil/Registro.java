package com.trivalia.trivalia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Registro extends Activity implements OnClickListener {
    private EditText user,pass;
    private Button mRegistro;

    //Proceso del dialogo
    private ProgressDialog pDialog;

    //Json paser class
    JSONParse jsonParser= new JSONParse();

    //redireccion de manera local
    //testing del emulador
    private static final String Register_URL= "http://brivalia.x10.mx/index/usuario/registro";

    //ids
    private static final String Tag_SUCCESS= "success";
    private static final String Tag_MESSAGE="message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        user=(EditText)findViewById(R.id.nomusu);
        pass=(EditText)findViewById(R.id.contra);

        mRegistro=(Button)findViewById(R.id.registro);
        //Cambio en view.oncliclister
        mRegistro.setOnClickListener((View.OnClickListener) this);
    }
    @Override
    public void onClick(View v){
        new CreateUser().execute();
    }
    class CreateUser extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pDialog= new ProgressDialog(Registro.this);
            pDialog.setMessage("Creating User..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... args){
            int success;
            String username= user.getText().toString();
            String password= user.getText().toString();
            try {
                List params= new ArrayList();
                params.add(new BasicNameValuePair("username",username));
                params.add(new BasicNameValuePair("password",password));
                Log.d("Request!","starting");
                JSONObject json= jsonParser.makeHttpRequest(Register_URL,"POST",params);
                Log.d("Registering attempt",json.toString());

                success= json.getInt(Tag_SUCCESS);
                if (success==1){
                    Log.d("user created!",json.toString());
                    finish();
                    return json.getString(Tag_MESSAGE);
                }else{
                    Log.d("Register failure!",json.getString(Tag_MESSAGE));
                    return json.getString(Tag_MESSAGE);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            return  null;
        }
        protected void onPostExecute(String file_url){
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Registro.this,file_url,Toast.LENGTH_LONG).show();
            }
        }
    }
}