package com.yyy.first;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String TAG = "MainActivity";
    public static WebSocket webSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TestAsync testAsync = new TestAsync();
        testAsync.execute("123");

        String url = "ws://10.0.2.2:8080/sp/websocket/socketServer.do";
        OkHttpClient oClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        WSListener wsListener = new WSListener();
        oClient.newWebSocket(request, wsListener);
        oClient.dispatcher().executorService().shutdown();

        this.findViewById(R.id.button).setOnClickListener(this);
        this.findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                if (webSocket != null) {
                    Log.d(TAG, webSocket.toString());
                    webSocket.send("123445556");
                }
                break;
            case R.id.button2:
                if (webSocket != null) {
                    byte bytes = 0x00;
                    Log.d(TAG, webSocket.toString());
                    webSocket.send(ByteString.of(bytes));
                }
                break;
                default:
        }
    }
}

class TestAsync extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        //OkHttp 本身就是异步的  所以
        //GET sample
        //String url = "http://www.baidu.com";
        /*String url = "http://10.0.2.2:8080/sp/producer";
        OkHttpClient oClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = oClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(MainActivity.TAG, "oClient onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d(MainActivity.TAG, "oClient onResponse " + response.body().string());
            }
        });*/

        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String body = "name:zzzzzzzz";
        RequestBody requestBody = new FormBody.Builder()
                .add("name", "zzzzzz")
                .build();
        //提交文本 json?
        /*Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/sp/test")
                .post(RequestBody.create(mediaType, body))
                .build();*/
        //提交表单
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/sp/test")
                .post(requestBody)
                .build();

        OkHttpClient oClite = new OkHttpClient();
        oClite.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("MainActivity", "TestAsync postExecute!!");
        super.onPostExecute(aVoid);
    }
}

class WSListener extends WebSocketListener {
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        MainActivity.webSocket = webSocket;
        Log.d(MainActivity.TAG, "onOpen");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(MainActivity.TAG, "onMessage string");
        super.onMessage(webSocket, text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.d(MainActivity.TAG, "onMessage byte");
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.d(MainActivity.TAG, "onClosing");
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.d(MainActivity.TAG, "onClosed");
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.d(MainActivity.TAG, "ws onFailure");
        super.onFailure(webSocket, t, response);
    }
}