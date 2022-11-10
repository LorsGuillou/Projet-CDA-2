<?php include_once ("header.php") ?>

<main class="narrow">
    <?php if ($scraps != null) : ?>
       <?php foreach($scraps as $scrap) : ?>
        <p><?= $scrap['titre'] ?></p>
        <p><?= $scrap['libelle'] ?></p>
        <p><?= $scrap['description'] ?></p>
        <p><?= $scrap['prix'] ?></p>
        <p><?= $scrap['annee'] ?></p>
    <?php endforeach; ?>
       <?php else : ?>
        <p>La recherche n'a pas encore été faite.</p>
        <?php endif; ?>

</main>

<?php include_once ("footer.php") ?>