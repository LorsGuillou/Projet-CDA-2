<?php include_once ("header.php") ?>

<main class="narrow">
    <?php foreach($scraps as $scrap) : ?>
        <p><?= $scrap['titre'] ?></p>
        <p><?= $scrap['libelle'] ?></p>
        <p><?= $scrap['description'] ?></p>
        <p><?= $scrap['prix'] ?></p>
        <p><?= $scrap['annee'] ?></p>
    <?php endforeach ?>    
</main>

<?php include_once ("footer.php") ?>