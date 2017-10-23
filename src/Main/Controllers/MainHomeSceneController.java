package Main.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainHomeSceneController implements Initializable{

    @FXML
    Pane add_sale_pane, view_sale_pane, add_purchase_pane, view_purchase_pane, inventory_pane, about_pane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        add_sale_pane.setOnMouseEntered((event) -> {
            add_sale_pane.setStyle("-fx-background-color: black");
        });
        add_sale_pane.setOnMouseExited((event)->{
            add_sale_pane.setStyle("-fx-background-color: transparent");
        });

        view_sale_pane.setOnMouseEntered((event) ->{
            view_sale_pane.setStyle("-fx-background-color: black");
        });

        view_sale_pane.setOnMouseExited(event -> {
            view_sale_pane.setStyle("-fx-background-color: transparent");
        });

        add_purchase_pane.setOnMouseEntered(event -> {
            add_purchase_pane.setStyle("-fx-background-color: black");
        });

        add_purchase_pane.setOnMouseExited(event -> {
            add_purchase_pane.setStyle("-fx-background-color: transparent");
        });

        view_purchase_pane.setOnMouseEntered(event -> {
            view_purchase_pane.setStyle("-fx-background-color: black");
        });

        view_purchase_pane.setOnMouseExited(event -> {
            view_purchase_pane.setStyle("-fx-background-color: transparent");
        });

        inventory_pane.setOnMouseEntered(event -> {
            inventory_pane.setStyle("-fx-background-color: black");
        });

        inventory_pane.setOnMouseExited(event -> {
            inventory_pane.setStyle("-fx-background-color: transparent");
        });

        about_pane.setOnMouseEntered(event -> {
            about_pane.setStyle("-fx-background-color: black");
        });

        about_pane.setOnMouseExited(event -> {
            about_pane.setStyle("-fx-background-color: transparent");
        });

    }
}
