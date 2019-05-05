package javaFX2tableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class TableViewController {

	@FXML
	private TableView<Cocktail> tbv01;
	@FXML
	private Button btn01;
	@FXML
	private TextArea txa01;

	@FXML
	private TableView<Map<String, String>> tbv02;
	@FXML
	private Button btn02;
	@FXML
	private TextArea txa02;

	private ArrayList<String> cocktailNames = new ArrayList<>();
	private HashMap<String, String> cocktailHmap = new HashMap<>();

	private ObservableList<Cocktail> cocktailDataModel = FXCollections.observableArrayList();
	public static final String Column1MapKey = "ename";
	public static final String Column2MapKey = "jname";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	void initialize() {

		// get Cocktail list
		{
			URL url = this.getClass().getResource("res/cocktail.csv");
			OpCsv csv = new OpCsv(url);

			TreeMap<Integer, String[]> map = csv.getCsv();
			Iterator<Integer> it = map.keySet().iterator();
			while (it.hasNext()) {
				int no = it.next();
				String[] words = map.get(no);
				String ename = words[0];
				String jname = words[1];
				if (cocktailHmap.containsKey(ename)) {
					String duplicateKey = ename + " ## duplicate ##";
					cocktailHmap.put(duplicateKey, jname + " (T_T)");
				}
				else {
					cocktailHmap.put(ename, jname);
				}
			}

			Iterator<String> itCocktail = (new TreeSet<>(cocktailHmap.keySet())).iterator(); // sort the key
			while (itCocktail.hasNext()) {
				cocktailNames.add(itCocktail.next());
			}
		}

		//////////////////////////////
		// Using Data Model
		assert tbv01 != null : "fx:id=\"tbv01\" was not injected: check your FXML file 'TableView.fxml'.";
		assert btn01 != null : "fx:id=\"btn01\" was not injected: check your FXML file 'TableView.fxml'.";
		assert txa01 != null : "fx:id=\"txa01\" was not injected: check your FXML file 'TableView.fxml'.";

		this.tbv01.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		TableColumn<Cocktail, String> enameColumn01 = new TableColumn<>("name(en)");
		// the "ename" string is used as a reference to an assumed
		// enameNameProperty() method in the Cocktail class type.
		enameColumn01.setCellValueFactory(new PropertyValueFactory<Cocktail, String>("ename"));

		TableColumn<Cocktail, String> jnameColumn01 = new TableColumn<>("name(jp)");
		// the "jname" string is used as a reference to an assumed
		// jnameNameProperty() method in the Cocktail class type.
		jnameColumn01.setCellValueFactory(new PropertyValueFactory<Cocktail, String>("jname"));

		this.tbv01.getColumns().clear();
		this.tbv01.getColumns().addAll(enameColumn01, jnameColumn01);

		//		cocktailDataModel.clear();
		for (String key : cocktailHmap.keySet()) {
			cocktailDataModel.add(new Cocktail(key, cocktailHmap.get(key)));
		}
		this.tbv01.setItems(cocktailDataModel);

		// sort
		this.tbv01.getSortOrder().clear();
		enameColumn01.setSortable(true);
		enameColumn01.setSortType(TableColumn.SortType.ASCENDING);
		// note : Executing the following code after adding the data.
		this.tbv01.getSortOrder().add(enameColumn01);

		//////////////////////////////
		// Using Map
		assert tbv02 != null : "fx:id=\"tbv02\" was not injected: check your FXML file 'TableView.fxml'.";
		assert btn02 != null : "fx:id=\"btn02\" was not injected: check your FXML file 'TableView.fxml'.";
		assert txa02 != null : "fx:id=\"txa02\" was not injected: check your FXML file 'TableView.fxml'.";

		this.tbv02.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		TableColumn<Map<String, String>, String> enameColumn02 = new TableColumn<>("name(en)");
		TableColumn<Map<String, String>, String> jnameColumn02 = new TableColumn<>("name(jp)");

		enameColumn02.setMinWidth(100);
		jnameColumn02.setMinWidth(100);

		enameColumn02.setCellValueFactory(new MapValueFactory(Column1MapKey));
		jnameColumn02.setCellValueFactory(new MapValueFactory(Column2MapKey));

		this.tbv02.setItems(generateDataInMap());

		this.tbv02.getColumns().clear();
		this.tbv02.getColumns().addAll(enameColumn02, jnameColumn02);

		Callback<TableColumn<Map<String, String>, String>, TableCell<Map<String, String>, String>> cellFactoryForMap =
				new Callback<TableColumn<Map<String, String>, String>, TableCell<Map<String, String>, String>>() {

					@Override
					public TableCell<Map<String, String>, String> call(TableColumn<Map<String, String>, String> p) {
						return new TextFieldTableCell<Map<String, String>, String>(new StringConverter<String>() {

							@Override
							public String toString(String t) {
								return t.toString();
							}

							@Override
							public String fromString(String s) {
								return s;
							}
						});
					}
				};

		enameColumn02.setCellFactory(cellFactoryForMap);
		jnameColumn02.setCellFactory(cellFactoryForMap);

		// sort
		this.tbv02.getSortOrder().clear();
		enameColumn02.setSortable(true);
		enameColumn02.setSortType(TableColumn.SortType.ASCENDING);
		// note : Executing the following code after adding the data.
		this.tbv02.getSortOrder().add(enameColumn02);
	}

	//////////////////////////////
	// Using Data Model
	@FXML
	void btn01OnAction(ActionEvent e) {
		this.setTxa01CocktailNames();
	}

	@FXML
	void tbv01DoubleClick(MouseEvent e) {
		if (e.getButton().equals(MouseButton.PRIMARY)) {
			if (e.getClickCount() == 2) {
				this.setTxa01CocktailNames();
			}
		}
	}

	private void setTxa01CocktailNames() {
		ObservableList<Cocktail> selected = FXCollections.observableArrayList(this.tbv01.getSelectionModel()
				.getSelectedItems());

		// sort
		java.util.Collections.sort(selected, new java.util.Comparator<Cocktail>() {
			@Override
			public int compare(Cocktail o1, Cocktail o2) {
				return o1.getEname().compareTo(o2.getEname());
			}
		});

		if (selected.size() > 0) {

			StringBuffer s = new StringBuffer();
			for (Cocktail item : selected) {
				s.append(item.getEname() + " (" + item.getJname() + ")\n");
			}
			this.txa01.setText(s.toString());
			this.tbv01.getSelectionModel().clearSelection();
		}
	}

	//////////////////////////////
	// Using Map
	private ObservableList<Map<String, String>> generateDataInMap() {
		ObservableList<Map<String, String>> allData = FXCollections.observableArrayList();

		for (String key : cocktailHmap.keySet()) {
			Map<String, String> dataRow = new HashMap<>();

			dataRow.put(Column1MapKey, key);
			dataRow.put(Column2MapKey, cocktailHmap.get(key));
			allData.add(dataRow);
		}
		return allData;
	}

	@FXML
	void btn02OnAction(ActionEvent e) {
		this.setTxa02CocktailNames();
	}

	@FXML
	void tbv02DoubleClick(MouseEvent e) {
		if (e.getButton().equals(MouseButton.PRIMARY)) {
			if (e.getClickCount() == 2) {
				this.setTxa02CocktailNames();
			}
		}
	}

	private void setTxa02CocktailNames() {
		ObservableList<Map<String, String>> selected = FXCollections.observableArrayList(this.tbv02.getSelectionModel()
				.getSelectedItems());

		// sort
		java.util.Collections.sort(selected, new java.util.Comparator<Map<String, String>>() {
			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				return o1.get(Column1MapKey).compareTo(o2.get(Column1MapKey));
			}
		});

		if (selected.size() > 0) {
			StringBuffer s = new StringBuffer();
			for (Map<String, String> item : selected) {
				s.append(item.get(Column1MapKey) + " (" + item.get(Column2MapKey) + ")\n");
			}
			this.txa02.setText(s.toString());
			this.tbv02.getSelectionModel().clearSelection();
		}
	}

}
