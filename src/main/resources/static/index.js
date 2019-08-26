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
        !task.hasOwnProperty("isRemoved") ||
        !task.hasOwnProperty("userId"));
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

    let date = new Date((new Date(task.creationTime)).getTime() - (new Date()).getTimezoneOffset() * 60000);
    let creationTime =
        [date.getHours().padLeft(), date.getMinutes().padLeft(), date.getSeconds().padLeft()].join(':')+ ' ' +
        [date.getDate().padLeft(), (date.getMonth() + 1).padLeft(), date.getFullYear().padLeft()].join('.');

    let divRow = document.createElement("div");
    divRow.setAttribute("data-taskid", task.id);
    divRow.setAttribute("data-userid", task.userId);
    divRow.className = "divRow";
    divRow.innerHTML =
        "<div class='divTask'>" + task.content + "</div>" +
        "<div class='divPriority'>" + priority + "</div>" +
        "<div class='divTime'>" + creationTime + "</div>";
    if (task.isRemoved)
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

            fetch("api/tasks", {
                method: "PUT",
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    id: task.id,
                    content: task.content,
                    priority: task.priority,
                    creationTime: task.creationTime,
                    isRemoved: !task.isRemoved,
                    userId: task.userId
                })
            }).then((response) => {
                return response.json();
            }).then((myJson) => {
                if (taskHasAllProperties(myJson)) {
                    task.content = myJson.content;
                    task.priority = myJson.priority;
                    task.creationTime = myJson.creationTime;
                    task.isRemoved = myJson.isRemoved;
                    task.userId = myJson.userId;

                    if (ev.target.parentElement.classList.contains("checked") && !task.isRemoved)
                        ev.target.parentElement.classList.remove("checked");
                    else if (task.isRemoved)
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
            isRemoved: false,
            userId: 1
        })
    }).then((response) => {
        return response.json();
    }).then((myJson) => {
        createNewRow(document.getElementById("taskList"), myJson, true);
    });

    document.getElementById("myInput").value = "";
};



removeAllRows = () => {
    let rows = document.getElementsByClassName("divRow");

    while(rows[0]) {
        rows[0].parentNode.removeChild(rows[0]);
    }
};

createRows = () => {
    for (let i = 0; i < tasks.length; i++)
        createNewRow(document.getElementById("taskList"), tasks[i], false);
};

let isAscSort = true;

taskHeaderAscSort = (a, b) => {
    if (a.content > b.content)
        return 1;

    if (a.content < b.content)
        return -1;

    return 0;
};

divTaskHeaderClick = () => {
    removeAllRows();

    if (!document.getElementById("divTaskHeader").classList.contains("filter")) {
        document.getElementById("divTaskHeader").classList.add("filter");
        document.getElementById("divPriorityHeader").classList.remove("filter");
        document.getElementById("divTimeHeader").classList.remove("filter");

        isAscSort = true;
    } else {
        isAscSort = !isAscSort;
    }

    if (isAscSort)
        tasks.sort(taskHeaderAscSort);
    else
        tasks.sort((a, b) => taskHeaderAscSort(a, b) * -1);

    createRows();
};

priorityHeaderAscSort = (a, b) => {
    if (a.priority > b.priority)
        return 1;

    if (a.priority < b.priority)
        return -1;

    return 0;
};

divPriorityHeaderClick = () => {
    removeAllRows();

    if (!document.getElementById("divPriorityHeader").classList.contains("filter")) {
        document.getElementById("divTaskHeader").classList.remove("filter");
        document.getElementById("divPriorityHeader").classList.add("filter");
        document.getElementById("divTimeHeader").classList.remove("filter");

        isAscSort = true;
    } else {
        isAscSort = !isAscSort;
    }

    if (isAscSort)
        tasks.sort(priorityHeaderAscSort);
    else
        tasks.sort((a, b) => priorityHeaderAscSort(a, b) * -1);

    createRows();
};

timeHeaderAscSort = (a, b) => {
    if (a.creationTime > b.creationTime)
        return 1;

    if (a.creationTime < b.creationTime)
        return -1;

    return 0;
};

divTimeHeaderClick = () => {
    removeAllRows();

    if (!document.getElementById("divTimeHeader").classList.contains("filter")) {
        document.getElementById("divTaskHeader").classList.remove("filter");
        document.getElementById("divPriorityHeader").classList.remove("filter");
        document.getElementById("divTimeHeader").classList.add("filter");

        isAscSort = true;
    } else {
        isAscSort = !isAscSort;
    }

    if (isAscSort)
        tasks.sort(timeHeaderAscSort);
    else
        tasks.sort((a, b) => timeHeaderAscSort(a, b) * -1);

    createRows();
};
