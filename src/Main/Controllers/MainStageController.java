package Main.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by techfreakworm on 10/9/2017.
 */
public class MainStageController implements Initializable{

    @FXML
    private Button pane_button1, pane_button2, pane_button21, pane_button3, pane_button4;

    @FXML
    private Pane pane1, pane2, pane21, pane3, pane4;

    @FXML
    private AnchorPane scene_anchor_pane;

    AnchorPane homeScenePane = null;
    AnchorPane allFeaturesPane = null;
    AnchorPane dashboardPane = null;

    public MainStageController() throws IOException {
        homeScenePane = FXMLLoader.load(getClass().getResource("../../Resources/Layouts/main_home_scene.fxml"));
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
        System.out.println(scene_anchor_pane.getWidth());
        scene_anchor_pane.getChildren().clear();
        homeScenePane.setPrefWidth(scene_anchor_pane.getWidth());
        homeScenePane.setMinWidth(scene_anchor_pane.getWidth());
        homeScenePane.setMaxWidth(scene_anchor_pane.getWidth());
        scene_anchor_pane.getChildren().addAll(homeScenePane);
    }


    public void viewDashboardScene()
    {
        scene_anchor_pane.getChildren().clear();
        dashboardPane.setPrefWidth(scene_anchor_pane.getWidth());
        dashboardPane.setMinWidth(scene_anchor_pane.getWidth());
        dashboardPane.setMinWidth(scene_anchor_pane.getWidth());
        scene_anchor_pane.getChildren().addAll(dashboardPane);
    }

    public void viewAllFeaturesScene()
    {
        scene_anchor_pane.getChildren().clear();
        allFeaturesPane.setPrefWidth(scene_anchor_pane.getWidth());
        allFeaturesPane.setMinWidth(scene_anchor_pane.getWidth());
        allFeaturesPane.setMinWidth(scene_anchor_pane.getWidth());
        scene_anchor_pane.getChildren().addAll(allFeaturesPane);
    }
}

