package extras;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.Main;

/**
 * Created by VIPUL GOYAL on 9/10/2017.
 */
public class AlertBox {
    public void alertfunction(Stage primaryStage,FXMLLoader fxmlLoader,String msg)
    {
        try {
            Message.setMsg(msg);
            Parent root=(Parent) fxmlLoader.load();
            Stage stage=new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch (Exception e){}
    }
}
