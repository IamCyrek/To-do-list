let tasks = [];
let filtrationQuery = "";
let sortingQuery = "";

(getTasks = async () => {
    let response = await fetch("api/tasks");
    let json = await response.json();

    createRows(json);
})();

removeAllRows = () => {
    let rows = document.getElementsByClassName("divRow");

    while (rows[0]) {
        rows[0].parentNode.removeChild(rows[0]);
    }

    tasks = [];
};

taskHasAllProperties = (task) => {
    return !(!task.hasOwnProperty("id") ||
        !task.hasOwnProperty("content") ||
        !task.hasOwnProperty("priority") ||
        !task.hasOwnProperty("creationTime") ||
        !task.hasOwnProperty("isRemoved") ||
        !task.hasOwnProperty("userId"));
};

createRows = (tasksOrJson) => {
    for (let i = 0; i < tasksOrJson.length; i++)
        createNewRow(document.getElementById("taskList"), tasksOrJson[i]);
};

createNewRow = (taskList, task) => {
    if (!taskHasAllProperties(task))
        return;

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
            } else {
                showDialog("Delete failed!");
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
            })
            .then((response) => {
                return response.json();
            })
            .then((myJson) => {
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

enableEnterAndSpaceKey = (event) => {
    if (event.keyCode === 13 || event.keyCode === 32)
        onAddClick();
};

taskValidation = (task) => {
    if (task !== undefined && (task.length < 3 || task.length > 255)) {
        showDialog("Incorrect task length!");
        return true;
    }

    return false;
};

onAddClick = () => {
    let prioritySelect = document.getElementById("addPrioritySelect");
    let priority = prioritySelect.options[prioritySelect.selectedIndex].value;
    let inputValue = document.getElementById("taskHeaderInput");

    if (taskValidation(inputValue.value.toString()))
        return;

    if (tasks.find(obj => {return obj.content === inputValue.value}) !== undefined) {
        showDialog("This task name is already in use!");
        return;
    }

    fetch("api/tasks", {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            content: inputValue.value,
            priority: priority,
            creationTime: +new Date(),
            isRemoved: false,
            userId: 1
        })
    })
    .then((response) => {
        return response.json();
    })
    .then((myJson) => {
        createNewRow(document.getElementById("taskList"), myJson);
    });

    inputValue.value = "";
    prioritySelect.selectedIndex = 0;
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

    let content = document.getElementById("content").value;
    if (content.length > 0)
        filtrationQuery += "content=" + content;

    let prioritySelect = document.getElementById("priority");
    let priority = prioritySelect.options[prioritySelect.selectedIndex].value - 1;
    if (priority !== -1)
        filtrationQuery += (filtrationQuery.length > 0 ? "&" : "") + "priority=" + priority;

    let startTime = document.getElementById("startTime").value;
    if (startTime.length > 0)
        filtrationQuery += (filtrationQuery.length > 0 ? "&" : "") + "startTime=" + startTime + ":00.000";

    let endTime = document.getElementById("endTime").value;
    if (endTime.length > 0)
        filtrationQuery += (filtrationQuery.length > 0 ? "&" : "") + "endTime=" + endTime + ":59.999";

    let isRemovedSelect = document.getElementById("isRemoved");
    let isRemoved = isRemovedSelect.options[isRemovedSelect.selectedIndex].value;
    if (parseInt(isRemoved) === 1)
        filtrationQuery += (filtrationQuery.length > 0 ? "&" : "") + "isRemoved=" + true;
    else if (parseInt(isRemoved) === 2)
        filtrationQuery += (filtrationQuery.length > 0 ? "&" : "") + "isRemoved=" + false;

    await applyForms();
};

createSortingQuerySubString = (elementId) => {
    let sortingSelect = document.getElementById(elementId);
    let sorting = parseInt(sortingSelect.options[sortingSelect.selectedIndex].value);

    if (sorting === 0)
        return;

    if (sorting === 1)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=content";

    if (sorting === 2)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=content,desc";

    if (sorting === 3)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=priority";

    if (sorting === 4)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=priority,desc";

    if (sorting === 5)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=creationTime";

    if (sorting === 6)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=creationTime,desc";

    if (sorting === 7)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=isRemoved";

    if (sorting === 8)
        sortingQuery += (sortingQuery.length > 0 ? "&" : "?") + "sort=isRemoved,desc";
};

applySortingForm = async () => {
    removeAllRows();

    sortingQuery = "";

    createSortingQuerySubString("firstSorting");
    createSortingQuerySubString("secondSorting");
    createSortingQuerySubString("thirdSorting");
    createSortingQuerySubString("fourthSorting");

    await applyForms();
};

applyForms = async () => {
    let query = "";

    if (sortingQuery.length > 0)
        query = sortingQuery;

    if (filtrationQuery.length > 0)
        query += (query.length > 0 ? "&" : "?") + filtrationQuery;

    let response = await fetch("api/tasks" + query);
    let json = await response.json();

    createRows(json);
};

closeFiltrationForm = () => {
    document.getElementById("filtration").style.display = "none";
};

closeSortingForm = () => {
    document.getElementById("sorting").style.display = "none";
};
