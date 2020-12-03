package dev.muathamer.webservicesdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayCourses(null);
    }

    public void displayCourses(View view) {
        System.out.println("onClickbtn");

        String url = "http://10.0.2.2/lamp/android-demo/getCourses.php";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    123);
        } else {
            DownloadTextTask runner = new DownloadTextTask();
            runner.execute(url);
        }
    }

    private InputStream OpenHttpConnection(String urlString) throws IOException {
        System.out.println("OpenHttpConnection");
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }

    private String DownloadText(String URL) {
        System.out.println("DownloadText");
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }

        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                //---convert the chars to a String---
                String readString =
                        String.copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            Log.d("Networking", e.getLocalizedMessage());
            return "";
        }
        return str;
    }



    private ArrayList<Course> parseStudents(String str){
        String[] coursesStr = str.split("\n");
        ArrayList<Course> courses = new ArrayList<>();

        for (String s : coursesStr) {
            String[] split = s.split("\t");
            int id = Integer.parseInt(split[0]);
            String name = split[1];
            String teacher = split[2];
            int studntsNo = Integer.parseInt(split[3]);

            courses.add(new Course(id, name, teacher, studntsNo));
        }
        return courses;
    }

    private void displayStudents(String str){

        ArrayAdapter<Course> listAdapter = new ArrayAdapter<Course>(this,
                android.R.layout.simple_list_item_1,
                parseStudents(str));

        ListView listView = findViewById(R.id.coursesList);
        listView.setAdapter(listAdapter);
    }

    private class DownloadTextTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("doInBackground");
            return DownloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("onPostExecute");
            System.out.println("res: " + result);

            displayStudents(result);
        }
    }
}