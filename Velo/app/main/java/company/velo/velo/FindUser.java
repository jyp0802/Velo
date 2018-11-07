package company.velo.velo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by jangsujin on 2017. 7. 23..
 */

public class FindUser extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_user);

        Button searchBtn = (Button)findViewById(R.id.button4);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText searchTxt = (EditText)findViewById(R.id.editText);
                String string = searchTxt.getText().toString();

                if (string.matches("")) {
                    Toast.makeText(FindUser.this, "Input", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(FindUser.this, string, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FindUser.this, VelowingListPage.class);
                    intent.putExtra("finduser", true);
                    intent.putExtra("finduser_txt", string);
                    startActivity(intent);
                }
            }
        });
    }
}
