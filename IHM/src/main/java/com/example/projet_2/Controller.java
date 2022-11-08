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

    public void scrap() throws IOException {
        String res = "";
        String searchTitle = title.getText();
        String searchGenre = genre.getValue();
        int searchDate = Integer.parseInt(date.getValue().toString().substring(0, 4));
        double searchPriceMin = Double.parseDouble(priceMin.getText());
        double searchPriceMax = Double.parseDouble(priceMax.getText());
        if (discogs.isSelected()) {
            res += ScrapDiscogs(searchTitle, searchPriceMin, searchPriceMax, searchDate, searchGenre);
        }
        if (fnac.isSelected()) {
            res += ScrapFnac(searchTitle, searchPriceMin, searchPriceMax, searchDate);
        }
        if (vinylcorner.isSelected()) {
            res += ScrapVinylCorner(searchTitle, searchPriceMin, searchPriceMax, searchDate, searchGenre);
        }
        if (leboncoin.isSelected()) {
            res += ScrapLeboncoin(searchTitle, searchPriceMin, searchPriceMax);
        }
        if (mesvinyles.isSelected()) {
            res += ScrapMesvinyles(searchTitle, searchPriceMin, searchPriceMax, searchDate);
        }
        if (culturefactory.isSelected()) {
            res += ScrapCultureFactory(searchTitle, searchPriceMin, searchPriceMax);
        }
        System.out.println(res);
        result.setText(res);
    }

    public String ScrapDiscogs(String searchTitle, double searchPriceMin, double searchPriceMax, int searchYear, String searchGenre) {
        String res = "";
        if (searchGenre.equals("Funk") || searchGenre.equals("Soul")) {
            searchGenre = "Funk+%2F+Soul";
        } else if (searchGenre.equals("Electro")) {
            searchGenre = "Electronic";
        } else if (searchGenre.equals("DubStep")) {
            searchGenre = "Stage+%26+Screen";
        }
        String url = "https://www.discogs.com/fr/search/?q=" + searchTitle.toLowerCase() + "&type=all&genre_exact" + searchGenre + "&format_exact=Vinyl";

        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage1 = webClient.getPage(url);
            List<HtmlAnchor> product = htmlPage1.getByXPath("//div[1]/a");
            for (HtmlElement prod : product) {
                String article = prod.getTextContent();
                HtmlPage htmlPage2 = prod.click();
                List<HtmlElement> desc = htmlPage2.getByXPath("//div[2]/div[1]/div[5]/section/div/div");
                List<HtmlElement> date = htmlPage2.getByXPath("//table/tbody/tr[3]/td/a/time");
                List<HtmlElement> price = htmlPage2.getByXPath("//div[2]/div[1]/div[11]/section/header/div/span/span");
                for (HtmlElement d : date) {
                    String yearString = d.getTextContent();
                    int prodYear = Integer.parseInt(yearString);
                    if (prodYear <= searchYear) {
                        for (HtmlElement p : price) {
                            String prodPrice = p.getTextContent();
                            prodPrice = prodPrice.replaceAll("[^0-9]", "");
                            prodPrice = prodPrice.replace(",", ".");
                            double prodPriceDouble = Double.parseDouble(prodPrice);
                            if (prodPriceDouble <= searchPriceMax && prodPriceDouble >= searchPriceMin) {
                                res += "Article : " + article + "\n" +
                                        "Prix : " + prodPrice + "\n";
                                if (!desc.isEmpty()) {
                                    res += "Description : " + desc.get(0).getTextContent() + "\n";
                                }
                                res += "URL : " + htmlPage2.getUrl() + "\n" +
                                        "_______________________________________\n\n";
                            }
                        }
                    }
                    if (res.equals("")) {
                        res = "La recherche sur Discogs n'a rien donnée";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return res;
    }

    public String ScrapFnac(String searchTitle, double searchPriceMin, double searchPriceMax, int searchYear) {
        String res = "";
        String url = "https://www.fnac.com/SearchResult/ResultList.aspx?SCat=0&Search=" + searchTitle;

        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage1 = webClient.getPage(url);
            List<HtmlAnchor> product = htmlPage1.getByXPath("//article/form/div[2]/div/p[1]/span/a");
            for (HtmlElement prod : product) {
                String article = prod.getTextContent();
                HtmlPage htmlPage2 = prod.click();
                List<HtmlElement> desc = htmlPage2.getByXPath("//div[2]/div/div[1]/div[2]/div[2]/div[1]/div[2]/div");
                List<HtmlElement> date = htmlPage2.getByXPath("//div[2]/dl[2]/dd/p");
                List<HtmlElement> price = htmlPage2.getByXPath(".//span[contains(@class,'userPrice')]");
                String yearString = date.get(0).getTextContent();
                yearString = yearString.replaceAll("[^0-9]", "");
                int prodYear = Integer.parseInt(yearString);
                if (prodYear <= searchYear) {
                    String prodPrice = price.get(0).getTextContent();
                    prodPrice = prodPrice.replaceAll("[^0-9]", "");
                    prodPrice = prodPrice.replace(",", ".");
                    double prodPriceDouble = Double.parseDouble(prodPrice);
                    if (prodPriceDouble <= searchPriceMax && prodPriceDouble >= searchPriceMin) {
                        res += "Article : " + article + "\n" +
                                "Prix : " + prodPrice + "\n";
                        if (!desc.isEmpty()) {
                            res += "Description : " + desc.get(0).getTextContent() + "\n";
                        }
                        res += "URL : " + htmlPage2.getUrl() + "\n" +
                                "_______________________________________\n\n";
                    }
                }
                if (res.equals("")) {
                    res = "La recherche sur la Fnac n'a rien donnée.";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String ScrapVinylCorner(String searchTitle, double searchPriceMin, double searchPriceMax, int searchYear, String searchGenre) {
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
            HtmlPage htmlPage1 = webClient.getPage(url);
            List<HtmlElement> product = htmlPage1.getByXPath("//div/div[2]/div[1]/h3/a");
            for (HtmlElement prod : product) {
                String article = prod.getTextContent();
                HtmlPage htmlPage2 = prod.click();
                List<HtmlElement> desc = htmlPage2.getByXPath(".//div[contains[@class,'product-description')]");
                List<HtmlElement> date = htmlPage2.getByXPath("//div[2]/div/div/div/section/div[1]/div[2]/div[1]/p[2]/strong");
                List<HtmlElement> price = htmlPage2.getByXPath("//div[1]/div[2]/div[2]/div[1]/div/span");
                List<HtmlElement> genre = htmlPage2.getByXPath("//div[2]/div/div/div/section/div[1]/div[2]/div[1]/p[4]");
                for (HtmlElement d : date) {
                    String yearString = d.getTextContent();
                    yearString = yearString.substring(yearString.length() - 4);
                    int prodYear = Integer.parseInt(yearString);
                    if (prodYear <= searchYear) {
                        if (genre.get(0).getTextContent().toLowerCase().contains(searchGenre.toLowerCase())) {
                            for (HtmlElement p : price) {
                                String prodPrice = p.getTextContent();
                                prodPrice = prodPrice.replaceAll("[^0-9]", "");
                                prodPrice = prodPrice.replace(",", ".");
                                double prodPriceDouble = Double.parseDouble(prodPrice);
                                if ((prodPriceDouble <= searchPriceMax && prodPriceDouble >= searchPriceMin)) {
                                    res += "Article : " + article + "\n" +
                                            "Prix : " + prodPrice + "\n";
                                    if (!desc.isEmpty()) {
                                        res += "Description : " + desc.get(0).getTextContent() + "\n";
                                    }
                                    res += "URL : " + htmlPage2.getUrl() + "\n" +
                                            "_______________________________________\n\n";
                                }
                            }
                        }
                    }
                    if (res.equals("")) {
                        res = "La recherche sur Vinyl Corner n'a rien donnée.";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String ScrapLeboncoin(String searchTitle, double searchPriceMin, double searchPriceMax) {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "%20");
        }
        String res = "";
        String url = "https://leboncoin.fr/recherche?category=26&text=" + searchTitle.toLowerCase() + "&price=" + searchPriceMin + "-" + searchPriceMax;

        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage1 = webClient.getPage(url);

            List<HtmlAnchor> product = htmlPage1.getByXPath("//p");
            for (HtmlElement prod : product) {
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
                                    "Description : " + prodDesc + "\n" +
                                    "URL : " + htmlPage2.getUrl() + "\n" +
                                    "_______________________________________\n\n";
                        }
                    }
                }
            }
            if (res.equals("")) {
                res = "La recherche sur Le Bon Coin n'a rien donnée.";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public String ScrapMesvinyles(String searchTitle, double searchPriceMin, double searchPriceMax, int searchYear) {

        String res = "";
        String url = "https://mesvinyles.fr/fr/recherche?controller=search&s=" + searchTitle;

        try {
            WebClient webClient = new WebClient();

            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage1 = webClient.getPage(url);
            List<HtmlElement> product = htmlPage1.getByXPath("//div/div[2]/h3/a");
            for (HtmlElement prod : product) {
                String article = prod.getTextContent();
                if (article.toLowerCase().contains(searchTitle.toLowerCase())) {
                    HtmlPage htmlPage2 = prod.click();
                    List<HtmlElement> date = htmlPage2.getByXPath("//div[2]/div/div/div/section/div[1]/div[2]/div[1]/p[2]/strong");
                    List<HtmlElement> price = htmlPage2.getByXPath("//div[1]/div[2]/div[2]/div[1]/div/span");
                    for (HtmlElement d : date) {
                        String yearString = d.getTextContent();
                        yearString = yearString.replaceAll("[^0-9]", "");
                        int prodYear;
                        if (yearString.equals("")) {
                            prodYear = 0;
                        } else {
                            prodYear = Integer.parseInt(yearString);
                        }
                        if (prodYear <= searchYear) {
                            for (HtmlElement p : price) {
                                String prodPrice = p.getTextContent();
                                prodPrice = prodPrice.replaceAll("[^0-9]", "");
                                prodPrice = prodPrice.replace(",", ".");
                                double prodPriceDouble = Double.parseDouble(prodPrice);
                                if ((prodPriceDouble <= searchPriceMax && prodPriceDouble >= searchPriceMin)) {
                                    res += "Article : " + prod + "\n" +
                                            "Prix : " + prodPrice + "\n" +
                                            "URL : " + htmlPage2.getUrl() + "\n" +
                                            "_______________________________________\n\n";
                                }
                            }
                        }
                        if (res.equals("")) {
                            res = "La recherche sur Mesvinyles n'a rien donnée.";
                        }
                    }
                }
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
        String res = "";
        String url = "https://culturefactory.fr/recherche?controller=search&s=" + searchTitle.toLowerCase();

        try {
            WebClient webClient = new WebClient();
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage1 = webClient.getPage(url);

            List<HtmlAnchor> product = htmlPage1.getByXPath("//article/div[2]/h4/a");
            for (HtmlElement prod : product) {
                String article = prod.getTextContent();
                if (article.toLowerCase().contains(searchTitle.toLowerCase()) && article.toLowerCase().contains("vinyle")) {
                    HtmlPage htmlPage2 = prod.click();
                    List<HtmlElement> desc = htmlPage2.getByXPath(".//div[contains(@class,'product-desc')]");
                    List<HtmlElement> price = htmlPage2.getByXPath("//div[1]/div[2]/div/div/section/div[1]/div[2]/div[1]/div[1]/div/span");
                    for (HtmlElement p : price) {
                        String prodPrice = p.getTextContent();
                        prodPrice = prodPrice.replaceAll("[^0-9]", "");
                        prodPrice = prodPrice.replace(",", ".");
                        double prodPriceDouble = Double.parseDouble(prodPrice);
                        if (prodPriceDouble <= searchPriceMax && prodPriceDouble >= searchPriceMin) {
                            String prodDesc = desc.get(0).getTextContent();
                            res += "Article : " + prod + "\n" +
                                    "Prix : " + prodPrice + "\n";
                                    if (!prodDesc.isEmpty()) {
                                        res += "Description : " + prodDesc + "\n";
                                    }
                                    res += "URL : " + htmlPage2.getUrl() + "\n" +
                                            "_______________________________________\n\n";
                        }
                    }
                }
            }
            if (res.equals("")) {
                res = "La recherche sur Culture Factory n'a rien donnée.";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void saveSearch () throws Exception {
        PrintWriter writer;
        File rep = new File("Recherche");
        rep.mkdir();

        String nameFile = "Recherche" + File.separator + title.getText() + ".txt";
        writer = new PrintWriter(new BufferedWriter(new FileWriter(nameFile)));
        writer.println(result.getText());
        writer.close();
    }
}