package br.com.pelisoli.ormlite.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

import br.com.pelisoli.ormlite.BuildConfig;
import br.com.pelisoli.ormlite.R;
import br.com.pelisoli.ormlite.helper.DatabaseHelper;
import br.com.pelisoli.ormlite.model.City;
import br.com.pelisoli.ormlite.model.Person;

/**
 * Created by pelisoli on 09/12/15.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

	private DatabaseHelper databaseHelper = null;

	private EditText edtDoc;

	private EditText edtName;

	private EditText edtCity;

	private Button btnSave;

	private ProgressDialog alertDialog;

	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

		edtDoc = (EditText) findViewById(R.id.edtDoc);

		edtName = (EditText) findViewById(R.id.edtName);

		edtCity = (EditText) findViewById(R.id.edtCity);

		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		getSupportActionBar().setTitle(R.string.app_name);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		alertDialog = new ProgressDialog(this);
		alertDialog.setMessage(getString(R.string.saving));
		alertDialog.setCancelable(false);

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
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btnSave:

				//Close soft keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

				Person person = new Person(edtDoc.getText().toString(),
						edtName.getText().toString(), new City(edtCity.getText().toString().toUpperCase()));


				alertDialog.show();

				new RegisterAsyncTask().execute(person);
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// Respond to the action bar's Up/Home button
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class RegisterAsyncTask extends AsyncTask<Person, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Person... persons) {
			boolean status = false;


			if(persons!= null && persons.length > 0){
				try {

					//Check if city is already on database
					List<City> city = getHelper().getCityDao().queryBuilder().where().eq("name", persons[0].getCity().getName()).query();

					if(city != null && city.size()  > 0 ){
						persons[0].setCity(city.get(0));
					}else{
						getHelper().getCityDao().create(persons[0].getCity());
					}

					//save register
					getHelper().getPersonDao().createOrUpdate(persons[0]);
					status = true;

				} catch (SQLException e) {
					if(e!= null && e.getMessage() != null){
						Log.e(BuildConfig.LOG_TAG, e.getMessage());
					}
				}
			}

			//it will cancel progress dialog
			publishProgress();

			return status;
		}

		@Override
		protected void onPostExecute(Boolean status) {
			super.onPostExecute(status);

			if(status){
				Snackbar.make(relativeLayout, R.string.success_on_saving, Snackbar.LENGTH_SHORT).show();
			}else{
				Snackbar.make(relativeLayout, R.string.error_on_saving, Snackbar.LENGTH_SHORT).show();
			}

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);

			alertDialog.cancel();

		}
	}
}
