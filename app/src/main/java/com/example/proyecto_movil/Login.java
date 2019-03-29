package com.trivalia.trivalia;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class Login extends Activity implements OnClickListener {
    private EditText user , pass;
    private Button mSubmit,mResister;
    private ProgressDialog pDialog;

    //clase json
    JSONParse jsonParse = new JSONParse();

    private static  final String LOGIN_URL="http://brivalia.x10.mx/";
    //la respuesta del JSON
    private  static final String TAG_SUCCESS="success";
    private static final String TAG_MESSAGE="message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //se pasan los datos de la caja de texto
        user = (EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.password);

        mSubmit=(Button)findViewById(R.id.login);
        mResister=(Button)findViewById(R.id.register);

        mSubmit.setOnClickListener((OnClickListener) this);
        mResister.setOnClickListener((OnClickListener) this);


    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login:
                new AttempLogin().execute();
                break;
            case R.id.register:
                Intent i= new Intent(this,Registro.class);
                startActivity(i);
                break;
                default:
                    break;
        }
    }
    class AttempLogin extends AsyncTask <String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            int success;
            String username=user.getText().toString();
            String password= pass.getText().toString();
            try {
                List params= new ArrayList();
                params.add(new BasicNameValuePair("userneme","username") );
                params.add(new BasicNameValuePair("password","password"));
                Log.d("request!","starting");
                JSONObject json= jsonParse.makeHttpRequest(LOGIN_URL,"POST",params);
                Log.d("Login Attempt",json.toString());
                success= json.getInt(TAG_SUCCESS);
                if (success==1) {
                    Log.d("Login Succefull!", json.toString());
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Login.this);
                    Editor edit = sp.edit();
                    edit.putString("username", username);
                    edit.commit();

                    Intent i = new Intent(Login.this, ReadComentes.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog= new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected void OnPostExecute(String file_url){
            pDialog.dismiss();
            if(file_url!=null){
                Toast.makeText(Login.this,file_url,Toast.LENGTH_LONG).show();
            }
        }
    }
}
