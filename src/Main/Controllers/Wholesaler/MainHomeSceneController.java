package Main.Controllers.Wholesaler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainHomeSceneController implements Initializable {

    @FXML
    Pane add_sale_pane, view_sale_pane, add_purchase_pane, view_purchase_pane, inventory_pane, about_pane;
    @FXML
    AnchorPane home_anchor;

    AnchorPane allFeaturesPane = null;
    SingleSelectionModel<Tab> singleSelectionModel = null;

    private double sceneWidth;
    private double sceneHeight;


    @Override
    public void initialize(URL location, ResourceBundle resources) {




        add_sale_pane.setOnMouseEntered((event) -> {
            add_sale_pane.setStyle("-fx-background-color: black");
        });
        add_sale_pane.setOnMouseExited((event) -> {
            add_sale_pane.setStyle("-fx-background-color: transparent");
        });

        add_sale_pane.setOnMouseClicked(event -> {
            viewTabs(1);
        });

        view_sale_pane.setOnMouseEntered((event) -> {
            view_sale_pane.setStyle("-fx-background-color: black");
        });

        view_sale_pane.setOnMouseExited(event -> {
            view_sale_pane.setStyle("-fx-background-color: transparent");
        });

        view_sale_pane.setOnMouseClicked(event -> {
            viewTabs(2);
        });

        add_purchase_pane.setOnMouseEntered(event -> {
            add_purchase_pane.setStyle("-fx-background-color: black");
        });

        add_purchase_pane.setOnMouseExited(event -> {
            add_purchase_pane.setStyle("-fx-background-color: transparent");
        });

        add_purchase_pane.setOnMouseClicked(event -> {
            viewTabs(3);
        });

        view_purchase_pane.setOnMouseEntered(event -> {
            view_purchase_pane.setStyle("-fx-background-color: black");
        });

        view_purchase_pane.setOnMouseExited(event -> {
            view_purchase_pane.setStyle("-fx-background-color: transparent");
        });

        view_purchase_pane.setOnMouseClicked(event -> {
            viewTabs(4);
        });

        inventory_pane.setOnMouseEntered(event -> {
            inventory_pane.setStyle("-fx-background-color: black");
        });

        inventory_pane.setOnMouseExited(event -> {
            inventory_pane.setStyle("-fx-background-color: transparent");
        });

        inventory_pane.setOnMouseClicked(event -> {
            viewTabs(0);
        });

        about_pane.setOnMouseEntered(event -> {
            about_pane.setStyle("-fx-background-color: black");
        });

        about_pane.setOnMouseExited(event -> {
            about_pane.setStyle("-fx-background-color: transparent");
        });

        about_pane.setOnMouseClicked(event -> {
            viewTabs(5);
        });

    }

    public void viewTabs(int index){

        Toolkit tk=Toolkit.getDefaultToolkit();
        sceneWidth=(int)tk.getScreenSize().getWidth()-25;
        sceneHeight=(int)tk.getScreenSize().getHeight()-50;
        try {
            allFeaturesPane = FXMLLoader.load(getClass().getResource("../../../Resources/Layouts/Retailers/main_features_tab_scene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(sceneHeight);
        System.out.println(sceneWidth);
        home_anchor.getChildren().clear();
        allFeaturesPane.setPrefWidth(sceneWidth);
        InventoryController.setDrawableWidth(sceneWidth);
        ViewSaleController.setDrawableWidth(sceneWidth);
        AddSaleController.setBillDrawableWidth(sceneWidth);
        allFeaturesPane.setPrefHeight(sceneHeight);
        home_anchor.getChildren().setAll(allFeaturesPane);
        singleSelectionModel = ((TabPane)allFeaturesPane.lookup("#tab_view")).getSelectionModel();
        singleSelectionModel.clearAndSelect(index);

    }
}
