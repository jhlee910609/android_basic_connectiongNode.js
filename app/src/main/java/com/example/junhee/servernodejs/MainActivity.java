package com.example.junhee.servernodejs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MainActivity extends AppCompatActivity {


    private Button btnWrite;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    public List<Bbs> bbsList;
    public static final int REQ_CODE = 12345;
    private Retrofit client = null;
    private IBbs myServer = null;
    private int exId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bbsList = new ArrayList<>();
        initView();
        Log.e("Main", "onCreate exid :: " + this.exId);

        setAdatper();
        setRetrofit();
        loadAll();
    }

    private void setExId(String id){
        this.exId = Integer.parseInt(id);
    }

    private void setAdatper() {
        adapter = new RecyclerAdapter(bbsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        btnWrite = (Button) findViewById(R.id.btnWrite);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnWrite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WriteActivity.class);
            startActivityForResult(intent, REQ_CODE);
            /**
             * 호출 시, startActivity로 호출하지 말고... 다른 방법으로 작성하기
             */
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            switch (resultCode) {
                case WriteActivity.RESULT_CANCEL:
                    recyclerView.postInvalidate();
                    break;

                case WriteActivity.RESULT_OK:
                    setExId(bbsList.get(0).getId());
                    Log.e("Main", "onActivityResult exId :: " + this.exId);
                    getLastItem();
                    break;
            }
        }
    }

    private void setRetrofit() {
          /* 1. 레트로핏 생성 */
        // 1.1. 서버 생성
        client = new Retrofit.Builder()
                .baseUrl(IBbs.SERVER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        /* 2. 서비스 연결 */
        myServer = client.create(IBbs.class);
    }

    private void loadAll() {

        /* 3. 서비스의 특정 함수 호출 -> Observable 생성 */
        Observable<ResponseBody> observable = myServer.readAll();

          /* 4. subscribe 등록 */
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            // stream 류는 순서가 중요하다
                            String jsonString = responseBody.string();
                            Log.e("Retrofit", "datas :::: " + jsonString);
                            // 1. 데이터를 꺼내고
                            Gson gson = new Gson();
                    /* converting 하기 위한 타입지정 */
                            /**
                             *  1.1. type을 만들어서 처리하기
                             * Type type = new TypeToken<List<Bbs>>() {}.getType();
                             * List<Bbs> data = gson.fromJson(jsonString, type);
                             * 1.2. 객체 배열로 넘겨 받기
                             * 2. 데이터를 아답터에 세팅하고
                             */

                            Bbs data[] = gson.fromJson(jsonString, Bbs[].class);
                            Log.e("MainActivity", "data :::: " + data.length);

                            this.bbsList.clear();
                            for (Bbs bbs : data) {
                                this.bbsList.add(bbs);
                            }

                            // 3. 아답터 갱신
                            adapter.notifyDataSetChanged();
                            setExId(bbsList.get(0).getId());
                            Log.e("Main", "loadAll exid :: " + this.exId);

//                          adapter.notifyItemInserted(bbsList.size()+1);
                        });
    }

    private void getLastItem() {

        Observable<ResponseBody> lastItemObservable = myServer.readLastRecord();

        lastItemObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            String jsonString = responseBody.string();
                            Log.e("Retrofit", jsonString);
                            Gson gson = new Gson();
                            Bbs lastBbs[] = gson.fromJson(jsonString, Bbs[].class);
                            int lastBbsId = Integer.parseInt(lastBbs[lastBbs.length - 1].getId());
                            Log.e("lastItemObservable", "lastItemId == " + lastBbsId);
                            if (lastBbsId > this.exId) {
                                for (int i = 0; i < lastBbs.length; i++) {
                                    Log.e("lastItemObservable", "for 문 = " + i);
                                    bbsList.add(lastBbs[i]);
                                    adapter.notifyItemInserted(bbsList.size() - 1 + i);
                                }
                                setExId(bbsList.get(0).getId());
                                Log.e("Main", "getLastItem :: " + exId);

                            } else {
                                Log.e("lastItemObservable", "loadAll");
                                loadAll();
                            }
                        });
    }
}
