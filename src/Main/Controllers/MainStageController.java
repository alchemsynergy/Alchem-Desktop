package Main.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainStageController implements Initializable{

    @FXML
    private Button pane_button1, pane_button2, pane_button21, pane_button3, pane_button4;

    @FXML
    private Pane pane1, pane2, pane21, pane3, pane4;

    @FXML
    private StackPane switcher_pane;

    AnchorPane homePane = null;
    AnchorPane allFeaturesPane = null;
    AnchorPane dashboardPane = null;

    public MainStageController() throws IOException {
        homePane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/main_home_scene.fxml"));
        allFeaturesPane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/main_features_tab_scene.fxml"));
        dashboardPane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/main_dashboard_scene.fxml"));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button[] buttons = new Button[5];
        buttons[0] = pane_button1;
        buttons[1] = pane_button2;
        buttons[2] = pane_button3;
        buttons[3] = pane_button4;
        buttons[4] = pane_button21;

        Pane[] panes = new Pane[5];
        panes[0] = pane1;
        panes[1] = pane2;
        panes[2] = pane3;
        panes[3] = pane4;
        panes[4] = pane21;

        for(int i = 0; i < 5; i++){
            int finalI = i;
            buttons[i].setOnMouseEntered((event) ->{
                    buttons[finalI].setStyle("-fx-background-color: grey");
                    panes[finalI].setStyle("-fx-background-color: aqua ");

            });

            buttons[i].setOnMouseExited((event)->{
                    buttons[finalI].setStyle("-fx-background-color: #200020");
                    panes[finalI].setStyle("-fx-background-color: #200020");

            });

        }
    }

    public void viewHomeScene()
    {
        switcher_pane.getChildren().clear();
        homePane.setPrefWidth(switcher_pane.getWidth());
        homePane.setPrefHeight(switcher_pane.getHeight());
        switcher_pane.getChildren().addAll(homePane);
    }


    public void viewDashboardScene()
    {
        switcher_pane.getChildren().clear();
        dashboardPane.setPrefWidth(switcher_pane.getWidth());
        dashboardPane.setPrefHeight(switcher_pane.getHeight());
        switcher_pane.getChildren().addAll(dashboardPane);
    }

    public void viewAllFeaturesScene()
    {
        switcher_pane.getChildren().clear();
        switcher_pane.setPrefWidth(allFeaturesPane.getWidth());
        switcher_pane.setPrefHeight(allFeaturesPane.getHeight());
        switcher_pane.getChildren().addAll(allFeaturesPane);
    }
}

