package com.example.alex.ferrytest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.alex.ferrytest.R;
/*

VicWeather: An app which displays weather data for Victoria BC
@author Alexander Steel


 */

public class MainActivity extends ActionBarActivity {

    TextView textMsg, textPrompt;
    final String textSource = "http://www.weatherlink.com/user/rvycweather/index.php?view=summary&headers=0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        textPrompt = (TextView)findViewById(R.id.textprompt);
        textMsg = (TextView) findViewById(R.id.msg);

        textPrompt.setText("Loading...");

        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        String textResult;

        @Override
        protected Void doInBackground(Void... params) { // Run background network access
            URL textUrl;

            try {
                textUrl = new URL(textSource);

                BufferedReader bufferReader = new BufferedReader(
                        new InputStreamReader(textUrl.openStream()));

                String stringBuffer;
                String stringText = "";

                int count = 4;
                boolean flag1 = false;

                while((stringBuffer = bufferReader.readLine()) != null) {

                    if (stringBuffer.contains("Outside Temp") || flag1){
                        flag1 = true;
                        count--;

                        if (count < 0)
                            flag1 = false;
                        //stringText += "current: ";

                        if (count != 0 && count != 3 )
                            if (count == 2)
                                stringText += stringBuffer + "right now \n";
                            else if (count == 1)
                                stringText += stringBuffer + "high temp\n";
                            else
                                stringText += stringBuffer + "low temp\n";
                    }

                }

                bufferReader.close();

                String tmp = "";
                count = 0;

                boolean flag = false;

                for(int i = 0; i < stringText.length(); i++){

                    if (stringText.charAt(i) == '>'){ // String parsing
                        flag = true;
                        continue;
                    }

                    if (stringText.charAt(i) == '<'){
                        flag = false;
                        //times.add(tmp);
                        if (count != 0)
                        tmp+= "\n";
                        count++;
                        continue;
                    }

                    if (flag)
                        tmp += stringText.charAt(i);

                }

                tmp = tmp.replaceAll("Outside Temp","");
                textResult = tmp;

            } catch(MalformedURLException e) { // Catch exceptions
                e.printStackTrace();
                textResult = e.toString();
            } catch(IOException e) {
                e.printStackTrace();
                textResult = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            textMsg.setText(textResult); // Display the grabbed text
            textPrompt.setText("");
            super.onPostExecute(result);
        }
    }
}
