package com.example.projet_2;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Scrapping {

    /*
    * Méthodes de scrapping : chaque méthode parcours un site, et retourne les données dans un ArrayList<ArrayList<String>>.
    * Si l'un des champs n'est pas trouvé sur le site, il sera remplacé par '--'.
     */

    public static ArrayList<ArrayList<String>> ScrapDiscogs(String searchTitle, String searchGenre, double searchPriceMin, double searchPriceMax, int searchYear) {
        ArrayList<ArrayList<String>> finalRes = new ArrayList<>();
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
                                ArrayList<String> res = new ArrayList<>();
                                res.add(article);
                                res.add(searchGenre);
                                if (!desc.isEmpty()) {
                                    res.add(desc.get(0).getTextContent());
                                } else {
                                    res.add("--");
                                }
                                res.add(prodPrice);
                                res.add("--");
                                res.add(String.valueOf(htmlPage2.getUrl()));
                                finalRes.add(res);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(finalRes);
        return finalRes;
    }

    public static ArrayList<ArrayList<String>> ScrapFnac(String searchTitle, String searchGenre, double searchPriceMin, double searchPriceMax, int searchYear) {
        ArrayList<ArrayList<String>> finalRes = new ArrayList<>();
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
                        ArrayList<String> res = new ArrayList<>();
                        res.add(article);
                        res.add(searchGenre);
                        if (!desc.isEmpty()) {
                            res.add(desc.get(0).getTextContent());
                        } else {
                            res.add("--");
                        }
                        res.add(prodPrice);
                        res.add("--");
                        res.add(String.valueOf(htmlPage2.getUrl()));
                        finalRes.add(res);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalRes;
    }

    public static ArrayList<ArrayList<String>> ScrapVinylCorner(String searchTitle, String searchGenre, double searchPriceMin, double searchPriceMax, int searchYear) {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "+");
        }

        ArrayList<ArrayList<String>> finalRes = new ArrayList<>();
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
                                    ArrayList<String> res = new ArrayList<>();
                                    res.add(article);
                                    res.add(searchGenre);
                                    if (!desc.isEmpty()) {
                                        res.add(desc.get(0).getTextContent());
                                    } else {
                                        res.add("--");
                                    }
                                    res.add(prodPrice);
                                    res.add(yearString);
                                    res.add(String.valueOf(htmlPage2.getUrl()));
                                    finalRes.add(res);
                                }
                            }
                        }
                    } else {
                        ArrayList<String> empty = new ArrayList<>();
                        String nothing = "La recherche sur Vinyl Corner n'a rien donnée.";
                        empty.add(nothing);
                        finalRes.add(empty);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalRes;
    }

    public static ArrayList<ArrayList<String>> ScrapLeboncoin(String searchTitle, String searchGenre, double searchPriceMin, double searchPriceMax) {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "%20");
        }
        ArrayList<ArrayList<String>> finalRes = new ArrayList<>();
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
                            ArrayList<String> res = new ArrayList<>();
                            res.add(article);
                            res.add(searchGenre);
                            if (!desc.isEmpty()) {
                                res.add(prodDesc);
                            } else {
                                res.add("--");
                            }
                            res.add(prodPrice);
                            res.add("--");
                            res.add(String.valueOf(htmlPage2.getUrl()));
                            finalRes.add(res);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalRes;
    }

    public static ArrayList<ArrayList<String>> ScrapMesvinyles(String searchTitle, String searchGenre, double searchPriceMin, double searchPriceMax, int searchYear) {

        ArrayList<ArrayList<String>> finalRes = new ArrayList<>();
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
                                    ArrayList<String> res = new ArrayList<>();
                                    res.add(article);
                                    res.add(searchGenre);
                                    res.add("--");
                                    res.add(prodPrice);
                                    res.add(yearString);
                                    res.add(String.valueOf(htmlPage2.getUrl()));
                                    finalRes.add(res);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalRes;
    }

    public static ArrayList<ArrayList<String>> ScrapCultureFactory(String searchTitle, String searchGenre, double searchPriceMin, double searchPriceMax) {
        if (searchTitle.contains(" ")) {
            searchTitle = searchTitle.replace(" ", "%+");
        }
        ArrayList<ArrayList<String>> finalRes = new ArrayList<>();
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
                            ArrayList<String> res = new ArrayList<>();
                            res.add(article);
                            res.add(searchGenre);
                            if (!desc.isEmpty()) {
                                res.add(desc.get(0).getTextContent());
                            } else {
                                res.add("--");
                            }
                            res.add(prodPrice);
                            res.add("--");
                            res.add(String.valueOf(htmlPage2.getUrl()));
                            finalRes.add(res);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalRes;
    }
}
