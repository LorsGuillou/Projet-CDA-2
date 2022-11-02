module com.example.projet_2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires htmlunit;

    opens com.example.projet_2 to javafx.fxml;
    exports com.example.projet_2;
}