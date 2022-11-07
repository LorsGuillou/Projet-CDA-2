package com.example.projet_2;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.util.List;

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
    private Button dbSend;

    @FXML
    private Button dbClose;

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
        button2.setOnAction(event -> popupmail.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, textField, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupmail.setScene(scene1);
        popupmail.showAndWait();
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
        Stage popupdb = new Stage();
        popupdb.initModality(Modality.APPLICATION_MODAL);
        popupdb.setTitle("Transmission BDD");
        Label label1 = new Label ("Transmission des données à la base de données");
        Label label2 = new Label("Cliquez sur Valider pour lancer la transmission :");
        Button button1 = new Button("Valider");
        Button button2 = new Button("Annuler");
        button2.setOnAction(event -> popupdb.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, label2, button1, button2);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupdb.setScene(scene1);
        popupdb.showAndWait();
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

    public void scrap() throws Exception {
        String searchTitle = title.getText();
        String searchGenre = genre.getValue();
        String searchDate = date.getValue().toString().substring(0, 4);
        String searchPriceMin = priceMin.getText();
        String searchPriceMax = priceMax.getText();
        if (leboncoin.isSelected()) {
            ScrapLeboncoin(searchTitle, searchPriceMin, searchPriceMax);
        }
        if (vinylcorner.isSelected()) {
            ScrapVinylCorner(searchTitle);
        }
    }

    public void ScrapLeboncoin(String searchTitle, String searchPriceMin, String searchPriceMax) {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "%20");
        }
        String url = "https://leboncoin.fr/recherche?category=26&text=" + searchTitle + "&price=" + searchPriceMin + "-" + searchPriceMax;

        try {
            WebClient webClient = new WebClient();

            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage = webClient.getPage(url);
            List<HtmlElement> li = htmlPage.getByXPath("//div[2]/div[1]/p");
            String res = "";
            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = webClient.getPage(e.click().getUrl());
                String nomArticle = "";
                String prixArticle = "";
                String description = "";

                List<HtmlElement> nom = htmlPage1.getByXPath("//div[3]/div/div/h1");
                List<HtmlElement> prix = htmlPage1.getByXPath("//div[3]/div/span//div/div[1]/div/span");
                List<HtmlElement> desc = htmlPage1.getByXPath("//div[5]/div/div/p");
                for (HtmlElement n : nom) {
                    nomArticle = n.getTextContent();
                }
                for (HtmlElement p : prix) {
                    prixArticle = p.getTextContent();
                    prixArticle = prixArticle.replace("\u00a0", "");
                }
                for (HtmlElement d : desc) {
                    description = d.getTextContent();
                }
                res += "Article : " + nomArticle +
                        "\n Prix : " + prixArticle +
                        "\n Description de l'article : " + description +
                        "\n lien : " + htmlPage1.getUrl() +
                        "\n--------------------------------------------------------------------\n";
            }
            this.result.setText(res);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ScrapVinylCorner(String searchTitle) {

        String url = "https://www.vinylcorner.fr/recherche?controller=search&s=" + searchTitle;

        try {
            PrintWriter ecrire;
            File rep = new File("Resultat");
            rep.mkdir();

            String nomFichier = "Resultat" + File.separator + searchTitle + ".txt";

            ecrire = new PrintWriter(new BufferedWriter(new FileWriter(nomFichier)));

            WebClient webClient = new WebClient();

            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage = webClient.getPage(url);
            String res = "";
            List<HtmlElement> liens = htmlPage.getByXPath("//h2/a");
            for (HtmlElement e : liens) {
                String nomAlbum = "";
                String prixArticle = "";
                String description = "";
                String year = "";
                String value = e.getAttribute("href");
                String urlProd = "https://www.vinylcorner.fr" + value;
                HtmlPage prodPage = e.click();
                List<HtmlElement> title = prodPage.getByXPath("//h2[@class='artist']");
                List<HtmlElement> price = prodPage.getByXPath("//span[@itemprop='price']");
                List<HtmlElement> desc = prodPage.getByXPath("//p[@class='product-info']");
                List<HtmlElement> date = prodPage.getByXPath("//strong");

                System.out.println(urlProd);
                ecrire.println(urlProd);
                for (HtmlElement t : title) {
                    nomAlbum = t.getTextContent();
                    System.out.println(t.getTextContent());
                    ecrire.println(t.getTextContent());
                }
                for (HtmlElement p : price) {
                    prixArticle = p.getTextContent();
                    System.out.println(p.getTextContent());
                    ecrire.println(p.getTextContent());
                }
                for (HtmlElement de : desc) {
                    description = de.getTextContent();
                    System.out.println(de.getTextContent());
                    ecrire.println(de.getTextContent());
                }
                for (HtmlElement da : date) {
                    year = da.getTextContent();
                    System.out.println(da.getTextContent());
                    ecrire.println(da.getTextContent());
                }
                res += "Titre : " + nomAlbum +
                        "\nPrix : " + prixArticle +
                        "\nDescription de l'article : " + description +
                        "\nAnnée : " + year +
                        "\nlien : " + prodPage.getUrl() +
                        "\n--------------------------------------------------------------------\n";
                System.out.println();
                ecrire.println();
            }
            ecrire.close();
            this.result.setText(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}