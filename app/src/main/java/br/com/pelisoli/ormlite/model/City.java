package br.com.pelisoli.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by pelisoli on 09/12/15.
 */
@DatabaseTable(tableName = "city")
public class City {

	@DatabaseField(generatedId = true)
	private long id;

	@DatabaseField (unique = true)
	private String name;

	City() {
	}

	public City(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
