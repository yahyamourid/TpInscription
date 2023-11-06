package ma.ensa.volley.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Student;

public class StudentAdapter extends BaseAdapter {
    private List<Student> students;
    private LayoutInflater inflater;

    public StudentAdapter(List<Student> students, Activity activity) {
        this.students = students;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void updateStudentsList(List<Student> newStudents) {
        students.clear();
        students.addAll(newStudents);
        notifyDataSetChanged(); // Informez l'adaptateur du changement de données
    }


    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return students.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.student_item, null);
        TextView id = convertView.findViewById(R.id.id);
        TextView firstName = convertView.findViewById(R.id.firstName);
        TextView lastName = convertView.findViewById(R.id.lastName);
        TextView email = convertView.findViewById(R.id.email);
        TextView  phone= convertView.findViewById(R.id.phone);
        firstName.setText(students.get(position).getFirstName());
        lastName.setText(students.get(position).getLastName());
        email.setText(students.get(position).getEmail());
        phone.setText(students.get(position).getPhone()+"");
        id.setText(students.get(position).getId()+"");
        return convertView;
    }
}
