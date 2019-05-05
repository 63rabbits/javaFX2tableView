package javaFX2tableView;

import javafx.beans.property.SimpleStringProperty;

public class Cocktail {
	private final SimpleStringProperty ename;
	private final SimpleStringProperty jname;

	Cocktail(String ename, String jname) {
		this.ename = new SimpleStringProperty(ename);
		this.jname = new SimpleStringProperty(jname);
	}

	public String getEname() {
		return this.ename.get();
	}

	public String getJname() {
		return this.jname.get();
	}

	public void setEname(String name) {
		ename.set(name);
	}

	public void setJname(String name) {
		jname.set(name);
	}

}
