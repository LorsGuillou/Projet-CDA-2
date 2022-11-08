module com.example.projet_2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires htmlunit;
    requires sib.api.v3.sdk;
    requires dotenv.java;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.projet_2 to javafx.fxml;
    exports com.example.projet_2;
}