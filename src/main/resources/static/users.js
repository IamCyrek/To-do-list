let users = [];
let filtrationQuery = "";
let sortingQuery = "";

(getUsers = async () => {
    let response = await fetch("api/users");
    let json = await response.json();

    createRows(json);
})();

removeAllRows = () => {
    let rows = document.getElementsByClassName("divRow");

    while (rows[0]) {
        rows[0].parentNode.removeChild(rows[0]);
    }

    users = [];
};

userHasAllProperties = (user) => {
    return !(!user.hasOwnProperty("id") ||
        !user.hasOwnProperty("name") ||
        !user.hasOwnProperty("createdAt"));
};

createRows = (usersOrJson) => {
    for (let i = 0; i < usersOrJson.length; i++)
        createNewRow(document.getElementById("userList"), usersOrJson[i]);
};

createNewRow = (userList, user) => {
    if (!userHasAllProperties(user))
        return;

    users.push(user);

    Number.prototype.padLeft = function(base, chr) {
        let len = (String(base || 10).length - String(this).length) + 1;
        return len > 0 ? new Array(len).join(chr || '0') + this : this;
    };

    let date = new Date((new Date(user.createdAt)).getTime() - (new Date()).getTimezoneOffset() * 60000);
    let createdAt =
        [date.getHours().padLeft(), date.getMinutes().padLeft(), date.getSeconds().padLeft()].join(':') + ' ' +
        [date.getDate().padLeft(), (date.getMonth() + 1).padLeft(), date.getFullYear().padLeft()].join('.');

    let divRow = document.createElement("div");
    divRow.setAttribute("data-userid", user.id);
    divRow.className = "divRow";
    divRow.innerHTML =
        "<div class='divUser'>" + user.name + "</div>" +
        "<div class='divTime'>" + createdAt + "</div>";

    let span = document.createElement("SPAN");
    let txt = document.createTextNode("\u00D7");
    span.className = "close";
    span.appendChild(txt);
    span.onclick = function() {
        let userid = parseInt(this.parentElement.getAttribute("data-userid"));

        fetch("api/users?id=" + userid, {
            method: "DELETE"
        }).then((response) => {
            return response.json();
        }).then((myJson) => {
            if (myJson.hasOwnProperty("deleted") && myJson.deleted === true) {
                users = users.filter(obj => obj.id !== userid);
                this.parentElement.parentElement.removeChild(this.parentElement);
            } else {
                showDialog("Delete failed!");
            }
        });
    };

    divRow.appendChild(span);

    divRow.addEventListener('click', function(ev) {
        document.getElementById("updateUserModal").style.display = "block";

        document.getElementById("updateUserNameInput").value = user.name;

        let d = new Date((new Date(user.createdAt)).getTime() - (new Date()).getTimezoneOffset() * 60000);
        document.getElementById("updateUserCreationTimeInput").value =
            [d.getFullYear().padLeft(), (d.getMonth() + 1).padLeft(), d.getDate().padLeft()].join("-") + 'T' +
            [d.getHours().padLeft(), d.getMinutes().padLeft(), d.getSeconds().padLeft()].join(":");

        document.getElementById("updateUser").onclick = function () {
            let name = document.getElementById("updateUserNameInput");
            let email = document.getElementById("updateUserEmailInput");
            let password = document.getElementById("updateUserPasswordInput");

            if (nameValidation(name.value.toString()))
                return;

            if (users.find(obj => {return obj.name === name.value && obj.id !== user.id}) !== undefined) {
                showDialog("This user name is already in use!");
                return;
            }

            if (emailValidation(email.value.toString()))
                return;

            if (passwordValidation(password.value.toString()))
                return;

            fetch("api/users", {
                method: "PUT",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    id: user.id,
                    name: name.value,
                    email: email.value,
                    password: password.value,
                    createdAt: user.createdAt
                })
            })
            .then((response) => {
                return response.json();
            })
            .then(async (myJson) => {
                if (myJson.hasOwnProperty("updated") && myJson.updated === true) {
                    removeAllRows();
                    await applyForms();
                } else {
                    showDialog("Update failed!");
                }

                email.value = "";
                password.value = "";
            });

            closeUpdateUserModal();
        }
    });

    userList.appendChild(divRow);
};



showDialog = (text) => {
    document.getElementById("closeDialogMessage").innerHTML = text;

    let dialog = document.getElementById("dialog");
    document.getElementById("closeDialogButton").onclick = () => {dialog.close()};

    dialog.show();
};

enableEnterAndSpaceKey = (event) => {
    if (event.keyCode === 13 || event.keyCode === 32)
        onAddClick();
};

nameValidation = (name) => {
    if (name !== undefined && (name.length < 3 || name.length > 255)) {
        showDialog("Incorrect name length!");
        return true;
    }

    return false;
};

emailValidation = (email) => {
    if (email !== undefined && (email.length < 3 || email.length > 63)) {
        showDialog("Incorrect email length!");
        return true;
    }

    return false;
};

