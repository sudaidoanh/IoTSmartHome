//Show menu

const menu = document.querySelector('.nav__menu');
const openMenu = document.querySelector('.btn__menu');
const closeMenu = document.querySelector('.btn-close');

openMenu.addEventListener('click', () => {
    menu.classList.toggle('active');
    openMenu.style.display = 'none';
    closeMenu.style.display = 'block';
    // menu.classList.add('animate__fadeInRight');
    // menu.classList.toggle('animate__fadeOutRight');
});

closeMenu.addEventListener('click', () => {
    menu.classList.toggle('active');
    closeMenu.style.display = 'none';
    openMenu.style.display = 'block';
    // menu.classList.toggle('animate__fadeInRight');
    // menu.classList.add('animate__fadeOutRight');
});