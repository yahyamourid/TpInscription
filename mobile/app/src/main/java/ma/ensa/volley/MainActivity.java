package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ma.ensa.volley.ui.filiere.FiliereList;
import ma.ensa.volley.ui.role.RoleList;
import ma.ensa.volley.ui.student.StudentList;

public class MainActivity extends AppCompatActivity {

    private Button student, major, role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        student = findViewById(R.id.student);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentList.class);
                startActivity(intent);
            }
        });
        major = findViewById(R.id.major);
        major.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FiliereList.class);
                startActivity(intent);
            }
        });
        role = findViewById(R.id.role);
        role.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoleList.class);
                startActivity(intent);
            }
        });


    }


}