package br.com.pelisoli.ormlite.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by pelisoli on 09/12/15.
 */
@DatabaseTable(tableName = "person")
public class Person {
	@DatabaseField(id = true)
	private String identification_document;

	@DatabaseField
	private String name;

	@DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
	private City city;

	Person() {
	}

	public Person(String identification_document, String name, City city) {
		this.identification_document = identification_document;
		this.name = name;
		this.city = city;
	}

	public String getIdentification_document() {
		return identification_document;
	}

	public void setIdentification_document(String identification_document) {
		this.identification_document = identification_document;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}
