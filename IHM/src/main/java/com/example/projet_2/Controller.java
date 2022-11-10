package com.example.projet_2;

import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private ArrayList<ArrayList<String>> products = new ArrayList<>();

    private BDD bdd = new BDD();
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
    private TextArea result;

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
    private TextField dbAdresse;

    @FXML
    private TextField dbName;

    @FXML
    private TextField dbPort;

    @FXML
    private TextField dbLogin;

    @FXML
    private TextField dbPwd;

    @FXML
    private Button dbSend;

    @FXML
    private Button dbClose;

    // Réinitialise la valeur du menu déroulant quand on appuie sur Effacer
    public void refreshComboBox() {
        genre.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(genre.getPromptText());
                } else {
                    setText(item);
                }
            }
        });
    }

    @FXML
    // Réinitialise tout les champs
    protected void clean() {
        title.setText("");
        genre.getSelectionModel().clearSelection();
        refreshComboBox();
        date.setValue(null);
        priceMin.setText("");
        priceMax.setText("");
        discogs.setSelected(false);
        fnac.setSelected(false);
        vinylcorner.setSelected(false);
        leboncoin.setSelected(false);
        mesvinyles.setSelected(false);
        culturefactory.setSelected(false);
    }

    @FXML
    // Ferme la fenêtre
    protected void close() {
        Platform.exit();
    }

    // Génération de la fenêtre d'envoi de mail
    public void mailPopup() throws IOException {
        Stage popupmail = new Stage();
        popupmail.initModality(Modality.APPLICATION_MODAL);
        popupmail.setTitle("Envoi courriel");
        Label label1 = new Label ("Saisie du courriel");
        Label label2 = new Label("Veuillez saisir l'email de l'expéditeur");
        TextField textField = new TextField();
        Button button1 = new Button("Envoyer");
        Button button2 = new Button("Annuler");
        button1.setOnAction(event -> Mail.send(textField.getText(), result.getText()));
        button2.setOnAction(event -> popupmail.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, textField, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupmail.setScene(scene1);
        popupmail.showAndWait();
    }

    // Ouvre la fenêtre de paramétrage de la base de données
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
        Stage popupdb = new Stage();
        popupdb.initModality(Modality.APPLICATION_MODAL);
        popupdb.setTitle("Transmission BDD");
        Label label1 = new Label ("Transmission des données à la base de données");
        Label label2 = new Label("Cliquez sur Valider pour lancer la transmission :");
        Button button1 = new Button("Valider");
        Button button2 = new Button("Annuler");
        button1.setOnAction(event -> saveInDb());
        button2.setOnAction(event -> popupdb.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupdb.setScene(scene1);
        popupdb.showAndWait();
    }

    public void noticePopup() throws Exception {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Mode d'emploi");
        Text text = new Text("Cette application a pour but de rechercher des vinyls sur plusieurs sites webs à la fois.\n" +
                "Pour cela, dans le champ titre, rentrez le nom de l'artiste, puis sélectionnez le genre dans le menu déroulant.\n" +
                "Pour l'année, ne prenez en compte que l'année, n'importe quelle date de l'année souhaitée fera l'affaire.\n" +
                "Saisissez ensuite la plage de prix a rechercher, puis le site à visiter. Le résultat de la recherche s'affichera dans la zone du bas.\n" +
                "Vous pouvez ensuite enregistrer le résultat de votre recherche dans Fichier->Enregistrer dans un fichier,\n" +
                "ou envoyer la recherche directement par e-mail dans Fichier-Envoi Courriel.\n" +
                "Vous pouvez également envoyez le résultat dans une base de données, en précisant ses informations dans Paramètres->Base de données\n" +
                "puis en enregistrant avec Fichier->Enregistrer dans la base de données.");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(text);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 650, 200);
        popup.setScene(scene);
        popup.showAndWait();
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

    public String writeResult() {
        String res = "";
        for (int i = 0; i < products.size(); i++) {
                res += "Article : " + products.get(i).get(0) + "\n"
                        + "Genre : " + products.get(i).get(1) + "\n"
                        + "Description : " + products.get(i).get(2) + "\n"
                        + "Prix : " + products.get(i).get(3) + "\n"
                        + "Annee : " + products.get(i).get(4) + "\n"
                        + "URL : " + products.get(i).get(5) + "\n";
        }
        return res;
    }

    public void scrap() throws IOException {
        String searchTitle = title.getText();
        String searchGenre = genre.getValue();
        int searchDate = Integer.parseInt(date.getValue().toString().substring(0, 4));
        double searchPriceMin = Double.parseDouble(priceMin.getText());
        double searchPriceMax = Double.parseDouble(priceMax.getText());
        if (discogs.isSelected()) {
            if (!Scrapping.ScrapDiscogs(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate).isEmpty()) {
                products.addAll(Scrapping.ScrapDiscogs(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate));
            } else {
                result.setText("La recherche sur Discogs n'a rien donnée.");
            }
        }
        if (fnac.isSelected()) {
            if (!Scrapping.ScrapFnac(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate).isEmpty()) {
                products.addAll(Scrapping.ScrapFnac(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate));
            } else {
                result.setText("La recherche sur la Fnac n'a rien donnée.");
            }
        }
        if (vinylcorner.isSelected()) {
            if (!Scrapping.ScrapVinylCorner(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate).isEmpty()) {
                products.addAll(Scrapping.ScrapVinylCorner(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate));
            } else {
                result.setText("La recherche sur Vinyl Corner n'a rien donnée.");
            }
        }
        if (leboncoin.isSelected()) {
            if (!Scrapping.ScrapLeboncoin(searchTitle, searchGenre, searchPriceMin, searchPriceMax).isEmpty()) {
                products.addAll(Scrapping.ScrapLeboncoin(searchTitle, searchGenre, searchPriceMin, searchPriceMax));
            } else {
                result.setText("La recherche sur Le Bon Coin n'a rien donnée.");
            }
        }
        if (mesvinyles.isSelected()) {
            if (!Scrapping.ScrapMesvinyles(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate).isEmpty()) {
                products.addAll(Scrapping.ScrapMesvinyles(searchTitle, searchGenre, searchPriceMin, searchPriceMax, searchDate));
            } else {
                result.setText("La recherche sur Mesvinyles n'a rien donnée.");
            }
        }
        if (culturefactory.isSelected()) {
            if (!Scrapping.ScrapCultureFactory(searchTitle, searchGenre, searchPriceMin, searchPriceMax).isEmpty()) {
                products.addAll(Scrapping.ScrapCultureFactory(searchTitle, searchGenre, searchPriceMin, searchPriceMax));
            } else {
                result.setText("La recherche sur Culture Factory n'a rien donnée.");
            }
        }
        System.out.println(writeResult());
        System.out.println(products);
        result.setText(writeResult());
    }

    public void saveSearch () throws Exception {
        if (result.getText().equals("")) {
            result.setText("Vous ne pouvez enregistrer une recherche vide.");
        } else {
            PrintWriter writer;
            File rep = new File("Recherche");
            rep.mkdir();

            String nameFile = "Recherche" + File.separator + "Resultat.txt";
            writer = new PrintWriter(new BufferedWriter(new FileWriter(nameFile)));
            writer.println(result.getText());
            writer.close();
        }
    }

    public void connectToBdd() throws Exception {
        String pwd = dbPwd.getText();
        if (dbPwd.getText().isEmpty()) {
            pwd = "";
        }
        bdd.setAdresse(dbAdresse.getText());
        bdd.setPort(dbPort.getText());
        bdd.setBdd(dbName.getText());
        bdd.setUser(dbLogin.getText());
        bdd.setPwd(pwd);
        bdd.connect();
    }

    public void saveInDb() {
        bdd.sendToDB(products);
    }
}