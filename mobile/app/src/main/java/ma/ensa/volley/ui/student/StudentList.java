package ma.ensa.volley.ui.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.adapters.StudentAdapter;
import ma.ensa.volley.beans.Student;

public class StudentList extends AppCompatActivity {
    private List<Student> students = new ArrayList<>();
    private ListView studentsList;
    RequestQueue requestQueue;
    StudentAdapter studentAdapter ;
    private ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        studentAdapter = new StudentAdapter(students, this);
        getStuents();
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentList.this, AddStudent.class);
                startActivity(intent);
                StudentList.this.finish();
            }
        });


    }

    public void getStuents(){
        String getSUrl = "http://192.168.1.109:8080/api/v1/students";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getSUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
//                Log.d("students",response.toString());
                TypeToken<List<Student>> token = new TypeToken<List<Student>>() {};
                students = gson.fromJson(response.toString(), token.getType());
                studentsList = findViewById(R.id.studentlList);
                Log.d("student",students.toString());
                studentAdapter.updateStudentsList(students);
                studentsList.setAdapter(studentAdapter);
                studentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final TextView idstudent = view.findViewById(R.id.id);
                        Intent intent = new Intent(StudentList.this, UpdateStudent.class);
                        intent.putExtra("id",idstudent.getText().toString());
                        StudentList.this.finish();
                        StudentList.this.finish();
                        startActivity(intent);
                    }
                });

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }

}