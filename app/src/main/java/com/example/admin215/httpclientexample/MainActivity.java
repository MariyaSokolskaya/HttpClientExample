package com.example.admin215.httpclientexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    EditText name, surname;
    Button sendButton;
    String nameStr, surnameStr, httpAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        sendButton = (Button) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameStr = name.getText().toString();
                surnameStr = surname.getText().toString();
                MyAsyncTask task = new MyAsyncTask();
                task.execute();
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://icomms.ru/inf/meteo.php?tid=1");
          //  List<NameValuePair> nameValuePairs = new ArrayList<>(2);
           // nameValuePairs.add(new BasicNameValuePair("name", nameStr));
           // nameValuePairs.add(new BasicNameValuePair("surname", surnameStr));
            try {
             //   httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if(httpResponse.getStatusLine().getStatusCode() == 200){
                    HttpEntity httpEntity = httpResponse.getEntity();
                    httpAnswer = EntityUtils.toString(httpEntity);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray weather = new JSONArray(httpAnswer);
                JSONObject weatherObj = weather.getJSONObject(0);
                String temp = weatherObj.getString("temp");
                String feel = weatherObj.getString("feel");
                surname.setText("Температура на 00:00 - " + temp);
                name.setText("Температура на 00:00 ощущается как - " + feel);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