passwordsValidation = (password, repeatPassword) => {
    if (password !== undefined && repeatPassword !== undefined &&
        password !== repeatPassword) {
        showDialog("Passwords do not match!");
        return true;
    }

    return false;
};

passwordValidation = (password) => {
    if (password !== undefined && password.length < 8 || password.length > 63) {
        showDialog("Incorrect password length!");
        return true;
    }

    return false;
};

onAddClick = () => {
    let name = document.getElementById("userNameInput");
    let email = document.getElementById("userEmailInput");
    let password = document.getElementById("userPasswordInput");
    let repeatPassword = document.getElementById("userRepeatPasswordInput");

    if (nameValidation(name.value.toString()))
        return;

    if (users.find(obj => {return obj.name === name.value}) !== undefined) {
        showDialog("This user name is already in use!");
        return;
    }

    if (emailValidation(email.value.toString()))
        return;

    if (passwordsValidation(password.value, repeatPassword.value))
        return;

    if (passwordValidation(password.value.toString()))
        return;

    fetch("api/users", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            name: name.value,
            email: email.value,
            password: password.value,
            createdAt: +new Date()
        })
    })
    .then((response) => {
        return response.json();
    })
    .then((myJson) => {
        createNewRow(document.getElementById("userList"), myJson);
    });

    name.value = "";
    email.value = "";
    password.value = "";
    repeatPassword.value = "";
};



openFiltrationForm = () => {
    document.getElementById("filtration").style.display = "block";
};

openSortingForm = () => {
    document.getElementById("sorting").style.display = "block";
};

disableEnterKey = (event) => {
    if (event.keyCode === 13)
        return false;
};

applyFiltrationForm = async () => {
    removeAllRows();

    filtrationQuery = "";

    let name = document.getElementById("name").value;
    if (name.length > 0)
        filtrationQuery += "name=" + name;

    let startTime = document.getElementById("startTime").value;
    if (startTime.length > 0)
        filtrationQuery += (filtrationQuery.length > 0 ? "&" : "") + "startTime=" + startTime + ":00.000";

    let endTime = document.getElementById("endTime").value;
    if (endTime.length > 0)
        filtrationQuery += (filtrationQuery.length > 0 ? "&" : "") + "endTime=" + endTime + ":59.999";

    await applyForms();
};

createSortingQuerySubString = (elementId) => {
    let sortingSelect = document.getElementById(elementId);
    let sorting = parseInt(sortingSelect.options[sortingSelect.selectedIndex].value);

    if (sorting === 0)
        return;

    if (sorting === 1)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=name";

    if (sorting === 2)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=name,desc";

    if (sorting === 3)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=createdAt";

    if (sorting === 4)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=createdAt,desc";
};

applySortingForm = async () => {
    removeAllRows();

    sortingQuery = "";

    createSortingQuerySubString("firstSorting");
    createSortingQuerySubString("secondSorting");

    await applyForms();
};

applyForms = async () => {
    let query = "";

    if (sortingQuery.length > 0)
        query = sortingQuery;

    if (filtrationQuery.length > 0)
        query += (query.length > 0 ? "&" : "?") + filtrationQuery;

    let response = await fetch("api/users" + query);
    let json = await response.json();

    createRows(json);
};

closeFiltrationForm = () => {
    document.getElementById("filtration").style.display = "none";
};

closeSortingForm = () => {
    document.getElementById("sorting").style.display = "none";
};

closeUpdateUserModal = () => {
    document.getElementById("updateUserModal").style.display = "none";
};

openUpdatePasswordModal = () => {
    document.getElementById("updatePasswordModal").style.display = "block";
};

closeUpdatePasswordModal = () => {
    document.getElementById("updatePasswordModal").style.display = "none";
};

updatePassword = () => {
    let email = document.getElementById("updateEmailInput");
    let oldPassword = document.getElementById("oldPasswordInput");
    let newPassword = document.getElementById("newPasswordInput");
    let repeatPassword = document.getElementById("repeatPasswordInput");

    if (emailValidation(email.value.toString()))
        return;

    if (passwordsValidation(newPassword.value, repeatPassword.value))
        return;

    if (passwordValidation(oldPassword.value.toString()) ||
        passwordValidation(newPassword.value.toString()))
        return;

    fetch("api/users/updatePassword", {
        method: "PUT",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            email: email.value,
            "oldPassword": oldPassword.value,
            "newPassword": newPassword.value
        })
    })
    .then((response) => {
        return response.json();
    })
    .then((myJson) => {
        if (myJson.hasOwnProperty("updated") && myJson.updated === true)
            return;

        showDialog("Update password failed!");
    });

    closeUpdatePasswordModal();

    email.value = "";
    oldPassword.value = "";
    newPassword.value = "";
    repeatPassword.value = "";
};

window.onclick = (event) => {
    if (event.target === document.getElementById("updateUserModal")) {
        document.getElementById("updateUserModal").style.display = "none";
    } else if (event.target === document.getElementById("updatePasswordModal")) {
        document.getElementById("updatePasswordModal").style.display = "none";
    }
};
