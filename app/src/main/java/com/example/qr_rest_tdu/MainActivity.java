package com.example.qr_rest_tdu;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import android.widget.CheckBox;
import org.json.JSONObject;
import java.lang.*;

public class MainActivity extends AppCompatActivity {
    public static TextView tvresult;
    public static TextView textNetwork;
    public static TextView postAntwort;
    CheckBox shuttle;
    CheckBox mensa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new RequestAsync().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shuttle = (CheckBox) findViewById(R.id.shuttle_checkBox2);
        mensa = (CheckBox) findViewById(R.id.mensa_checkBox);
        tvresult = (TextView) findViewById(R.id.tvresult);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        Button check_connection = (Button) findViewById(R.id.netz_button);
        textNetwork = (TextView) findViewById(R.id.text_network);
        check_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedToNetwork(getApplicationContext())) {
                    textNetwork.setText("1");
                    textNetwork.setBackgroundColor(Color.GREEN);
                } else {
                    textNetwork.setText("0");
                    textNetwork.setBackgroundColor(Color.RED);
                }

            }
        });
        postAntwort = (TextView) findViewById(R.id.HTTPresult);
        Button postHttp = (Button) findViewById(R.id.post_btn);
        postHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // postAntwort.setText(getData());
                // postAntwort.setText(HTTP_post());
                RequestAsync req = new RequestAsync();
                postAntwort.setText(req.doInBackground());
                req.onPostExecute(getData());
                //postAntwort.setText(req.sendPost("http://45.77.214.216:8080", get_json_Data()));
            }
        });
    }
    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                //return RequestHandler.sendGet("https://prodevsblog.com/android_get.php");

                // POST Request
                JSONObject postDataParams = new JSONObject();
                postDataParams = get_json_Data();

                return RequestHandler.sendPost("http://45.77.214.216:8080", postDataParams);
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }


    public JSONObject buidJsonObject(String s1, String s2) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("qrCode", s1);
            jsonObject.accumulate("priceID", s2);
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }

    public String getData() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("qrCode", tvresult.getText());
            if (mensa.isChecked() && !(shuttle.isChecked())) {
                jsonObject.accumulate("priceID", "mensa");
            }
            if (!(mensa.isChecked()) && shuttle.isChecked()) {
                jsonObject.accumulate("priceID", "shuttle");
            }
            return jsonObject.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    public JSONObject get_json_Data() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("qrCode", tvresult.getText());
            if (mensa.isChecked() && !(shuttle.isChecked())) {
                jsonObject.accumulate("priceID", "mensa");
            }
            if (!(mensa.isChecked()) && shuttle.isChecked()) {
                jsonObject.accumulate("priceID", "shuttle");
            }
            return jsonObject;
        } catch (Exception ex) {
            return null;
        }
    }
}






