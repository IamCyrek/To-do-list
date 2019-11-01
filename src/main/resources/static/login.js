(() => {
    let url = new URL(window.location.href);
    if (url.searchParams.get("error") !== null) {
        document.getElementById("errorLabel").style.display = "block";
        document.getElementById("logoutLabel").style.display = "none";
    } else if (url.searchParams.get("logout") !== null) {
        document.getElementById("errorLabel").style.display = "none";
        document.getElementById("logoutLabel").style.display = "block";
    } else {
        document.getElementById("errorLabel").style.display = "none";
        document.getElementById("logoutLabel").style.display = "none";
    }
})();
