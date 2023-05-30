window.onload = function() {
    //serve a mandare l'utente alla home
    //quando va indietro nel browser
    //così non si bugga se hai appena fatto operazioni di modifica
    window.history.pushState(null, null, window.location.href);
    window.addEventListener('popstate', function (event) {
        window.location.replace("/");
    });
}