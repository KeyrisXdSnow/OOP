package sample;

import computer.architecture.comType.PersonalComputer;
import computer.architecture.comType.portable.Laptop;
import computer.architecture.comType.portable.PortableComputer;
import computer.architecture.comType.portable.Smartphones;
import computer.architecture.comType.portable.Tablet;
import computer.architecture.comType.stationary.StationaryComputer;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.util.Callback;
import shop.Shop;

import java.util.HashMap;

public class MyTreeTable {

    private TreeView<String> langsTreeView ;
    HashMap<String,String> types ;


    MyTreeTable(Shop data) {

        types = new HashMap<>();
        types.put("Laptop", Laptop.class.getPackageName() + "." + Laptop.class.getSimpleName());
        types.put("Smartphones", Smartphones.class.getPackageName() + "." + Smartphones.class.getSimpleName());
        types.put("Tablet", Tablet.class.getPackageName() + "." + Tablet.class.getSimpleName());
        types.put("PersonalComputer",PersonalComputer.class.getPackageName() + "." + PersonalComputer.class.getSimpleName());
        types.put("PortableComputer",PortableComputer.class.getPackageName() + "." + PortableComputer.class.getSimpleName());
        types.put("StationaryComputer",StationaryComputer.class.getPackageName() + "." + StationaryComputer.class.getSimpleName());

        CheckBoxTreeItem<String> rootTreeNode = new CheckBoxTreeItem<String>("PersonalComputer");
        CheckBoxTreeItem<String> item ;

        CheckBoxTreeItem<String> portable = new CheckBoxTreeItem<>("PortableComputer");
        item = new CheckBoxTreeItem<>("Laptop") ;
        if (data.getTypedEquipment(types.get(item.getValue())) != null) {
            item.setSelected(true);
            portable.setSelected(true);
            rootTreeNode.setSelected(true);
        }
        portable.getChildren().add(item);

        item = new CheckBoxTreeItem<>("Smartphones") ;
        if (data.getTypedEquipment(types.get(item.getValue())) != null) {
            item.setSelected(true);
            portable.setSelected(true);
            rootTreeNode.setSelected(true);
        }
        portable.getChildren().add(item);
        item = new CheckBoxTreeItem<>("Tablet") ;
        if (data.getTypedEquipment(types.get(item.getValue())) != null) {
            item.setSelected(true);
            portable.setSelected(true);
            rootTreeNode.setSelected(true);
        }
        portable.getChildren().add(item);

        CheckBoxTreeItem<String> stationary = new CheckBoxTreeItem<>("StationaryComputer");
        item = new CheckBoxTreeItem<>("StationaryComputer") ;
        if (data.getTypedEquipment(types.get(item.getValue())) != null) {
            item.setSelected(true);
            stationary.setSelected(true);
            rootTreeNode.setSelected(true);
        }
        stationary.getChildren().add(item);

        rootTreeNode.getChildren().add(portable);
        rootTreeNode.getChildren().add(stationary);



        langsTreeView = new TreeView<>(rootTreeNode);


        langsTreeView.setOnMouseClicked(mouseEvent -> {

           CheckBoxTreeItem checkBoxTreeItem = (CheckBoxTreeItem) langsTreeView.getSelectionModel().getSelectedItem();

           if (mouseEvent.getClickCount() == 2 ) {

               if (langsTreeView.getSelectionModel().getSelectedItem().getChildren().isEmpty()) {

                   String type = types.get(langsTreeView.getSelectionModel().getSelectedItem().getValue());
                   if (!checkBoxTreeItem.isSelected()) {
                       if (data.getTypedEquipment(type) != null) {
                           data.sellComputer(type);
                       }
                   } else {
                           data.buyComputer(type);

                   }
               } else {
                   if (!checkBoxTreeItem.isSelected()) {
                       for (TreeItem<String> chitem : langsTreeView.getSelectionModel().getSelectedItem().getChildren()) {
                           if (chitem.getChildren().size() != 0) {
                           for (TreeItem<String> citem : chitem.getChildren()) {
                                   if (data.getTypedEquipment(types.get(citem.getValue())) != null)
                                       data.sellComputer(types.get(citem.getValue()));
                               }
                           } else {
                               if (data.getTypedEquipment(types.get(chitem.getValue())) != null)
                                   data.sellComputer(types.get(chitem.getValue()));
                           }
                       }
                   } else {
                       for (TreeItem<String> chitem : langsTreeView.getSelectionModel().getSelectedItem().getChildren()) {
                           if (chitem.getChildren().size() != 0) {
                               for (TreeItem<String> citem : chitem.getChildren()) {
                                   if (data.getTypedEquipment(types.get(citem.getValue())) == null)
                                       data.buyComputer(types.get(citem.getValue()));
                               }
                           } else {
                               if (data.getTypedEquipment(types.get(chitem.getValue())) == null)
                                   data.buyComputer(types.get(chitem.getValue()));
                           }
                       }
                   }
               }
           }
        });

        langsTreeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {

            @Override
            public TreeCell<String> call(TreeView<String> param) {


                    return new CheckBoxTreeCell<String>();

            }
        });

    }



    TreeView<String> getTree () {
        return  langsTreeView;
    }
}
