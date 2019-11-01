onSubmitClick = async () => {
    if (document.getElementById("password").value !== document.getElementById("matchingPassword").value) {
        debugger
        return;
    }

    fetch("/api/users/registration", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        })
    })
        .then((response) => {
            return response.json();
        })
        .then((myJson) => {
            console.log(myJson);
        }).catch(reason => console.log(reason));
    debugger
};

disableEnterKey = (event) => {
    if (event.keyCode === 13)
        return false;
};
