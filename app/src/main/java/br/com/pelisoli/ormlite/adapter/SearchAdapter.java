package br.com.pelisoli.ormlite.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.pelisoli.ormlite.R;
import br.com.pelisoli.ormlite.model.Person;

/**
 * Created by pelisoli on 21/11/15.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Person> personList;

    private LayoutInflater mLayoutInflater;


    public SearchAdapter(Context context, List<Person> personList) {
        this.personList = personList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater
                .inflate(R.layout.item_person, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtDocNumber.setText(personList.get(position).getIdentification_document());

        holder.txtName.setText(personList.get(position).getName());

        holder.txtCity.setText(personList.get(position).getCity().getName());

    }

    @Override
    public int getItemCount() {
        int size = 0;

        if(personList != null) {
            size = personList.size();
        }

        return size;
    }


    public void addPersonList(List<Person> personList){
        this.personList = personList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtDocNumber;

        private TextView txtName;

        private TextView txtCity;

        public ViewHolder(View itemView) {
            super(itemView);

            txtDocNumber = (TextView) itemView.findViewById(R.id.txtDocNumber);

            txtName = (TextView) itemView.findViewById(R.id.txtName);

            txtCity = (TextView) itemView.findViewById(R.id.txtCity);
        }
    }
}
