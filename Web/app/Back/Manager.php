<?php

use FFI\Exception;

class Manager {
    private static $pdo = null;

    protected static function dbConnect() {
        if (isset(self::$pdo)) {
            return self::$pdo;
        } else {
            try {
                self::$pdo = new \PDO (
                    'mysql:host=' . $_ENV['DB_HOST'] . ';
                    dbname=' . $_ENV['DB_NAME'] . ';
                    charset=utf8',
                    $_ENV['DB_USERNAME'],
                    $_ENV['DB_PASSWORD']
                );
                return self::$pdo;
            } catch (Exception $e) {
                die('Erreur : ' . $e->getMessage());
            }
        }
    }

    public static function getDb() {
        $pdo = self::dbConnect();
        $req = $pdo->prepare('SELECT recherche.titre, recherche.description, recherche.prix, recherche.annee, genre.libelle FROM recherche INNER JOIN genre ON recherche.id_genre = genre.id');
        $req->execute(array());
        $res = $req->fetchAll();

        return $res;
    }

    public function home() {
        require ("./app/Front/home.php");
    }

    public function page() {
        require ("./app/Front/page.php");
    }

    public function scrap() {
        $scraps = self::getDb();
        require ("./app/Front/scrap.php");
    }
}