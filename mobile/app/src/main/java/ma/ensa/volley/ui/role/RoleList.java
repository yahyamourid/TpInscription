package ma.ensa.volley.ui.role;

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
import ma.ensa.volley.adapters.FiliereAdapter;
import ma.ensa.volley.adapters.RoleAdapter;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.ui.filiere.AddFiliere;
import ma.ensa.volley.ui.filiere.FiliereList;
import ma.ensa.volley.ui.filiere.UpdateFiliere;

public class RoleList extends AppCompatActivity {


    private List<Role> roles = new ArrayList<>();
    private ListView rolesList;
    RequestQueue requestQueue;
    RoleAdapter roleAdapter ;
    private ImageButton addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_list);
        roleAdapter = new RoleAdapter(roles, this);
        getRoles();
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleList.this, AddRole.class);
                startActivity(intent);
            }
        });


    }

    public void getRoles(){
        String getSUrl = "http://192.168.1.109:8080/api/v1/roles";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                getSUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
//                Log.d("students",response.toString());
                TypeToken<List<Role>> token = new TypeToken<List<Role>>() {};
                roles = gson.fromJson(response.toString(), token.getType());
                rolesList = findViewById(R.id.roleslist);
                Log.d("student",roles.toString());
                roleAdapter.updaterolesList(roles);
                rolesList.setAdapter(roleAdapter);
                rolesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final TextView idstudent = view.findViewById(R.id.id);
                        Intent intent = new Intent(RoleList.this, UpdateRole.class);
                        intent.putExtra("id",idstudent.getText().toString());
                        RoleList.this.finish();
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