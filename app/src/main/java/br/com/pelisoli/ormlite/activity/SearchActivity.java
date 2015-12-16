package br.com.pelisoli.ormlite.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.pelisoli.ormlite.BuildConfig;
import br.com.pelisoli.ormlite.R;
import br.com.pelisoli.ormlite.adapter.SearchAdapter;
import br.com.pelisoli.ormlite.helper.DatabaseHelper;
import br.com.pelisoli.ormlite.model.Person;

/**
 * Created by pelisoli on 19/11/15.
 */
public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSearch;

    private Button btnSearch;

    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;

    private SearchAdapter searchAdapter;

    private ArrayList<Person> personList;

    private DatabaseHelper databaseHelper = null;

    private ProgressDialog alertDialog;

    private static boolean SHOW_PROGRESS_DIALOG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        //UI components
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        //Button listener
        btnSearch.setOnClickListener(this);

        if(savedInstanceState != null){
            personList = (ArrayList<Person>) savedInstanceState.getSerializable("personList");
        }

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recycler view with all addresses found in search
        searchAdapter = new SearchAdapter(getApplicationContext(), personList);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setVisibility(View.VISIBLE);

        alertDialog = new ProgressDialog(this);
        alertDialog.setMessage(getString(R.string.loading));
        alertDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSearch:
                recyclerView.setVisibility(View.GONE);

                //It's not allowed an empty string
                if(edtSearch.getText().toString().equals("")){

                    //Show warning message
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.empty_search));
                    builder.setPositiveButton(R.string.ok_label, null);
                    builder.show();

                }else {
                    //Start communication with API
                    new SeachRegister().execute(edtSearch.getText().toString());
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Saving instance information
        outState.putSerializable("personList", personList);
    }

    private class SeachRegister extends AsyncTask<String, Boolean, List<Person>>{

        @Override
        protected List<Person> doInBackground(String... searchStrings) {
            List<Person> personList = null;

            publishProgress(SHOW_PROGRESS_DIALOG);

            if(searchStrings != null && searchStrings.length > 0){
                try {

                    personList = getHelper().getPersonDao().queryBuilder().where().like("name", "%" + searchStrings[0] + "%").query();

                } catch (SQLException e) {
                    if(e != null && e.getMessage() != null) {
                        Log.e(BuildConfig.LOG_TAG, e.getMessage());
                    }
                }
            }

            return personList;
        }

        @Override
        protected void onProgressUpdate(Boolean... status) {
            super.onProgressUpdate(status);

            if(status != null && status.length > 0){
                if(status[0]){
                    alertDialog.show();
                }else{
                    alertDialog.cancel();
                }
            }
        }

        @Override
        protected void onPostExecute(List<Person> personNewList) {
            super.onPostExecute(personNewList);
            
            if(personNewList == null){
                personList = null;
            }else{
                personList = new ArrayList(personNewList);
            }
            
            //Hide progress bar
            alertDialog.cancel();

            //Add address list to recycler view
            searchAdapter.addPersonList(personList);

            //show recycler view
            recyclerView.setVisibility(View.VISIBLE);

        }
    }
}
