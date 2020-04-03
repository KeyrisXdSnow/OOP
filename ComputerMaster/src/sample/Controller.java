package sample;

import computer.architecture.comType.portable.Laptop;
import computer.architecture.comType.portable.Smartphones;
import computer.architecture.comType.portable.Tablet;
import computer.architecture.comType.stationary.StationaryComputer;
import computer.specifications.Specifications;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shop.Shop;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Controller {

    @FXML
    private Button equipmentType;

    @FXML
    private TextField textFiledPutType;

    @FXML
    private AnchorPane Top;

    @FXML
    private Button addItem,addType;;

    private Pane root ;


    private Shop shop = new Shop("Мой самык классный компьютерный магазин", 10000);
    private String selectedType;
    private Table table ;


    @FXML
    private AnchorPane tableContainer;

    @FXML
    void initialize() {
        initShop();
        initializeTable();
        initMenu();
    }

    private void initializeTable() {
        table = new Table(tableContainer,shop,null); // сделать super на контструкторы done


    }

    private void initShop() {

        ArrayList<String> programs = new ArrayList<>();
        programs.add("Bpwin"); programs.add("VisualStudio"); programs.add("Paint");
        programs.add("Microsoft Office");



        shop.buyComputer(new Smartphones("Xiaomi Mi8", new Specifications("Snapdragon 560","exynos960",
                "Android","0.6","Xiaomi","red"),1000,programs ));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapragon 520","exynos910",
                "Android","0.9","Google","black"),1000,programs ));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520","exynos910",
                "Android","0.9","Google","black"),1000,programs ));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520","exynos910",
                "Android","0.9","Google","black"),1000,programs ));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520","exynos910",
                "Android","0.9","Google","black"),1000,programs ));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520","exynos910",
                "Android","0.9","Google","black"),1000,programs ));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520","exynos910",
                "Android","0.9","Google","black"),1000,programs ));
        shop.buyComputer(new Smartphones("Pixel 4", new Specifications("Snapdragon 520","exynos910",
                "Android","0.9","Google","black"),1000,programs ));

        shop.buyComputer(new Smartphones("Huawei A40", new Specifications("Snapdragon 590","exynos960",
                "Android","0.2","Huawei","black"),800,programs ));

        shop.buyComputer(new Laptop("Lenovo Legion Y530", new Specifications("Intel i5","exynos960",
                "Windows","2.5","Lenovo","silver"),1500,programs ));

        shop.buyComputer(new Laptop("Asus A34K", new Specifications("Intel i5","exynos960",
                "Windows","2.5","Asus","Black"),1200,programs ));

        shop.buyComputer(new Laptop("Asus A723K", new Specifications("Intel i9","exynos960",
                "Windows","2.5","Asus","silver"),900,programs ));

        shop.buyComputer(new Laptop("Xiaomi Air", new Specifications("Intel i5","exynos960",
                "Windows","1.3","Lenovo","silver"),800,programs ));



    }

    @FXML
    void addItem () {
        addItem.setOnMouseClicked(mouseEvent -> {
         try {
                table.addNewGadget();
                table.refresh(tableContainer,selectedType);
                table.selectItem("");

            } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void buttonClicked() {

        equipmentType.setOnKeyPressed( key -> {
            if (key.getCode() == KeyCode.RIGHT_PARENTHESIS) swapTechType(1);
            if (key.getCode() == KeyCode.LEFT_PARENTHESIS) swapTechType (-1);
        });

        equipmentType.setOnMouseClicked( mouseEvent -> {

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
    private void AddType () {

        root.setVisible(true);

    }

    private  void initMenu () {

        MyTreeTable tree = new MyTreeTable(shop) ;
        root = new Pane(tree.getTree());
        root.setPrefSize(0,0);
        Top.getChildren().add(root);
        root.setVisible(false);

        root.setOnMouseExited( mouseDragEvent -> {
            root.setVisible(false);
        });


    }

    @FXML
    private void swapTechType(int step) {
        List techTypes = new ArrayList(shop.getTechniqueTypes());
        if (!techTypes.isEmpty()) {
            if (techTypes.indexOf(selectedType) >= 0)
                selectedType = String.valueOf
                        (techTypes.get((techTypes.indexOf(selectedType) + step) % techTypes.size()));
            else selectedType = (String.valueOf(techTypes.get(0)));
            String[] classes = selectedType.split("\\.");
            equipmentType.setText(classes[classes.length - 1]);

        } else  {
            selectedType = "empty";
            equipmentType.setText(selectedType);
            table.clearTable();
        }

    }



}