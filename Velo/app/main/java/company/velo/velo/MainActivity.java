package company.velo.velo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    private Button registerBtn, loginBtn;

    private boolean saveLoginData;
    private SharedPreferences appData;

    private EditText[] Edittxt = new EditText[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Signup Button Click시 Activity 전환

        registerBtn = (Button) findViewById(R.id.signupButton);
        loginBtn = (Button) findViewById(R.id.loginButton);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterAccount.class);
                startActivity(intent);
            }
        });

        // Login 기능

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

        // 이전에 로그인 정보를 저장시킨 기록이 있다면 로그인 유지

        if (saveLoginData) {
            Intent intent = new Intent (MainActivity.this, BikeListPage.class);;
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
        }

        Edittxt[0] = (EditText) findViewById(R.id.emailInput);
        Edittxt[1] = (EditText) findViewById(R.id.passwordInput);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InsertData task = new InsertData();
                task.setContext(MainActivity.this);
                task.setString("Login.php", "phptest_MainActivity");

                task.execute();
            }
        });
    }

    // 설정값을 저장하는 함수
    private void save(String uid, String[] info) {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입(저장시킬 이름, 저장시킬 값) - 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", true);
        editor.putString("uid", uid.trim());
        editor.putString("user_email", Edittxt[0].getText().toString().trim());
        editor.putString("user_pw", Edittxt[1].getText().toString().trim());
        editor.putString("user_name", info[0].trim());
        editor.putString("user_id", info[1].trim());
        editor.putString("user_phone", info[2].trim());
        editor.putString("user_studentid", info[3].trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입(저장된 이름, 기본값) - 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
    }

    class InsertData extends AbstractInsertData {

        @Override
        String doSomething(MyTaskParams... params){

            String[] input = new String[2];

            for (int i=0;i<2;++i){
                input[i] = Edittxt[i].getText().toString();
            }

            return "email=" + input[0] + "&pw=" + input[1];
        }

        class Instant {
            public String login, uid, name, id, phone, studentid;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            Instant obj = new Gson().fromJson(result, Instant.class);

            if (obj.login.equals("1")){
                String[] info = {obj.name, obj.id, obj.phone, obj.studentid};
                save(obj.uid, info);
                login_success();
            } else {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void login_success() {
        Intent intent = new Intent (MainActivity.this, BikeListPage.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }
}
