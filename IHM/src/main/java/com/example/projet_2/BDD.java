package com.example.projet_2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class BDD {
    private String adresse;
    private String port;
    private String bdd;
    private String user;
    private String pwd;

    public BDD(){}

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setBdd(String bdd) {
        this.bdd = bdd;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void connect() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + adresse + ":" + port + "/" + bdd, user, pwd)) {
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.format("SQL State : %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToDB(ArrayList<ArrayList<String>> scrap) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/vinyl", "root", "")) {
            for (int i = 0; i < scrap.size(); i++) {
                int idGenre = 0;
                switch (scrap.get(i).get(1)) {
                    case "Rock" : idGenre = 1;break;
                    case "Blues" : idGenre = 2;break;
                    case "Jazz" : idGenre = 3;break;
                    case "Reggae" : idGenre = 4;break;
                    case "Funk" : idGenre = 5;break;
                    case "Electro" : idGenre = 6;break;
                    case "DubStep" : idGenre = 7;break;
                    case "Soul" : idGenre = 8;break;
                }
                String SQL_INSERT = "INSERT INTO recherche (titre, description, prix, id_genre, annee) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT);
                pstmt.setString(1, scrap.get(i).get(0));
                pstmt.setString(2, scrap.get(i).get(2));
                pstmt.setString(3, scrap.get(i).get(3));
                pstmt.setInt(4, idGenre);
                pstmt.setString(5, scrap.get(i).get(4));
                int row = pstmt.executeUpdate();
                System.out.println(row);
            }
        } catch (SQLException e) {
            System.err.format("SQL State : %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
