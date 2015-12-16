package br.com.pelisoli.ormlite.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import br.com.pelisoli.ormlite.R;
import br.com.pelisoli.ormlite.activity.RegisterActivity;
import br.com.pelisoli.ormlite.activity.SearchActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
	private Button btnRegistration;

	private Button btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnRegistration = (Button) findViewById(R.id.btnRegistration);
		btnRegistration.setOnClickListener(this);

		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent intent;

		switch(view.getId()){
			case R.id.btnRegistration:
				intent = new Intent(this, RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.btnSearch:
				intent = new Intent(this, SearchActivity.class);
				startActivity(intent);
				break;

		}
	}
}
