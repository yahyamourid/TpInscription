package ma.ensa.volley.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ma.ensa.volley.R;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;

public class RoleAdapter extends BaseAdapter {
    private List<Role> roles;
    private LayoutInflater inflater;

    public RoleAdapter(List<Role> roles, Activity activity) {
        this.roles = roles;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void updaterolesList(List<Role> newfilieres) {
        roles.clear();
        roles.addAll(newfilieres);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return roles.size();
    }

    @Override
    public Object getItem(int position) {
        return roles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return roles.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.role_item, null);
        TextView id = convertView.findViewById(R.id.id);
        TextView code = convertView.findViewById(R.id.name);
        id.setText(roles.get(position).getId()+"");
        code.setText(roles.get(position).getName());
        return convertView;
    }
}
