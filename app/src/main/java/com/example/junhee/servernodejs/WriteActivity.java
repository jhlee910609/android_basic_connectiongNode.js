package com.example.junhee.servernodejs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class WriteActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editAuthor;
    private EditText editContent;
    private Button btnCancel;
    private Button btnSave;
    private boolean flag = false;
    public static final int RESULT_OK = 1231244;
    public static final int RESULT_CANCEL = 123489;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        initView();
        setOnClick();
    }

    private void setOnClick() {
        btnSave.setOnClickListener(v -> {
            postData(
                    editTitle.getText().toString(),
                    editAuthor.getText().toString(),
                    editContent.getText().toString()
            );
            setResult(RESULT_OK);
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCEL);
            finish();

        });
    }


    private void postData(String title, String author, String content) {

        /* 입력할 객체 생성*/
        Bbs bbs = new Bbs(title, author, content);

          /* 1. 레트로핏 생성 */
        // 1.1. 서버 생성
        Retrofit client = new Retrofit.Builder()
                .baseUrl(IBbs.SERVER)
//                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        /* 2. 서비스 연결 */
        IBbs myServer = client.create(IBbs.class);

        /* 3. 서비스의 특정 함수 호출 -> Observable 생성 */
        // 3.1. bbs 객체를 수동으로 전송하기 위해서는
        // 3.2. bbs 객체 -> json String 변환
        // 3.3. RequestBody에 미디어타입과 String 으로 변환된 데이터를
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(
                // MediaType.parse
                MediaType.parse("application/json"),
                gson.toJson(bbs)
        );
        Observable<ResponseBody> observable = myServer.write(requestBody);

        /* 4. subscribe 등록 */
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        responseBody -> {
                            String result = responseBody.string(); // 결과코드를 넘겨서 처리...
                            finish();
                            /**
                             * 1. 내가 정상적으로 새글을 읿력한 후 데이터를 전송하고 종료 -> 목록 갱신 해야됨
                             * 2. 새글을 전송하지 않고, 화면을 그냥 종료 -> 목록 갱신 안해도 됨 // 종료 버튼 혹은 백버튼
                             *
                             * 위의 두 가지 상황을 구분해서 결과값을 호출한 MainActivity로 넘겨서 처리
                             */

                        });
    }

    private void initView() {
        editTitle = (EditText) findViewById(R.id.title);
        editAuthor = (EditText) findViewById(R.id.author);
        editContent = (EditText) findViewById(R.id.content);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }
}
