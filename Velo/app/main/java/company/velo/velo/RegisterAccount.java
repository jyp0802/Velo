package company.velo.velo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterAccount extends AppCompatActivity {

    private EditText[] Edittxt = new EditText[5];
    private TextInputLayout[] textInputLayouts = new TextInputLayout[5];
    private SharedPreferences appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_account);

        appData = getSharedPreferences("appData", MODE_PRIVATE);

        Edittxt[0] = (EditText)findViewById(R.id.name_text);
        Edittxt[1] = (EditText)findViewById(R.id.id_text);
        Edittxt[2] = (EditText)findViewById(R.id.password_text);
        Edittxt[3] = (EditText)findViewById(R.id.email_text);
        Edittxt[4] = (EditText)findViewById(R.id.phone_number_text);

        textInputLayouts[0] = (TextInputLayout)findViewById(R.id.name_text_layout);
        textInputLayouts[1] = (TextInputLayout)findViewById(R.id.id_text_layout);
        textInputLayouts[2] = (TextInputLayout)findViewById(R.id.password_text_layout);
        textInputLayouts[3] = (TextInputLayout)findViewById(R.id.email_text_layout);
        textInputLayouts[4] = (TextInputLayout)findViewById(R.id.phone_number_text_layout);

        Button buttonRegister = (Button)findViewById(R.id.button2);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] reg_array = {"Name","ID","Password","Email","Phone Number"};
                boolean ret = false;
                for (int i = 0; i < 5; i++) {
                    if (Edittxt[i].getText().toString().length()<1)
                    {
                        textInputLayouts[i].setError("Please enter " + reg_array[i]);
                        ret = true;
                    } else
                    {
                        textInputLayouts[i].setError(null);
                    }
                }
                if (ret) return;

                if (!Edittxt[3].getText().toString().contains("@"))
                {
                    textInputLayouts[3].setError("Please enter a valid email address");
                    return;
                }
                if (Edittxt[4].getText().toString().length() < 8)
                {
                    textInputLayouts[4].setError("Please enter a valid phone number");
                    return;
                }

                InsertData task = new InsertData();
                task.setContext(RegisterAccount.this);
                task.setString("RegisterAccount.php", "phptest_RegisterAccount");

                task.execute();

                SharedPreferences.Editor editor = appData.edit();
                editor.putBoolean("SAVE_LOGIN_DATA", false);
                editor.apply();
                Intent intent = new Intent(RegisterAccount.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    class InsertData extends AbstractInsertData {

        @Override
        String doSomething(MyTaskParams... params){
            String[] input = new String[5];

            for (int i=0;i<5;++i){
                input[i] = Edittxt[i].getText().toString();
            }

            return "name=" + input[0] + "&id=" + input[1] + "&pw="+ input[2] +"&email=" + input[3] + "&phone=" + input[4] + "&studentid=" + "0" ;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String msg = null;

            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);

            if(result.equals("success")){
                msg = "Welcome. Registration is complete.";
            } else if (result.equals("duplicated")){
                msg = "Duplicate ID. Please try another ID.";
            } else {
                msg = "Sorry. Registration is failed.";
            }

            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }
}