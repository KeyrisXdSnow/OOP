package sample;

import classLoader.MyClassLoader;
import computer.architecture.comType.PersonalComputer;
import computer.architecture.comType.portable.Laptop;
import computer.architecture.comType.portable.Smartphones;
import computer.specifications.Specifications;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import serilization.binary.BinarySerializer;
import serilization.json.JsonSerializer;
import serilization.Serializer;
import serilization.text.TextSerializer;
import shop.Shop;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class Controller {

    @FXML
    private Button equipmentType;
    @FXML
    private AnchorPane Top, tableContainer;
    @FXML
    private Button addItem;
    @FXML
    private Menu archivers;

    private Pane root;
    private Serializer<PersonalComputer> serializer;
    private Shop shop = new Shop("Мой самык классный компьютерный магазин", 10000);
    private String selectedType;
    private Table table;
    private FileChooser fileChooser;
    private final ArrayList<String> binaryFiles = new ArrayList<>(), jsonFiles = new ArrayList<>(),
            textFiles = new ArrayList<>(), allFiles = new ArrayList<>();
    private String pluginsDirectory = "D:\\Labs\\Semester4\\OOP\\Lab2\\ComputerMaster\\src\\plugins" ;

    @FXML
    void initialize() {
        initShop();
        initializeTable();
        initMenu();
        initFileChooser();
    }

    private void initFileChooser() {

        binaryFiles.add("*.dat");
        jsonFiles.add("*.json");
        textFiles.add("*.txt");
        textFiles.add("*.csv");
        allFiles.addAll(binaryFiles);
        allFiles.addAll(jsonFiles);
        allFiles.addAll(textFiles);


        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Binary Files", binaryFiles),
                new FileChooser.ExtensionFilter("JSON Files", jsonFiles),
                new FileChooser.ExtensionFilter("Text Files", textFiles),
                new FileChooser.ExtensionFilter("All Files", allFiles));
    }

    private void initializeTable() {
        table = new Table(tableContainer, shop, null); // сделать super на контструкторы done
    }

    private void initShop() {

        ArrayList<String> programs = new ArrayList<>();
        programs.add("Bpwin");
        programs.add("VisualStudio");
        programs.add("Paint");
        programs.add("Microsoft Office");

        shop.buyComputer(new Smartphones("Xiaomi Mi8", new Specifications("Snapdragon 560", "exynos960",
                "Android", "0.6", "Xiaomi", "red"), 1000, (ArrayList<String>) programs.clone()));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapragon 520", "exynos910",
                "Android", "0.9", "Google", "black"), 1000, (ArrayList<String>) programs.clone()));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520", "exynos910",
                "Android", "0.9", "Google", "black"), 1000, (ArrayList<String>) programs.clone()));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520", "exynos910",
                "Android", "0.9", "Google", "black"), 1000, (ArrayList<String>) programs.clone()));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520", "exynos910",
                "Android", "0.9", "Google", "black"), 1000, (ArrayList<String>) programs.clone()));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520", "exynos910",
                "Android", "0.9", "Google", "black"), 1000, (ArrayList<String>) programs.clone()));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520", "exynos910",
                "Android", "0.9", "Google", "black"), 1000, (ArrayList<String>) programs.clone()));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520", "exynos910",
                "Android", "0.9", "Google", "black"), 1000, (ArrayList<String>) programs.clone()));

        shop.buyComputer(new Smartphones("Huawei A40", new Specifications("Snapdragon 590", "exynos960",
                "Android", "0.2", "Huawei", "black"), 800, (ArrayList<String>) programs.clone()));

        shop.buyComputer(new Laptop("Lenovo Legion Y530", new Specifications("Intel i5", "exynos960",
                "Windows", "2.5", "Lenovo", "silver"), 1500, (ArrayList<String>) programs.clone()));

        shop.buyComputer(new Laptop("Asus A34K", new Specifications("Intel i5", "exynos960",
                "Windows", "2.5", "Asus", "Black"), 1200, (ArrayList<String>) programs.clone()));

        shop.buyComputer(new Laptop("Asus A723K", new Specifications("Intel i9", "exynos960",
                "Windows", "2.5", "Asus", "silver"), 900, (ArrayList<String>) programs.clone()));

        shop.buyComputer(new Laptop("Xiaomi Air", new Specifications("Intel i5", "exynos960",
                "Windows", "1.3", "Lenovo", "silver"), 800, (ArrayList<String>) programs.clone()));


    }

    @FXML
    void addItem() {
        addItem.setOnMouseClicked(mouseEvent -> {
            try {
                table.addNewGadget();
                table.refresh(tableContainer, selectedType);
                table.selectItem();
            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void buttonClicked() {

        equipmentType.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.RIGHT_PARENTHESIS) swapTechType(1);
            if (key.getCode() == KeyCode.LEFT_PARENTHESIS) swapTechType(-1);
        });

        equipmentType.setOnMouseClicked(mouseEvent -> {

            swapTechType(+1);

            try {
                if (!selectedType.equals("empty")) {
                    table.refresh(tableContainer, selectedType);
                    shop = table.getShopData();
                } else {

                }

            } catch (ClassNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }

        });

    }

    @FXML
    private void AddType() {

        root.setVisible(true);

    }

    private void initMenu() {

        MyTreeTable tree = new MyTreeTable(shop);
        root = new Pane(tree.getTree());
        root.setPrefSize(0, 0);
        Top.getChildren().add(root);
        root.setVisible(false);

        root.setOnMouseExited(mouseDragEvent -> {
            root.setVisible(false);
        });


    }

    @FXML
    private void swapTechType(int step) {
        List techTypes = new ArrayList(shop.getTechniqueTypes());
        if (!techTypes.isEmpty()) {
            if (techTypes.contains(selectedType))
                selectedType = String.valueOf
                        (techTypes.get((techTypes.indexOf(selectedType) + step) % techTypes.size()));
            else selectedType = (String.valueOf(techTypes.get(0)));
            String[] classes = selectedType.split("\\.");
            equipmentType.setText(classes[classes.length - 1]);

        } else {
            selectedType = "empty";
            equipmentType.setText(selectedType);
            table.clearTable();
        }

    }

    @FXML
    void menuHidden () {
        archivers.getItems().clear();
       // archivers = new Menu();
        System.runFinalization();
        System.gc();
        Runtime.getRuntime().gc();
    }
    @FXML
    private void saveFile() {
        FileChooser.ExtensionFilter filterAll = fileChooser.getExtensionFilters().get(fileChooser.getExtensionFilters().size() - 1);
        fileChooser.getExtensionFilters().remove(filterAll);
        Alert alert ;
        try {
            File file = serializeFile(fileChooser.showSaveDialog(new Stage()));
            if (file == null) return;
            alert = new Alert(Alert.AlertType.CONFIRMATION,"Данные сохранены успешно");
        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка\n" + e.getMessage());
        } finally {
            fileChooser.getExtensionFilters().add(filterAll);
        }
        alert.show();
    }
    @FXML
    private void loadFile ()  {
        Alert alert ;
        try {
            File file = deserializeFile(fileChooser.showOpenDialog(new Stage()));
            if (file != null) {
                table.clearTable();
                alert = new Alert(Alert.AlertType.CONFIRMATION,"Данные загружены успешно");
                swapTechType(0);
                table.refresh(tableContainer, selectedType);
            } else return ;
        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка\n" + e.getMessage());
        }
        alert.show();

    }

    private File serializeFile(File file) throws Exception {

        if (file != null) {

            String path = file.getAbsolutePath();
            String ext = "*" + path.substring(path.lastIndexOf("."));

            if (binaryFiles.contains(ext)) {
                serializer = new BinarySerializer<>();
            } else if (jsonFiles.contains(ext)) {
                serializer = new JsonSerializer<>();
            } else if (textFiles.contains(ext)) {
                serializer = new TextSerializer<>();
            }

            serializer.serialize(shop.getRange(), file);
        }
        return file;
    }

    private File deserializeFile(File file) throws Exception {

        if (file != null) {
            String path = file.getAbsolutePath().toString();
            String ext = "*" + path.substring(path.lastIndexOf("."));

            if (binaryFiles.contains(ext)) {
                serializer = new BinarySerializer<>();
            } else if (jsonFiles.contains(ext)) {
                serializer = new JsonSerializer<>();
            } else if (textFiles.contains(ext)) {
                serializer = new TextSerializer<>();
            }
            shop.setRange(serializer.deserialize(file));
        }

        return file;
    }


    @FXML
    void downloadDependenciesZip() throws Exception {
        MyClassLoader classLoader = new MyClassLoader(pluginsDirectory);
        //archivers.getItems().clear();
       // archivers = new Menu();
        classLoader.loadJar();
        ArrayList<String> interfaceMethods = new ArrayList<>();
        for (Class aClass : classLoader.getLoadedClasses()){
            if (aClass.isInterface()){
                for (Method method : aClass.getMethods()) interfaceMethods.add(method.getName());
                break;
            }
        }
        if (interfaceMethods == null ^ interfaceMethods.size() <= 0 ) return;

        for (Class aClass : classLoader.getLoadedClasses()){

            if (aClass.isInterface()) continue;

            ArrayList<String> classMethods = new ArrayList<>();
            for (Method method : aClass.getMethods()) classMethods.add(method.getName());

            if (classMethods.containsAll(interfaceMethods)) {

                Constructor constructor = aClass.getConstructor();
                constructor.setAccessible(true);
                Object classInstance = constructor.newInstance();
                Field fieldName = aClass.getDeclaredField("zipName"),fieldType = aClass.getDeclaredField("zipType");
                fieldName.setAccessible(true); fieldType.setAccessible(true);
                Object archiverName = fieldName.get(classInstance), archiverType = fieldType.get(classInstance);
                MenuItem menuItem = new MenuItem(archiverName.toString());
                menuItem.setOnAction(actionEvent -> {
                        FileChooser.ExtensionFilter filterAll = fileChooser.getExtensionFilters().get(fileChooser.getExtensionFilters().size() - 1);
                        fileChooser.getExtensionFilters().remove(filterAll);
                        Alert alert ;
                        try {
                            File file = serializeFile(fileChooser.showSaveDialog(new Stage()));
                            if (file == null) return;
                            String path = file.getAbsolutePath();
                            String ext = "*" + path.substring(path.lastIndexOf("."));
                            File zipFile = new File(path.replaceFirst(ext.substring(1),archiverType.toString()));
                            Method[] methods = aClass.getMethods();
                            Method method = null ;
                            for (Method m : methods)  if (m.getName().equals("zip")) method = m ;
                            if (method == null ) throw new Exception("Методы для архивации в пакете не найдено");
                            method.setAccessible(true);
                            method.invoke(classInstance, new File[]{zipFile, file});
                            //new ZipArchiver().zip(zipFile,file);
                            alert = new Alert(Alert.AlertType.INFORMATION,"Данные сохранены успешно");
                            file.delete();
                        } catch (Exception e) {
                            alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка\n" + e.getMessage());
                        } finally {
                            fileChooser.getExtensionFilters().add(filterAll);
                        }
                        alert.show();
                });
                archivers.getItems().add(menuItem);
            }
        }
        classLoader.closeClassLoader();
    }


    @FXML
    void downloadDependenciesUnzip() throws Exception {

        MyClassLoader classLoader = new MyClassLoader(pluginsDirectory);
        classLoader.loadJar();
        FileChooser unZipFileChooser = new FileChooser();

        ArrayList<String> interfaceMethods = new ArrayList<>();
        for (Class aClass : classLoader.getLoadedClasses()){
            if (aClass.isInterface()){
                for (Method method : aClass.getMethods()) interfaceMethods.add(method.getName());
                break;
            }
        }
        if (interfaceMethods == null ^ interfaceMethods.size() <= 0 ) return;

        for (Class aClass : classLoader.getLoadedClasses()){

            if (aClass.isInterface()) continue;

            ArrayList<String> classMethods = new ArrayList<>();
            for (Method method : aClass.getMethods()) classMethods.add(method.getName());

            if (classMethods.containsAll(interfaceMethods)) {

                Constructor constructor = aClass.getConstructor();
                constructor.setAccessible(true);
                Object classInstance = constructor.newInstance();
                Field fieldName = aClass.getDeclaredField("zipName"),fieldType = aClass.getDeclaredField("zipType");
                fieldName.setAccessible(true); fieldType.setAccessible(true);
                Object archiverName = fieldName.get(classInstance), archiverType = fieldType.get(classInstance);
                unZipFileChooser.getExtensionFilters().add( new FileChooser.ExtensionFilter(archiverName.toString(),"*"+archiverType.toString()));

            }
        }
        Alert alert ;
        try {
            File zipFile = unZipFileChooser.showOpenDialog(new Stage());
            if (zipFile == null) return;
            String path = zipFile.getAbsolutePath();
            String ext = "*" + path.substring(path.lastIndexOf("."));

            for (Class aClass : classLoader.getLoadedClasses()) {

                if (aClass.isInterface()) continue;

                ArrayList<String> classMethods = new ArrayList<>();
                for (Method method : aClass.getMethods()) classMethods.add(method.getName());

                if (classMethods.containsAll(interfaceMethods)) {

                    Constructor constructor = aClass.getConstructor();
                    constructor.setAccessible(true);
                    Object classInstance = constructor.newInstance();
                    Field fieldType = aClass.getDeclaredField("zipType");
                    fieldType.setAccessible(true);
                    Object archiverType = fieldType.get(classInstance);

                    if (ext.substring(1).equals(archiverType)) {
                        Method[] methods = aClass.getMethods();
                        Method method = null;
                        for (Method m : methods) if (m.getName().equals("unzip")) method = m;
                        if (method == null) throw new Exception("Методы для разархивации в пакете не найдено");
                        method.setAccessible(true);
                        Object filePath = method.invoke(classInstance, zipFile);
                       // String filePath = new ZipArchiver().unzip(zipFile);
                        File file = new File(filePath.toString());
                        deserializeFile(file);
                        swapTechType(0);
                        table.refresh(tableContainer, selectedType);
                        break;

                    }
                }
            }

            alert = new Alert(Alert.AlertType.INFORMATION,"Данные загружены успешно");
        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка\n" + e.getMessage());
        }
        alert.show();

        classLoader.closeClassLoader();
    }

    @FXML
    void unZipAuto() throws Exception {

        MyClassLoader classLoader = new MyClassLoader(pluginsDirectory);
        classLoader.loadJar();
        FileChooser unZipFileChooser = new FileChooser();
        ArrayList<String> loadingTypes = new ArrayList<>(), interfaceMethods = new ArrayList<>();

        for (Class aClass : classLoader.getLoadedClasses()){
            if (aClass.isInterface()){
                for (Method method : aClass.getMethods()) interfaceMethods.add(method.getName());
                break;
            }
        }
        if (interfaceMethods == null ^ interfaceMethods.size() <= 0 ) return;

        for (Class aClass : classLoader.getLoadedClasses()){

            if (aClass.isInterface()) continue;

            ArrayList<String> classMethods = new ArrayList<>();
            for (Method method : aClass.getMethods()) classMethods.add(method.getName());

            if (classMethods.containsAll(interfaceMethods)) {

                Constructor constructor = aClass.getConstructor();
                constructor.setAccessible(true);
                Object classInstance = constructor.newInstance();
                Field fieldType = aClass.getDeclaredField("zipType");
                fieldType.setAccessible(true);
                loadingTypes.add(fieldType.get(classInstance).toString());

            }
        }
        Alert alert ;
        try {
            File zipFile = unZipFileChooser.showOpenDialog(new Stage());
            if (zipFile == null) return;
            String path = zipFile.getAbsolutePath();
            String ext = "*" + path.substring(path.lastIndexOf("."));

            if (!loadingTypes.contains(ext.substring(1))) {
                alert = new Alert(Alert.AlertType.INFORMATION,"Тип архива не поддерживается");
                alert.show();
                return;
            }

            for (Class aClass : classLoader.getLoadedClasses()) {

                if (aClass.isInterface()) continue;

                ArrayList<String> classMethods = new ArrayList<>();
                for (Method method : aClass.getMethods()) classMethods.add(method.getName());

                if (classMethods.containsAll(interfaceMethods)) {

                    Constructor constructor = aClass.getConstructor();
                    constructor.setAccessible(true);
                    Object classInstance = constructor.newInstance();
                    Field fieldType = aClass.getDeclaredField("zipType");
                    fieldType.setAccessible(true);
                    Object archiverType = fieldType.get(classInstance);

                    if (ext.substring(1).equals(archiverType)) {
                        Method[] methods = aClass.getMethods();
                        Method method = null;
                        for (Method m : methods) if (m.getName().equals("unzip")) method = m;
                        if (method == null) throw new Exception("Методы для разархивации в пакете не найдено");
                        method.setAccessible(true);
                        Object filePath = method.invoke(classInstance, zipFile);
                        File file = new File(filePath.toString());
                        deserializeFile(file);
                         swapTechType(0);
                        table.refresh(tableContainer, selectedType);
                        break;

                    }
                }
            }

            alert = new Alert(Alert.AlertType.INFORMATION,"Данные загружены успешно");
        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR, "Произошла ошибка\n" + e.getMessage());
        }
        alert.show();

        classLoader.closeClassLoader();

    }



}