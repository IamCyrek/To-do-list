(getTasks = async () => {
    let response = await fetch("api/tasks");
    let json = await response.json();

    let myUL = document.getElementById("myUL");
    while (myUL.firstChild) {
        myUL.removeChild(myUL.firstChild)
    }

    for (let i = 0; i < json.length; i++)
        myUL.appendChild(createNewLI(json[i]));
})();

createNewLI = (task) => {
    let li = document.createElement("li");
    li.setAttribute("data-taskid", task.id);
    li.innerHTML = task.content;
    if (task.removed)
        li.classList.add("checked");

    let span = document.createElement("SPAN");
    let txt = document.createTextNode("\u00D7");
    span.className = "close";
    span.appendChild(txt);
    span.onclick = function() {
        fetch("api/tasks?id=" + this.parentElement.getAttribute("data-taskid"), {
            method: "DELETE"
        }).then((response) => {
            return response.json();
        }).then((myJson) => {
            console.log(myJson);
            this.parentElement.parentElement.removeChild(this.parentElement);
        });
    };
    li.appendChild(span);

    li.addEventListener('click', function(ev) {
        if (ev.target.tagName === 'LI') {
            let removed = true;
            if (ev.target.classList.contains("checked"))
                removed = false;

            fetch("api/tasks?id=" + this.getAttribute("data-taskid"), {
                method: "PUT",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({content: this.textContent.slice(0, -1), removed: removed})
            }).then((response) => {
                return response.json();
            }).then((myJson) => {
                if (myJson.removed)
                    ev.target.classList.add("checked");
                else
                    ev.target.classList.remove("checked");
            });
        }
    });

    return li;
};

onAddClick = () => {
    let inputValue = document.getElementById("myInput").value;

    if (inputValue.toString().length < 3 || inputValue.toString().length > 50) {
        alert("Incorrect task length!");
        return;
    }

    fetch("api/tasks", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({content: inputValue, removed: false})
    }).then((response) => {
        return response.json();
    }).then((myJson) => {
        document.getElementById("myUL").appendChild(createNewLI(myJson));
    });

    document.getElementById("myInput").value = "";
};
