let tasks = [];

(getTasks = async () => {
    let response = await fetch("api/tasks");
    let json = await response.json();

    for (let i = 0; i < json.length; i++)
        createNewRow(document.getElementById("taskList"), json[i], true);
})();



taskHasAllProperties = (task) => {
    return !(!task.hasOwnProperty("id") ||
        !task.hasOwnProperty("content") ||
        !task.hasOwnProperty("priority") ||
        !task.hasOwnProperty("creationTime") ||
        !task.hasOwnProperty("removed"));
};

createNewRow = (taskList, task, isAdd) => {
    if (!taskHasAllProperties(task))
        return;

    if (isAdd)
        tasks.push(task);

    let priority = "<span style='color:red'>Critical</span>";
    if (task.priority === 0)
        priority = "<span style='color:green'>Low</span>";
    else if (task.priority === 1)
        priority = "<span style='color:blue'>Normal</span>";
    else if (task.priority === 2)
        priority = "<span style='color:yellow'>High</span>";

    Number.prototype.padLeft = function(base, chr) {
        let len = (String(base || 10).length - String(this).length) + 1;
        return len > 0 ? new Array(len).join(chr || '0') + this : this;
    };

    let date = new Date(task.creationTime);
    let creationTime =
        [date.getHours().padLeft(), date.getMinutes().padLeft(), date.getSeconds().padLeft()].join(':')+ ' ' +
        [date.getDate().padLeft(), (date.getMonth() + 1).padLeft(), date.getFullYear().padLeft()].join('.');

    let divRow = document.createElement("div");
    divRow.setAttribute("data-taskid", task.id);
    divRow.className = "divRow";
    divRow.innerHTML =
        "<div class='divTask'>" + task.content + "</div>" +
        "<div class='divPriority'>" + priority + "</div>" +
        "<div class='divTime'>" + creationTime + "</div>";
    if (task.removed)
        divRow.classList.add("checked");

    let span = document.createElement("SPAN");
    let txt = document.createTextNode("\u00D7");
    span.className = "close";
    span.appendChild(txt);
    span.onclick = function() {
        let taskid = parseInt(this.parentElement.getAttribute("data-taskid"));

        fetch("api/tasks?id=" + taskid, {
            method: "DELETE"
        }).then((response) => {
            return response.json();
        }).then((myJson) => {
            if (myJson.hasOwnProperty("deleted") && myJson.deleted === true) {
                tasks = tasks.filter(obj => obj.id !== taskid);
                this.parentElement.parentElement.removeChild(this.parentElement);
            }
        });
    };
    divRow.appendChild(span);

    divRow.addEventListener('click', function(ev) {
        if (ev.target.tagName === 'DIV') {

            let task = tasks.find(obj => obj.id === parseInt(this.getAttribute("data-taskid")));

            fetch("api/tasks?id=" + task.id, {
                method: "PUT",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    content: task.content,
                    priority: task.priority,
                    creationTime: task.creationTime,
                    removed: !task.removed
                })
            }).then((response) => {
                return response.json();
            }).then((myJson) => {
                if (taskHasAllProperties(myJson)) {
                    task.content = myJson.content;
                    task.priority = myJson.priority;
                    task.creationTime = myJson.creationTime;
                    task.removed = myJson.removed;

                    if (ev.target.parentElement.classList.contains("checked") && !task.removed)
                        ev.target.parentElement.classList.remove("checked");
                    else if (task.removed)
                        ev.target.parentElement.classList.add("checked");
                }
            });
        }
    });

    taskList.appendChild(divRow);
};



showDialog = (text) => {
    document.getElementById("closeDialogMessage").innerHTML = text;

    let dialog = document.getElementById("dialog");
    document.getElementById("closeDialogButton").onclick = () => {dialog.close()};

    dialog.show();
};

onAddClick = () => {
    let prioritySelect = document.getElementById("addPrioritySelect");
    let priority = prioritySelect.options[prioritySelect.selectedIndex].value;
    let inputValue = document.getElementById("myInput").value;

    if (inputValue.toString().length < 3 || inputValue.toString().length > 255) {
        showDialog("Incorrect task length!");
        return;
    }

    if (tasks.find(obj => {return obj.content === inputValue}) !== undefined) {
        showDialog("This task name is already in use!");
        return;
    }

    fetch("api/tasks", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            content: inputValue,
            priority: priority,
            creationTime: +new Date(),
            removed: false
        })
    }).then((response) => {
        return response.json();
    }).then((myJson) => {
        createNewRow(document.getElementById("taskList"), myJson, true);
    });

    document.getElementById("myInput").value = "";
};
