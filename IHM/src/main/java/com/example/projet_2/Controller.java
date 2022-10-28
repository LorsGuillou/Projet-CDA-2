package com.example.projet_2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class Controller {
    @FXML
    private TextField title;

    @FXML
    private ComboBox<String> genre;

    @FXML
    private DatePicker date;

    @FXML
    private TextField priceMin;

    @FXML
    private TextField priceMax;

    @FXML
    private CheckBox discogs;

    @FXML
    private CheckBox fnac;

    @FXML
    private CheckBox vinylcorner;

    @FXML
    private CheckBox leboncoin;

    @FXML
    private CheckBox mesvinyles;

    @FXML
    private CheckBox culturefactory;

    @FXML
    private Button search;

    @FXML
    private Button erase;

    @FXML
    private MenuItem saveOne;

    @FXML
    private MenuItem send;

    @FXML
    private MenuItem saveTwo;

    @FXML
    private MenuItem exit;

    @FXML
    private MenuItem db;

    @FXML
    private MenuItem notice;

    @FXML
    private Button dbSend;

    @FXML
    private Button dbClose;

    @FXML
    protected void clean() {
        title.setText("");
        genre.setValue(genre.getPromptText());
        date.setValue(null);
        priceMin.setText("");
        priceMax.setText("");
        discogs.setSelected(false);
        fnac.setSelected(false);
        vinylcorner.setSelected(false);
        leboncoin.setSelected(false);
        mesvinyles.setSelected(false);
        culturefactory.setSelected(false);
    };

    @FXML
    protected void close() {
        Platform.exit();
    }

    public void mailPopup() throws IOException {
        Stage popupmail = new Stage();
        popupmail.initModality(Modality.APPLICATION_MODAL);
        popupmail.setTitle("Envoi courriel");
        Label label1 = new Label ("Saisie du courriel");
        Label label2 = new Label("Veuillez saisir l'email de l'expéditeur");
        TextField textField = new TextField();
        Button button1 = new Button("Envoyer");
        Button button2 = new Button("Annuler");
        button1.setOnAction(e -> popupmail.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, textField, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupmail.setScene(scene1);
        popupmail.showAndWait();
    }

    public void onMailClick() throws IOException {
        mailPopup();
    }

    public void dbScene() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HelloApplication.class.getResource("bddIHM.fxml"));
            Scene scene = new Scene(loader.load(), 500, 500);
            Stage stage = new Stage();
            stage.setTitle("Paramètres de la base de données");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dbPopup() throws IOException {
        Stage popupmail = new Stage();
        popupmail.initModality(Modality.APPLICATION_MODAL);
        popupmail.setTitle("Transmission BDD");
        Label label1 = new Label ("Transmission des données à la base de données");
        Label label2 = new Label("Veuillez saisir l'email de l'expéditeur");
        TextField textField = new TextField();
        Button button1 = new Button("Envoyer");
        Button button2 = new Button("Annuler");
        button1.setOnAction(e -> popupmail.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, textField, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupmail.setScene(scene1);
        popupmail.showAndWait();
    }

    @FXML
    protected void dbAccess() {
        try {
            dbScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void exitWindow() {
        Stage stage = (Stage) dbClose.getScene().getWindow();
        stage.close();
    }
}