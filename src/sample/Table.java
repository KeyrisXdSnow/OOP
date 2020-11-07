package sample;

import com.sun.jdi.Value;
import computer.architecture.comType.PersonalComputer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.lang.reflect.*;
import javafx.util.Callback;
import shop.Shop;

import java.util.*;

public class Table <T extends Pane, O extends Shop> {

    private TableView<ObservableList<StringProperty>> table, rootTable;
    private Pane pane;
    private String columnCountName = "кол-во", ColumnDeleteName = "", columnDeleteButtonName = "удалить";
    private String rangeIdentification = "name"; // ???????????????????????????????????????????????????????
    private int rangeIdentifierColumnIndex = -1;
    private String selectedType;
    private O shopData;
    private int rootIndex = 0;
    boolean weTryAddGadget = false;

    public TableView<ObservableList<StringProperty>> getTable() { return table; }

    Table(T controller, O shopData, TableView rootTable) {

        this.shopData = shopData;
        this.rootTable = rootTable;

        table = new TableView<>();
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initTableAction();

        pane = new Pane();
        pane.setVisible(false);

        pane.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            if (pane.isVisible()) {
                pane.getChildren().clear();
                pane.setVisible(false);
            }
        });

        controller.getChildren().add(table);
        controller.getChildren().add(pane);

        table.setVisible(false);

    }

    private Table(T controller, O shopData, int index, TableView rootTable, int rangeIdentifierColumnIndex) {
        this(controller, shopData, rootTable);
        this.rootIndex = index;
        this.rangeIdentifierColumnIndex = rangeIdentifierColumnIndex;
    }

    private Table(T controller, O shopData, int index, TableView rootTable, String selectedType,
                  String tableID, int rangeIdentifierColumnIndex) {
        this(controller, shopData, index, rootTable, rangeIdentifierColumnIndex);
        this.selectedType = selectedType;
        table.setId(tableID);

    }

    void refresh(T controller, String selectedType)
            throws ClassNotFoundException, IllegalAccessException {

        table.setVisible(true);
        table.setId(selectedType);
        table.getColumns().clear();
        table.getItems().clear();
        rootTable = table;

        table.setPrefWidth(controller.getWidth());
        table.setPrefHeight(controller.getHeight());

        this.selectedType = selectedType;

        createMainTableColumns();
        createMainTableItems(table);

    }

    private void refresh(double weight, double height, Object object, Field field)
            throws ClassNotFoundException, IllegalAccessException {

        table.setVisible(true);
        table.getColumns().clear();
        table.getItems().clear();

        table.setPrefWidth(weight);
        table.setPrefHeight(height);


        createMainTableColumns(object, field);
        table.getItems().add(createItem(object));

    }

    private void initTableAction() {
        table.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                table.getSelectionModel().getSelectedItem().get(table.getSelectionModel().getSelectedIndex());
                //   изменение поля
            }
        });

    }

    private void createMainTableColumns() throws ClassNotFoundException, IllegalAccessException {

        List techTypes = new ArrayList(shopData.getTechniqueTypes());

        Object[] list = shopData.getTypedEquipment(selectedType).keySet().toArray();
        if (list == null) return;
        final int[] j = {0};

        Class aClass = Class.forName(String.valueOf(techTypes.get(techTypes.indexOf(selectedType))));

        String className = aClass.getName();
        final int[] count = {0};
        {
            new Object() {
                {
                }

                void addColumn(Class aClass) throws IllegalAccessException {
                    if (!aClass.getSimpleName().equals(Object.class.getSimpleName())) {
                        addColumn(aClass.getSuperclass());
                    } else return;

                    Field[] fields = aClass.getDeclaredFields();

                    for (Field field : fields) {
                        field.setAccessible(true);

                        if (field.getType().isPrimitive() ^
                                (field.getType().getName().equals(String.class.getName())) ^
                                (field.getType().getName().equals(Character.class.getName())) ^
                                (field.getType().getName().equals(Integer.class.getName())) ^
                                (field.getType().getName().equals(Double.class.getName())) ^
                                (field.getType().getName().equals(Float.class.getName())) ^
                                (field.getType().getName().equals(Long.class.getName())) ^
                                (field.getType().getName().equals(Short.class.getName())) ^
                                (field.getType().getName().equals(Boolean.class.getName())) ^
                                (field.getType().getName().equals(Byte.class.getName()))) {

                            if (Modifier.isProtected(field.getModifiers()) ^ Modifier.isPublic(field.getModifiers()) ^
                                    Modifier.isFinal(field.getModifiers()) ^ Modifier.isStatic(field.getModifiers())) {
                                table.getColumns().add(createColumn(field, count[0]++));
                            } else {
                                if (aClass.getName().equals(className)) {
                                    if (Modifier.isPrivate(field.getModifiers()) ^
                                            Modifier.isStatic(field.getModifiers()) ^
                                            Modifier.isFinal(field.getModifiers()) ^
                                            Modifier.isPublic(field.getModifiers()) ^ Modifier.isProtected(field.getModifiers()))
                                        table.getColumns().add(createColumn(field, count[0]++));
                                }
                            }
                        } else {
                            if (list.length != 0)
                                table.getColumns().add(createObjectColumn(field.get(list[j[0]++ % list.length]), field, field.getName().replaceFirst("class", "").trim(),
                                        count[0]++));
                        }
                    }
                }

            }.addColumn(aClass);
        }
        table.getColumns().add(createColumn(columnCountName, count[0]++));
        createButtonColumn(table, ColumnDeleteName, columnDeleteButtonName);

        int i = 0;
        for (TableColumn column : rootTable.getColumns()) {
            if (column.getText().equals(rangeIdentification)) {
                rangeIdentifierColumnIndex = i;
                break;
            }
            i++;
        }
    }

    private void createMainTableColumns(Object object, Field field) throws ClassNotFoundException, IllegalAccessException {

        Class aClass = Class.forName(field.getType().toString().replaceFirst("class", "").trim());
        String className = aClass.getName();
        final int[] count = {0};
        {
            new Object() {
                {
                }

                void addColumn(Class aClass) throws IllegalAccessException {
                    if (!aClass.getSimpleName().equals(Object.class.getSimpleName())) {
                        addColumn(aClass.getSuperclass());
                    } else return;

                    Field[] fields = aClass.getDeclaredFields();

                    for (Field field : fields) {

                        field.setAccessible(true);

                        if (field.getType().isPrimitive() ^
                                (field.getType().getName().equals(String.class.getName())) ^
                                (field.getType().getName().equals(Character.class.getName())) ^
                                (field.getType().getName().equals(Integer.class.getName())) ^
                                (field.getType().getName().equals(Double.class.getName())) ^
                                (field.getType().getName().equals(Float.class.getName())) ^
                                (field.getType().getName().equals(Long.class.getName())) ^
                                (field.getType().getName().equals(Short.class.getName())) ^
                                (field.getType().getName().equals(Boolean.class.getName())) ^
                                (field.getType().getName().equals(Byte.class.getName()))) {

                            if (Modifier.isProtected(field.getModifiers()) ^ Modifier.isPublic(field.getModifiers()) ^
                                    Modifier.isFinal(field.getModifiers()) ^ Modifier.isStatic(field.getModifiers())) {
                                table.getColumns().add(createColumn(field, count[0]++));
                            } else {
                                if (aClass.getName().equals(className)) {
                                    if (Modifier.isPrivate(field.getModifiers()) ^
                                            Modifier.isStatic(field.getModifiers()) ^
                                            Modifier.isFinal(field.getModifiers()) ^
                                            Modifier.isPublic(field.getModifiers()) ^ Modifier.isProtected(field.getModifiers()))
                                        table.getColumns().add(createColumn(field, count[0]++));
                                }
                            }
                        } else {
                            if (object != null) {

                                if (object.getClass().getName().contains("util") || !object.getClass().isPrimitive()) {

                                    table.getColumns().add(createObjectColumn(field.get(object), field, field.getName().replaceFirst("class", "").trim(), count[0]++));

                                } else table.getColumns().add(createColumn(field, count[0]++));

                            } else table.getColumns().add(createColumn("null", count[0]++));
                        }
                    }
                }

            }.addColumn(aClass);
        }
    }

    private boolean isShellClasses(String type) {

        if (type.equals(String.class.getName()) ^
                type.equals(Character.class.getName()) ^
                type.equals(Integer.class.getName()) ^
                type.equals(Double.class.getName()) ^
                type.equals(Float.class.getName()) ^
                type.equals(Long.class.getName()) ^
                type.equals(Short.class.getName()) ^
                type.equals(Boolean.class.getName()) ^
                type.equals(Byte.class.getName())) return true;

        return false;

    }

    private TableColumn createObjectColumn(Object object, Field field, String name, int columnIndex) throws IllegalAccessException {

        TableColumn buttonCol = new TableColumn(name);

        if (object != null) buttonCol.setId(field.getDeclaringClass().toString());
        else buttonCol.setId("null");

        buttonCol.setCellValueFactory(new PropertyValueFactory<>(""));

        Callback<TableColumn<Value, String>, TableCell<Value, String>> cellFactory = new Callback<>() {

            @Override
            public TableCell call(final TableColumn<Value, String> param) {
                final TableCell<Value, String> cell = new TableCell<>() {
                    final Button button = new Button();
                    Table tableIn;

                    @Override
                    protected void updateItem(String item, boolean empty) {

                        button.setText("Нажми на меня :З");

                        button.setOnMouseClicked(mouseEvent -> {

                            if (!pane.isVisible()) {
                                try {
                                    if (table.equals(rootTable)) rootIndex = getIndex();

                                    Object gadget = getGadget(selectedType,
                                            rootTable.getItems().get(rootIndex).get(rangeIdentifierColumnIndex).getValueSafe()); // аааааааааааааааааааа

                                    Object[] object = {gadget, gadget};
                                    final Field[] columnField = {null, null};
                                    final boolean[] find = {false};

                                    new Object() {
                                        {
                                        }

                                        void getSuperClass(Class aClass, Object ourObject) throws IllegalAccessException {
                                            if (!aClass.getSimpleName().equals(Object.class.getSimpleName())) {
                                                getSuperClass(aClass.getSuperclass(), ourObject);
                                            } else return;

                                            if (find[0]) return;

                                            for (Field f : aClass.getDeclaredFields()) {

                                                if (find[0]) return;

                                                f.setAccessible(true);

                                                if (f.getType().getName().contains("util")) {
                                                    if (f.getName().equals(getTableColumn().getText())) {
                                                        columnField[0] = f;
                                                        object[0] = ourObject;
                                                        find[0] = true;
                                                        return;
                                                    } else continue;
                                                }

                                                if (f.getDeclaringClass().toString().equals(getTableColumn().getId()) && f.getName().equals(getTableColumn().getText())) {
                                                    columnField[0] = f;
                                                    object[0] = ourObject;
                                                    find[0] = true;
                                                    return;
                                                } else {
                                                    if (f.get(ourObject) != null) {
                                                        if (!f.getType().isPrimitive() &&
                                                                !isShellClasses(f.getType().toString().replaceFirst("class", "").trim()))
                                                            getSuperClass(f.get(ourObject).getClass(), f.get(ourObject));
                                                    }
                                                }
                                            }
                                        }
                                    }.getSuperClass(gadget.getClass(), gadget);

                                    Object classField;

                                    if (columnField[0] != null) classField = columnField[0].get(object[0]);
                                    else {
                                        columnField[0] = columnField[1];
                                        classField = object[0];
                                    }

                                    pane.setVisible(true);
                                    pane.setLayoutX(button.getParent().getLayoutX());
                                    pane.setLayoutY(button.getParent().getParent().getLayoutY() + button.getHeight());

                                    if (classField == (null)) {

                                        button.setText("empty");
                                        pane.setVisible(true);

                                        tableIn = new Table(pane, shopData, rootIndex, rootTable, selectedType, "", rangeIdentifierColumnIndex);
                                    } else {

                                        tableIn = new Table(pane, shopData, rootIndex, rootTable, selectedType,
                                                field.getName(), rangeIdentifierColumnIndex);
                                        if (weTryAddGadget) tableIn.setWeTryAddGadget(true);
                                        try {
                                            if (columnField[0].getType().isArray()) {
                                                tableIn.addColumnCollection(pane.getPrefWidth(),
                                                        pane.getPrefHeight(), Collections.singleton(classField));
                                                tableIn.addItemsArray(classField);
                                            }
                                        } catch (NullPointerException e) {
                                        }
                                        if (classField.getClass().getName().contains("util") ||
                                                classField.getClass().getName().contains("lang")) {

                                            if (classField.getClass().getTypeName().contains(ArrayList.class.getTypeName())) {
                                                tableIn.addColumnCollection(pane.getPrefWidth(), pane.getPrefHeight(), Collections.singleton(classField));
                                                tableIn.addItemsCollections((ArrayList) classField);
                                            }
                                            if (classField.getClass().getTypeName().contains(LinkedList.class.getTypeName())) {
                                                tableIn.addColumnCollection(pane.getPrefWidth(), pane.getPrefHeight(), Collections.singleton(classField));
                                                tableIn.addItemsCollections((LinkedList) classField);
                                            }

                                            if (classField.getClass().getTypeName().contains(LinkedHashMap.class.getTypeName())) {
                                                tableIn.addColumnCollection(pane.getPrefWidth(), pane.getPrefHeight(), (LinkedHashMap) classField, columnField[0]);
                                                tableIn.addItemsCollections((LinkedHashMap) classField);
                                            }
                                            if (classField.getClass().getTypeName().contains(HashMap.class.getTypeName())) {
                                                tableIn.addColumnCollection(pane.getPrefWidth(), pane.getPrefHeight(), (LinkedHashMap) classField, columnField[0]);
                                                tableIn.addItemsCollections((HashMap) classField);
                                            }

                                        } else {
                                            if (field.get(object[0]) != null)
                                                tableIn.refresh(pane.getPrefWidth(), pane.getPrefHeight(), field.get(object[0]), field);
                                        }
                                    }

                                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {

                                pane.setVisible(false);
                                pane.getChildren().clear();
                                tableIn = null;
                            }
                        });

                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(button);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };
        buttonCol.setCellFactory(cellFactory);
        return buttonCol;
    }

    private void addColumnCollection(double weight, double height, Collection collections) {

        table.setVisible(true);
        table.getColumns().clear();
        table.getItems().clear();

        table.setPrefWidth(weight);
        table.setPrefHeight(height);

        table.getColumns().add(createColumn("key", 0));
        table.getColumns().get(0).setEditable(false);
        table.getColumns().add(createColumn("value", 1));
    }

    private void addColumnCollection(double weight, double height, LinkedHashMap hashMap, Field field) throws IllegalAccessException {

        table.setVisible(true);

        table.getColumns().clear();
        table.getItems().clear();

        table.setPrefWidth(weight);
        table.setPrefHeight(height);

        String type = hashMap.keySet().toArray()[0].getClass().getTypeName();

        if (type.equals(String.class.getName()) ^ type.equals(Integer.class.getName())) {
            TableColumn tableColumn = createColumn("key", 0);
            tableColumn.setText("key");
            tableColumn.setId(field.getDeclaringClass().toString().replace("class ", "").trim());
            table.getColumns().add(tableColumn);
        } else {
            String name = "key";

            table.getColumns().add(
                    createObjectColumn(hashMap, field, name, 0));
        }

        type = hashMap.get(hashMap.keySet().toArray()[0]).getClass().getTypeName();

        if (type.equals(String.class.getName()) ^ type.equals(Integer.class.getName())) {
            TableColumn tableColumn1 = createColumn("value", 1);
            tableColumn1.setText("value");
            tableColumn1.setId(String.valueOf(hashMap.getClass()));
            table.getColumns().add(tableColumn1);
        } else {
            String name = "value";
            table.getColumns().add(
                    createObjectColumn(hashMap, field, name, 1));
        }
    }

    private void addItemsCollections(ArrayList arrayList) {

        for (int i = 0; i < arrayList.size(); i++) {
            ObservableList<StringProperty> data = FXCollections.observableArrayList();
            data.addAll(new SimpleStringProperty(String.valueOf(i)),
                    new SimpleStringProperty(String.valueOf(arrayList.get(i))));
            table.getItems().add(data);

        }
    }

    private void addItemsCollections(LinkedList linkedList) {

        for (int i = 0; i < linkedList.size(); i++) {
            ObservableList<StringProperty> data = FXCollections.observableArrayList();
            data.addAll(new SimpleStringProperty(String.valueOf(i)),
                    new SimpleStringProperty(String.valueOf(linkedList.get(i))));
            table.getItems().add(data);

        }
    }

    private void addItemsCollections(HashMap hashMap) {

        String keyType = hashMap.keySet().toArray()[0].getClass().getTypeName();
        String valueType = hashMap.get(hashMap.keySet().toArray()[0]).getClass().getTypeName();

        for (int i = 0; i < hashMap.size(); i++) {
            ObservableList<StringProperty> data = FXCollections.observableArrayList();

            if (keyType.equals(String.class.getName()) ^ keyType.equals(Integer.class.getName()))
                data.add(new SimpleStringProperty(String.valueOf(hashMap.keySet().toArray()[i])));
            else data.add(new SimpleStringProperty("0"));

            if (valueType.equals(String.class.getName()) ^ valueType.equals(Integer.class.getName())) {
                data.add(new SimpleStringProperty(String.valueOf(hashMap.get(hashMap.keySet().toArray()[i]))));
            } else data.add(new SimpleStringProperty("0"));

            table.getItems().add(data);
        }

    }

    private void addItemsCollections(LinkedHashMap linkedHashMap) {

        String keyType = linkedHashMap.keySet().toArray()[0].getClass().getTypeName();
        String valueType = linkedHashMap.get(linkedHashMap.keySet().toArray()[0]).getClass().getTypeName();


        for (int i = 0; i < linkedHashMap.size(); i++) {
            ObservableList<StringProperty> data = FXCollections.observableArrayList();

            if (keyType.equals(String.class.getName()) ^ keyType.equals(Integer.class.getName()))
                data.add(new SimpleStringProperty(String.valueOf(linkedHashMap.keySet().toArray()[i])));
            else data.add(new SimpleStringProperty("0"));

            if (valueType.equals(String.class.getName()) ^ valueType.equals(Integer.class.getName())) {
                data.add(new SimpleStringProperty(String.valueOf(linkedHashMap.get(linkedHashMap.keySet().toArray()[i]))));
            } else data.add(new SimpleStringProperty("0"));

            table.getItems().add(data);
        }

    }

    void addItemsArray(Object object) {

        for (int i = 0; i < Array.getLength(object); i++) {

            ObservableList<StringProperty> data = FXCollections.observableArrayList();
            data.addAll(new SimpleStringProperty(String.valueOf(i)), new SimpleStringProperty(String.valueOf(Array.get(object, i))));
            table.getItems().add(data);

        }

    }

    void setWeTryAddGadget(boolean value) {
        this.weTryAddGadget = value;
    }


    private TableColumn createColumn(Field field, int columnIndex) {

        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>(field.getName());
        column.setId(String.valueOf(field.getDeclaringClass()));
        column.setId(field.getDeclaringClass().getName());

        column.setCellValueFactory(cellDataFeatures -> {
            ObservableList<StringProperty> values = cellDataFeatures.getValue();
            if (columnIndex >= values.size()) {
                return new SimpleStringProperty("");
            } else {
                return cellDataFeatures.getValue().get(columnIndex);
            }
        });

        column.setCellFactory(TextFieldTableCell.forTableColumn());

        // editCell( column);
        editCell1(column);

        return column;
    }

    private TableColumn createColumn(String name, int columnIndex) {

        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>(name);
        column.setId("");

        column.setCellValueFactory(cellDataFeatures -> {
            ObservableList<StringProperty> values = cellDataFeatures.getValue();
            if (columnIndex >= values.size()) {
                return new SimpleStringProperty("");
            } else {
                return cellDataFeatures.getValue().get(columnIndex);
            }
        });

        column.setCellFactory(TextFieldTableCell.forTableColumn());

        //  editCell( column);
        editCell1(column);

        return column;
    }

    private void createButtonColumn(TableView table, String columnName, String buttonName) {

        TableColumn buttonCol = new TableColumn(columnName);
        buttonCol.setCellValueFactory(new PropertyValueFactory<>(""));

        Callback<TableColumn<Value, String>, TableCell<Value, String>> cellFactory = new Callback<>() {

            @Override
            public TableCell call(final TableColumn<Value, String> param) {

                final Button button = new Button(buttonName);
                final TableCell<Value, String> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(button);
                            setText(null);
                        }

                        button.setOnMouseClicked(mouseEvent -> {

                            int itemIndex = getIndex();

                            ObservableList<TableColumn> columns = table.getColumns();

                            for (int i = 0; i < columns.size() - 1; i++) {
                                if (columns.get(i).getText().equals(rangeIdentification)) {

                                    ObservableList neededItem = (ObservableList) table.getItems().get(itemIndex);
                                    StringProperty neededCell = (StringProperty) neededItem.get(i);

                                    shopData.sellComputer(selectedType, neededCell.getValue());

                                    table.getColumns().clear();
                                    table.getItems().clear();

                                    try {
                                        createMainTableColumns();
                                        createMainTableItems(table);
                                    } catch (ClassNotFoundException | IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                }
                            }

                        });
                    }
                };
                return cell;
            }
        };

        buttonCol.setCellFactory(cellFactory);
        table.getColumns().add(buttonCol);

    }

    private ObservableList createItem(Field object) throws IllegalAccessException, ClassNotFoundException {

        ObservableList<StringProperty> data = FXCollections.observableArrayList();
        Class aClass = Class.forName(object.getType().toString().replaceFirst("class", "").trim());


        {
            new Object() {
                {
                }

                void addItem(Class aClass) throws IllegalAccessException {
                    if (!aClass.getSimpleName().equals(Object.class.getSimpleName())) {
                        addItem(aClass.getSuperclass());
                    } else return;

                    Field[] fields = aClass.getDeclaredFields();

                    for (Field field : fields) {
                        field.setAccessible(true);

                        if (field.getType().isPrimitive() ^ (field.getType().getName().equals(String.class.getName()))) {

                            if (Modifier.isProtected(field.getModifiers()) ^ Modifier.isPublic(field.getModifiers()) ^
                                    Modifier.isFinal(field.getModifiers())) {
                                data.add(new SimpleStringProperty(field.get(object).toString()));
                            } else if (aClass.getName().equals(object.getName()))
                                if (Modifier.isPrivate(field.getModifiers()) ^ Modifier.isFinal(field.getModifiers()))
                                    data.add(new SimpleStringProperty(field.get(object).toString()));
                        } else {
                            if (field.getType().getName().contains("collections"))
                                data.add(new SimpleStringProperty(collectionInfo(Collections.singleton(field.get(object)))));
                            else data.add(new SimpleStringProperty("object"));
                        }
                    }
                }
            }.addItem(aClass);
        }
        return data;
    }

    private ObservableList createItem(Object list) throws IllegalAccessException {

        ObservableList<StringProperty> data = FXCollections.observableArrayList();

        if (list == null) {
            data.add(new SimpleStringProperty("null"));
            return data;
        }

        Class aClass = list.getClass();
        {
            new Object() {
                {
                }

                void addItem(Class aClass) throws IllegalAccessException {

                    if (!aClass.getSimpleName().equals(Object.class.getSimpleName())) addItem(aClass.getSuperclass());
                    else return;

                    Field[] fields = aClass.getDeclaredFields();

                    for (Field field : fields) {

                        field.setAccessible(true);

                        if (field.getType().isPrimitive() ^ (field.getType().getName().equals(String.class.getName()))) {

                            if (Modifier.isProtected(field.getModifiers()) ^ Modifier.isPublic(field.getModifiers()) ^
                                    Modifier.isFinal(field.getModifiers())) {
                                data.add(new SimpleStringProperty(field.get(list).toString()));
                            } else if (aClass.getName().equals(list.getClass().getName()))
                                if (Modifier.isPrivate(field.getModifiers()) ^ Modifier.isFinal(field.getModifiers()))
                                    data.add(new SimpleStringProperty(field.get(list).toString()));

                        } else {
                            if (field.getType().getName().contains(".util") ||
                                    field.getType().getName().contains("AbstractCollection")) {
                                data.add(new SimpleStringProperty(collectionInfo(Collections.singleton(field.get(list)))));
                            } else data.add(new SimpleStringProperty("have not aceses"));
                        }
                    }
                }
            }.addItem(aClass);
        }
        return data;
    }

    private void createMainTableItems(TableView table) throws IllegalAccessException {

        ObservableList<StringProperty> data;
        Object[] list = shopData.getTypedEquipment(selectedType).keySet().toArray();

        for (int j = 0; j < list.length; j++) {
            data = createItem(list[j]);

            ObservableList<TableColumn> columns = table.getColumns();

            for (int i = 0; i < table.getColumns().size() - 1; i++) {
                if (columns.get(i).getText().equals(rangeIdentification)) {
                    StringProperty neededCell = data.get(i);

                    data.add(new SimpleStringProperty(String.valueOf(
                            shopData.getGadgetCount(selectedType, neededCell.getValue()))));
                    break;
                }
            }
            table.getItems().add(data);
        }

    }

    private String collectionInfo(Collection collections) {
        if (collections.isEmpty()) return "empty";

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < collections.toArray().length; i++) str.append(collections.toArray()[i] + "\n");

        return String.valueOf(str);
    }

    private Object getGadget(String selectedType, String gadgetName) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Class[] paramTypes = new Class[]{String.class, String.class};
        Method findComputer = shopData.getClass().getDeclaredMethod("getGadget", paramTypes);
        findComputer.setAccessible(true);
        return findComputer.invoke(shopData, selectedType, gadgetName);
    }

    private void generateAlert(Alert.AlertType type, String text) {
        Alert alert = new Alert(type, text);
        alert.show();

        try {
            Method add = shopData.getClass().getDeclaredMethod("sellComputer", String.class, String.class);
            add.setAccessible(true);
            weTryAddGadget = false;
            add.invoke(shopData, selectedType, "New Gadget");
            table.getColumns().clear();
            table.getItems().clear();
            createMainTableColumns();
            createMainTableItems(table);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void editCell1(TableColumn<ObservableList<StringProperty>, String> column) {

        column.setOnEditStart(editStart -> {
            if (table.equals(rootTable)) rootIndex = editStart.getTablePosition().getRow();
            if (weTryAddGadget &&
                    !rootTable.getColumns().get(rangeIdentifierColumnIndex).getCellData(rootIndex).equals("New Gadget")) {
                generateAlert(Alert.AlertType.ERROR, "Error");
            }
        });

        column.setOnEditCommit(cellEditEvent -> {

            String oldValue = cellEditEvent.getOldValue();
            int cellIndex = cellEditEvent.getTablePosition().getRow(), columnIndex = cellEditEvent.getTablePosition().getColumn();

            try {
                Object gadget = getGadget(selectedType, rootTable.getItems().get(rootIndex).
                        get(rangeIdentifierColumnIndex).getValueSafe());

                Object[] object = {gadget, gadget};
                final Field[] columnField = {null};
                final boolean[] find = {false};

                if (cellEditEvent.getTableColumn().getText().equals(columnCountName)) {
                    shopData.setGadgetCount(selectedType, rootTable.getItems().get(rootIndex).
                            get(rangeIdentifierColumnIndex).getValueSafe(), Integer.valueOf(cellEditEvent.getNewValue()));

                    ObservableList oList = cellEditEvent.getTableView().getItems().get(cellEditEvent.getTablePosition().getRow());
                    oList.set(cellEditEvent.getTablePosition().getColumn(),
                            new SimpleStringProperty(String.valueOf(cellEditEvent.getNewValue())));
                    cellEditEvent.getTableView().getItems().set(cellEditEvent.getTablePosition().getRow(),oList);

                }

                new Object() {
                    {
                    }

                    void getSuperClass(Class aClass, Object ourObject) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
                        if (!aClass.getSimpleName().equals(Object.class.getSimpleName())) {
                            getSuperClass(aClass.getSuperclass(), ourObject);
                        } else return;

                        if (find[0]) return;

                        for (Field field : aClass.getDeclaredFields()) {

                            if (find[0]) return;

                            field.setAccessible(true); //java\.(util|lang)\.

                            if (field.getType().getName().contains("util") ^ field.getName().contains("lang")) {

                                if (cellEditEvent.getTableView().getId().equals(field.getName()) &&
                                        field.getName().equals(cellEditEvent.getTableView().getId())) {

                                    columnField[0] = field;
                                    object[1] = ourObject;

                                    if (field.getType().getName().replaceFirst("class", "").
                                            trim().equals(ArrayList.class.getTypeName())) {

                                        ArrayList arrayList = (ArrayList) field.get(ourObject);

                                        Class clazz = Class.forName(arrayList.get(cellIndex).getClass().getName());

                                        Method valueOf;
                                        try {
                                            valueOf = clazz.getDeclaredMethod("valueOf", Object.class);
                                        } catch (NoSuchMethodException e) {
                                            valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                                        }
                                        valueOf.setAccessible(true);

                                        arrayList.remove(cellEditEvent.getTablePosition().getRow());
                                        arrayList.add(cellEditEvent.getTablePosition().getRow(), valueOf.invoke(clazz, cellEditEvent.getNewValue()));
                                        field.set(ourObject, arrayList);

                                    }

                                    if (field.getType().getName().replaceFirst("class", "").
                                            trim().equals(LinkedList.class.getTypeName())) {

                                        LinkedList arrayList = (LinkedList) field.get(ourObject);
                                        Class clazz = Class.forName(arrayList.get(cellIndex).getClass().getName());

                                        Method valueOf;
                                        try {
                                            valueOf = clazz.getDeclaredMethod("valueOf", Object.class);
                                        } catch (NoSuchMethodException e) {
                                            valueOf = clazz.getDeclaredMethod("valueOf", String.class);
                                        }
                                        valueOf.setAccessible(true);

                                        arrayList.remove(cellEditEvent.getTablePosition().getRow());
                                        arrayList.add(cellEditEvent.getTablePosition().getRow(), valueOf.invoke(clazz, cellEditEvent.getNewValue()));

                                        field.set(ourObject, arrayList);
                                    }

                                    if (field.getType().getName().replaceFirst("class", "").
                                            trim().equals(HashMap.class.getTypeName())) {

                                        HashMap hashMap = (HashMap) field.get(ourObject);
                                        Class clazzKey, clazzValue;

                                        clazzValue = Class.forName((hashMap.keySet().toArray()[cellIndex]).getClass().getName());
                                        clazzKey = Class.forName(hashMap.get(hashMap.keySet().toArray()[cellIndex]).getClass().getName());


                                        Method valueOf;
                                        try {
                                            valueOf = clazzValue.getDeclaredMethod("valueOf", Object.class);
                                        } catch (NoSuchMethodException e) {
                                            valueOf = clazzValue.getDeclaredMethod("valueOf", String.class);
                                        }
                                        valueOf.setAccessible(true);


                                        if (columnIndex == 0) {

                                            Method valueOfKey;
                                            try {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", Object.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", String.class);
                                            }
                                            valueOfKey.setAccessible(true);

                                            hashMap.remove(hashMap.keySet().toArray()[columnIndex]);
                                            hashMap.put(valueOfKey.invoke(clazzKey, cellEditEvent.getNewValue()),
                                                    valueOf.invoke(clazzValue, cellEditEvent.getTableView().getColumns().get(1).getCellData(cellIndex)));
                                        } else
                                            hashMap.replace(hashMap.keySet().toArray()[cellIndex], valueOf.invoke(clazzValue, cellEditEvent.getNewValue()));

                                        field.set(ourObject, hashMap);
                                    }

                                    if (field.getType().getName().replaceFirst("class", "").
                                            trim().equals(LinkedHashMap.class.getTypeName())) {

                                        LinkedHashMap hashMap = (LinkedHashMap) field.get(ourObject);
                                        Class clazzKey, clazzValue;

                                        clazzValue = Class.forName((hashMap.keySet().toArray()[cellIndex]).getClass().getName());
                                        clazzKey = Class.forName(hashMap.get(hashMap.keySet().toArray()[cellIndex]).getClass().getName());


                                        Method valueOf;
                                        try {
                                            valueOf = clazzValue.getDeclaredMethod("valueOf", Object.class);
                                        } catch (NoSuchMethodException e) {
                                            valueOf = clazzValue.getDeclaredMethod("valueOf", String.class);
                                        }
                                        valueOf.setAccessible(true);


                                        if (columnIndex == 0) {

                                            Method valueOfKey;
                                            try {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", Object.class);
                                            } catch (NoSuchMethodException e) {
                                                valueOfKey = clazzKey.getDeclaredMethod("valueOf", String.class);
                                            }
                                            valueOfKey.setAccessible(true);

                                            hashMap.remove(hashMap.keySet().toArray()[columnIndex]);
                                            hashMap.put(valueOfKey.invoke(clazzKey, cellEditEvent.getNewValue()),
                                                    valueOf.invoke(clazzValue, cellEditEvent.getTableView().getColumns().get(1).getCellData(cellIndex)));
                                        } else
                                            hashMap.replace(hashMap.keySet().toArray()[cellIndex], valueOf.invoke(clazzValue, cellEditEvent.getNewValue()));

                                        field.set(ourObject, hashMap);
                                    }
                                    find[0] = true;
                                    return;
                                } else continue;
                            }

                            if (cellEditEvent.getTableColumn().getId().equals(field.getDeclaringClass().toString().replaceFirst("class", "").trim()) &&
                                    field.getName().equals(cellEditEvent.getTableColumn().getText())) {

                                ObservableList oList = cellEditEvent.getTableView().
                                        getItems().get(cellEditEvent.getTablePosition().getRow());
                                oList.set(cellEditEvent.getTablePosition().getColumn(),
                                        new SimpleStringProperty(String.valueOf(cellEditEvent.getNewValue())));
                                table.refresh();
                                rootTable.refresh();

                                switch (field.getType().getSimpleName()) {
                                    case "Integer":
                                    case "int": {
                                        field.setInt(ourObject,
                                                Integer.parseInt(cellEditEvent.getNewValue()));
                                        break;
                                    }
                                    case "String": {
                                        field.set(ourObject,
                                                cellEditEvent.getNewValue());
                                        break;
                                    }
                                    case "double":
                                    case "Double": {
                                        field.setDouble(ourObject,
                                                Double.parseDouble(cellEditEvent.getNewValue()));
                                        break;
                                    }
                                    case "boolean":
                                    case "Boolean": {
                                        boolean b = Boolean.parseBoolean(cellEditEvent.getNewValue());
                                        oList = cellEditEvent.getTableView().
                                                getItems().get(cellEditEvent.getTablePosition().getRow());
                                        oList.set(cellEditEvent.getTablePosition().getColumn(),
                                                new SimpleStringProperty(String.valueOf(b)));
                                        field.setBoolean(ourObject, b);
                                        table.refresh();
                                        rootTable.refresh();
                                        break;
                                    }
                                    case "byte":
                                    case "Byte": {
                                        field.setByte(ourObject,
                                                Byte.parseByte(cellEditEvent.getNewValue()));
                                        break;
                                    }
                                    case "float":
                                    case "Float": {
                                        field.setFloat(ourObject,
                                                Float.parseFloat(cellEditEvent.getNewValue()));
                                        break;
                                    }
                                    case "short":
                                    case "Short": {
                                        field.setShort(ourObject,
                                                Short.parseShort(cellEditEvent.getNewValue()));
                                        break;
                                    }
                                    case "long":
                                    case "Long": {
                                        field.setLong(ourObject,
                                                Long.parseLong(cellEditEvent.getNewValue()));
                                        break;
                                    }
                                    default: {
                                        field.set(ourObject, (Object) cellEditEvent.getNewValue());
                                    }
                                }

                                object[0] = ourObject;
                                columnField[0] = field;
                                find[0] = true;
                                return;
                            } else {
                                if (field.get(ourObject) != null) {
                                    if (!field.getType().isPrimitive() &&
                                            !isShellClasses(field.getType().toString().replaceFirst("class", "").trim()))
                                        getSuperClass(field.get(ourObject).getClass(), field.get(ourObject));
                                }
                            }
                        }
                    }
                }.getSuperClass(gadget.getClass(), gadget);

                if (weTryAddGadget && find[0]) {
                    for (int j = 0; j < cellEditEvent.getTableView().getColumns().size(); j++)
                        cellEditEvent.getTableView().getColumns().get(j).setOnEditStart(e -> {
                        });
                    weTryAddGadget = false;
                }

            } catch (Exception e) {
                ObservableList oList = cellEditEvent.getTableView().
                        getItems().get(cellEditEvent.getTablePosition().getRow());
                oList.set(cellEditEvent.getTablePosition().getColumn(),
                        new SimpleStringProperty(oldValue));
                table.refresh();
                rootTable.refresh();

                Alert alert = new Alert(Alert.AlertType.ERROR, "Введены некорректные данные");
                alert.show();
            }
        });

    }

    void addNewGadget() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException {

        Method add = shopData.getClass().getDeclaredMethod("buyComputer", PersonalComputer.class, String.class);
        add.setAccessible(true);
        Class aClass = Class.forName(selectedType);
        add.invoke(shopData, aClass.getConstructor().newInstance(), selectedType);
        weTryAddGadget = true;

    }

    void selectItem() {

        int i = 0;
        for (; i < table.getItems().size(); i++) {
            if (table.getItems().get(i).get(rangeIdentifierColumnIndex).getValue().equals("New Gadget")) break;
        }

        table.edit(i, table.getColumns().get(rangeIdentifierColumnIndex));

    }

    O getShopData() {
        return shopData;
    }

    void clearTable() {
        table.getColumns().clear();
        table.getItems().clear();
    }


}


