package com.example.projet_2;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class Scrapping extends Thread {
    private String title;
    private String genre;
    private LocalDate date;
    private String min;
    private String max;
    private boolean discogs = false;
    private boolean fnac = false;
    private boolean vinylcorner = false;
    private boolean leboncoin = false;
    private boolean mesvinyles = false;
    private boolean culturefactory = false;
    private String res = "";

    public Scrapping (String title, String genre, LocalDate date, String min, String max) {
        this.title = title;
        this.genre = genre;
        this.date = date;
        this.min = min;
        this.max = max;
    }

    public void setDiscogs(boolean discogs) {
        this.discogs = discogs;
    }

    public void setFnac(boolean fnac) {
        this.fnac = fnac;
    }

    public void setVinylcorner(boolean vinylcorner) {
        this.vinylcorner = vinylcorner;
    }

    public void setLeboncoin(boolean leboncoin) {
        this.leboncoin = leboncoin;
    }

    public void setMesvinyles(boolean mesvinyles) {
        this.mesvinyles = mesvinyles;
    }

    public void setCulturefactory(boolean culturefactory) {
        this.culturefactory = culturefactory;
    }

    public void setRes(String res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return res;
    }

    @Override
    public void run() {
        PrintWriter doc;
        String titleSearch = title;
        String genreSearch = genre;
        LocalDate dateSearch = date;
        String minPrice = min;
        String maxPrice = max;
        String url;
        WebClient webclient = new WebClient();
        webclient.getOptions().setUseInsecureSSL(true);
        webclient.getOptions().setCssEnabled(false);
        webclient.getOptions().setJavaScriptEnabled(false);
        HtmlPage htmlPage;
        if (leboncoin) {
            url = "https://leboncoin.fr/recherche?category=26&text=" + titleSearch + "&price=" + minPrice + "-" + maxPrice;
            try {
                htmlPage = webclient.getPage(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<HtmlElement> li = htmlPage.getByXPath("//div[1]/div[1]/p");
            for (HtmlElement e : li) {
                HtmlPage htmlPage1 = null;
                try {
                    htmlPage1 = webclient.getPage(e.click().getUrl());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
                setRes("Article : " + nomArticle + "\nPrix : " + prixArticle + "\nDescription de l'article : " + description + "\nlien : " + htmlPage1.getUrl() +
                "\n--------------------------------------------------------------------------------------------\n");
            }
        }
    }
}
