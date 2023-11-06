package ma.ensa.volley.ui.filiere;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.adapters.FiliereAdapter;
import ma.ensa.volley.adapters.StudentAdapter;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Student;
import ma.ensa.volley.ui.student.AddStudent;
import ma.ensa.volley.ui.student.StudentList;
import ma.ensa.volley.ui.student.UpdateStudent;

public class FiliereList extends AppCompatActivity {

    private List<Filiere> filieres = new ArrayList<>();
    private ListView filieresList;
    RequestQueue requestQueue;
    FiliereAdapter filiereAdapter ;
    private ImageButton addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_filieres);
        filiereAdapter = new FiliereAdapter(filieres, this);
        getFiliere();
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FiliereList.this, AddFiliere.class);
                startActivity(intent);
                FiliereList.this.finish();
            }
        });


    }

    public void getFiliere(){
        String getSUrl = "http://192.168.1.109:8080/api/v1/filieres";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getSUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
//                Log.d("students",response.toString());
                TypeToken<List<Filiere>> token = new TypeToken<List<Filiere>>() {};
                filieres = gson.fromJson(response.toString(), token.getType());
                filieresList = findViewById(R.id.filiereList);
                Log.d("student",filieres.toString());
                filiereAdapter.updatefilieresList(filieres);
                filieresList.setAdapter(filiereAdapter);
                filieresList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final TextView idstudent = view.findViewById(R.id.id);
                        Intent intent = new Intent(FiliereList.this, UpdateFiliere.class);
                        intent.putExtra("id",idstudent.getText().toString());
                        FiliereList.this.finish();
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