package br.com.pelisoli.ormlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.pelisoli.ormlite.BuildConfig;
import br.com.pelisoli.ormlite.model.City;
import br.com.pelisoli.ormlite.model.Person;

/**
 * Created by pelisoli on 10/12/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "dbExample.db";

	private static final int DATABASE_VERSION = 1;

	private Dao<Person, String> personDao;

	private Dao<City, Integer> cityDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Person.class);
			TableUtils.createTable(connectionSource, City.class);
		} catch (SQLException e) {
			if(e != null && e.getMessage()!= null){
				Log.e(BuildConfig.LOG_TAG, e.getMessage());
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

	}

	public Dao<Person, String> getPersonDao() throws SQLException {
		if (personDao == null) {
			personDao = getDao(Person.class);
		}
		return personDao;
	}

	public Dao<City, Integer> getCityDao() throws SQLException {
		if (cityDao == null) {
			cityDao = getDao(City.class);
		}
		return cityDao;
	}
}
