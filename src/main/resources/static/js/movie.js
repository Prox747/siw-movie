// chiama la funzione appena si loada l'html e tutte le risorse esterne
window.onload = function() {
    //serve a mandare l'utente alla home
    //quando va indietro nel browser
    //così non si bugga se hai appena fatto operazioni di modifica
    window.history.pushState(null, null, window.location.href);
    window.addEventListener('popstate', function(event) {
        window.location.replace("/");
    });

    setHeightForReviewsContainer();
};

// setto l'altezza del container delle recensioni in base all'altezza della card
//oppure se non ci sono recensioni, setto l'altezza a 250px
function setHeightForReviewsContainer() {
    const reviewsIsEmpty = JSON.parse(document.getElementById("reviewsIsEmpty").getAttribute("data-isEmpty"));
    const reviewsContainer = document.getElementsByClassName("reviews-container")[0];
    const card = document.getElementsByClassName("card")[0];
    if (reviewsIsEmpty) {
        reviewsContainer.style.height = "150px";
        return;
    }
    reviewsContainer.style.height = card.clientHeight + "px";
}