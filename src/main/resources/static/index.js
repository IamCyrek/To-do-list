let tasks = [];

(getTasks = async () => {
    let response = await fetch("api/tasks");
    let json = await response.json();

    let myUL = document.getElementById("myUL");
    while (myUL.firstChild) {
        myUL.removeChild(myUL.firstChild)
    }

    for (let i = 0; i < json.length; i++)
        createNewLI(myUL, json[i]);
})();

createNewLI = (myUL, task) => {
    if (!task.hasOwnProperty("id") ||
        !task.hasOwnProperty("content") ||
        !task.hasOwnProperty("removed"))
        return;

    tasks.push(task);

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

    myUL.appendChild(li);
};

onAddClick = () => {
    let inputValue = document.getElementById("myInput").value;

    if (inputValue.toString().length < 3 || inputValue.toString().length > 255) {
        alert("Incorrect task length!");
        return;
    }

    if (tasks.find(obj => {return obj.content === inputValue}) !== undefined) {
        let dialog = document.getElementById("dialog");
        document.getElementById("closeDialog").onclick = () => {dialog.close()};
        dialog.show();
        return;
    }

    fetch("api/tasks", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({content: inputValue, removed: false})
    }).then((response) => {
        return response.json();
    }).then((myJson) => {
        createNewLI(document.getElementById("myUL"), myJson);
    });

    document.getElementById("myInput").value = "";
};
