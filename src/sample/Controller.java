package sample;

import JdbcConnection.jdbc;
import extras.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Controller {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button close;

    @FXML
    private ToggleGroup type;

    @FXML
    private RadioButton wholesaler,retailer;

    public void initialize()
    {
      wholesaler.setUserData("Wholesaler");
      retailer.setUserData("Retailer");
    }

    public void login(ActionEvent event)
    {
        int flag=0,kind=0,chk=0;
        String user,pass;
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("AlertScreen.fxml"));
        user=username.getText();
        pass=password.getText();
        if(user.equals("") || pass.equals("") )
        {
          new AlertBox().alertfunction(Main.primaryStage,fxmlLoader,"Fill in the missing fields");
        }
        else
        {
            try
            {
                Connection conn = jdbc.jconn_ankit();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM user_access");
                while (rs.next())
                {
                    if(user.equals(rs.getString("username")) && pass.equals(rs.getString("password")))
                    {
                        chk=rs.getInt("user_type_id");
                        ResultSet rs1=stmt.executeQuery("select * from user_type");
                        while(rs1.next())
                        {
                            if(type.getSelectedToggle().getUserData().toString().equals(rs1.getString("type")))
                            {
                                kind=rs1.getInt("user_type_id");
                                break;
                            }
                        }
                        rs1.close();
                        if(chk==kind) {
                            flag=1;
                        }
                        break;
                    }
                }
                rs.close();
                if(flag==1)
                {
                    FXMLLoader fxmlLoader1=new FXMLLoader(getClass().getResource("blank.fxml"));
                    Parent root1=(Parent) fxmlLoader1.load();
                    Stage stage1=new Stage();
                    stage1.setScene(new Scene(root1));
                    stage1.showAndWait();
                }
                else
                {
                   new AlertBox().alertfunction(Main.primaryStage,fxmlLoader,"Invalid Credentials");
                }
            } catch (Exception e) {}
        }
    }
    public void closeAction(ActionEvent e)
    {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
    public void joinNow(ActionEvent e)
    {
        try {
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root2=(Parent) fxmlLoader2.load();
            Stage stage2=new Stage();
            stage2.setScene(new Scene(root2));
            stage2.setResizable(false);
            stage2.initModality(Modality.WINDOW_MODAL);
            stage2.showAndWait();
        }
        catch (Exception e1) {}
    }
}
