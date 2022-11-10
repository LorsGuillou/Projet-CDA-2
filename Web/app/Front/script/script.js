// Gestion du menu burger

const hamburger = document.querySelector(".hamburger");
const navMenu = document.querySelector(".nav-menu");

// Dévoiler / cacher la navigation
hamburger.addEventListener("click", (e) => {
    e.preventDefault();
    hamburger.classList.toggle("active");
    navMenu.classList.toggle("active");
});