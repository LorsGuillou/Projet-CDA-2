<?php 

require_once __DIR__ . '/vendor/autoload.php';

$dotenv = Dotenv\Dotenv::createImmutable(__DIR__);
$dotenv->load();

require_once './app/Back/Manager.php';


try {
    $manager = new Manager();
    if (isset($_GET['action'])) {
        if ($_GET['action'] == 'home') {
            $manager->home();
        } elseif ($_GET['action'] == 'page') {
            $manager->page();
        } else if ($_GET['action'] == 'scrap') {
            $manager->scrap();
        }
    } else {
        $manager->home();
    }
} catch (Exception $e) {
    $e->getMessage();
}


