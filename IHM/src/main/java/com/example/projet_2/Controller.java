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
    }

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
        double searchPriceMin = Double.parseDouble(priceMin.getText());
        double searchPriceMax = Double.parseDouble(priceMax.getText());
        if (leboncoin.isSelected()) {
            ScrapLeboncoin(searchTitle, searchPriceMin, searchPriceMax);
        }
        if (vinylcorner.isSelected()) {
            ScrapVinylCorner(searchTitle);
        }
    }

    public String ScrapLeboncoin(String searchTitle, double searchPriceMin, double searchPriceMax) {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "%20");
        }
        String res = "Résultats Le Bon Coin :\n";
        String url = "https://leboncoin.fr/recherche?category=26&text=" + searchTitle + "&price=" + searchPriceMin + "-" + searchPriceMax;

        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage1 = webClient.getPage(url);

            List<HtmlAnchor> produits = htmlPage1.getByXPath("//p");
            for (HtmlElement prod : produits) {
                String article = prod.getTextContent();
                if (article.toLowerCase().contains(searchTitle.toLowerCase())) {
                    HtmlPage htmlPage2 = prod.click();
                    List<HtmlElement> desc = htmlPage2.getByXPath("//div[5]/div/div/p");
                    List<HtmlElement> price = htmlPage2.getByXPath("//div[3]/div/span/div/div[1]/div/span");
                    for (HtmlElement p : price) {
                        String prodPrice = p.getTextContent();
                        for (HtmlElement d : desc) {
                            String prodDesc = d.getTextContent();
                            res += "Article : " + prod + "\n" +
                                    "Prix : " + prodPrice + "\n" +
                                    "Description : " + prodDesc +
                                    "URL : " + htmlPage2.getUrl() + "\n" +
                                    "_______________________________________";
                        }
                    }
                }
            }
            System.out.println(res);
            if (res.equals("")) {
                res = "La recherche sur Le Bon Coin n'a rien donnée.";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String ScrapCultureFactory(String searchTitle, double searchPriceMin, double searchPriceMax) {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "%+");
        }
        String res = "Résultats Culture Factory :\n";
        String url = "https://culturefactory.fr/recherche?controller=search&s=" + searchTitle + "&price=" + searchPriceMin + "-" + searchPriceMax;

        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage1 = webClient.getPage(url);

            List<HtmlAnchor> produits = htmlPage1.getByXPath("//p");
            for (HtmlElement prod : produits) {
                String article = prod.getTextContent();
                if (article.toLowerCase().contains(searchTitle.toLowerCase())) {
                    HtmlPage htmlPage2 = prod.click();
                    List<HtmlElement> desc = htmlPage2.getByXPath("//div[5]/div/div/p");
                    List<HtmlElement> price = htmlPage2.getByXPath("//div[3]/div/span/div/div[1]/div/span");
                    for (HtmlElement p : price) {
                        String prodPrice = p.getTextContent();
                        for (HtmlElement d : desc) {
                            String prodDesc = d.getTextContent();
                            res += "Article : " + prod + "\n" +
                                    "Prix : " + prodPrice + "\n" +
                                    "Description : " + prodDesc +
                                    "URL : " + htmlPage2.getUrl() + "\n" +
                                    "_______________________________________";
                        }
                    }
                }
            }
            System.out.println(res);
            if (res.equals("")) {
                res = "La recherche sur Le Bon Coin n'a rien donnée.";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String ScrapVinylCorner(String searchTitle) throws IOException {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "+");
        }

        String res = "";
        String url = "https://www.vinylcorner.fr/recherche?controller=search&s=" + searchTitle.toLowerCase();

        try {
            WebClient webClient = new WebClient();

            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage = webClient.getPage(url);
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
                for (HtmlElement t : title) {
                    nomAlbum = t.getTextContent();
                    System.out.println(t.getTextContent());
                }
                for (HtmlElement p : price) {
                    prixArticle = p.getTextContent();
                    System.out.println(p.getTextContent());
                }
                for (HtmlElement de : desc) {
                    description = de.getTextContent();
                    System.out.println(de.getTextContent());
                }
                for (HtmlElement da : date) {
                    year = da.getTextContent();
                    System.out.println(da.getTextContent());
                }
                res += "Titre : " + nomAlbum +
                        "\nPrix : " + prixArticle +
                        "\nDescription de l'article : " + description +
                        "\nAnnée : " + year +
                        "\nlien : " + prodPage.getUrl() +
                        "\n--------------------------------------------------------------------\n";
                System.out.println();
            }
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}