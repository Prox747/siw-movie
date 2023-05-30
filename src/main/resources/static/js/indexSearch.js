//So che usare il DB era MOLTO più veloce ma è difficile perchè dovrei
//chiamare una funzione su spring boot che mi ritorna una lista di film
//poi dovrei prima di passarli a js, convertirli in JSON, e poi
//ricostruire gli oggetti in hmtl, è molto codice
function filterMoviesBasedOnSearchText() {
    const input = document.getElementById("movieSearchInput");
    const searchText = input.value.toUpperCase();
    const movies = document.getElementById("movie-carousel").children;
    for (let i = 0; i < movies.length; i++) {
        const title = movies[i].getAttribute("data-title");

        if (title.toUpperCase().includes(searchText)) {
            movies[i].style.display = "";
        } else {
            movies[i].style.display = "none";
        }
    }
}

function filterArtistsBasedOnSearchText() {
    const input = document.getElementById("artistSearchInput");
    const searchText = input.value.toUpperCase();
    const artists = document.getElementById("artist-carousel").children;
    for (let i = 0; i < artists.length; i++) {
        const name = artists[i].getAttribute("data-name");
        const surname = artists[i].getAttribute("data-surname");

        if (name.toUpperCase().includes(searchText) || surname.toUpperCase().includes(searchText)) {
            artists[i].style.display = "";
        } else {
            artists[i].style.display = "none";
        }
    }
}