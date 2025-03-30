/**
 * Handles the keyboard event. In case of ENTER (13) it saves a new todoItem or updates
 * an existing one
 * @param e keyboard event
 */
function handleEnterKey(e) {
    if (e.keyCode === 13) {
        e.preventDefault(); // Ensure it is only this code that run
        let listName = document.getElementById('listNameTextField').value
        // Clear input field!
        let listNameTextField = $("#listNameTextField");
        listNameTextField.val('');

        // Check if we are editing or not!
        let isEditing = listNameTextField.attr("isEditing");

        if (isEditing) {
            // clear editing flag.
            listNameTextField.removeAttr("isEditing");
            let itemId = listNameTextField.attr("editingItemId");
            listNameTextField.removeAttr("editingItemId");
            putEditTodoList(itemId, listName);
        } else {
            postNewTodoList(listName);
        }
    }
}

/**
 * Post a new todoList to the backend
 * @param listName the name of the list
 */
function postNewTodoList(listName) {

    if (listName) {

        $.ajax({
            type: "POST",
            url: "/api/v1/todolist-names",
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify({id: getUUID(), name: listName}),
            success: function (data) {
                loadLists();
            },
        });
    }
}

/**
 * Updates the todoList in the backend
 * @param listId the id of a list item
 * @param listName the new name of the list
 */
function putEditTodoList(listId, listName) {
    if (listName) {

        $.ajax({
            type: "PUT",
            url: "/api/v1/todolist-names/" + listId,
            headers: {
                "Content-Type": "application/json"
            },
            data: JSON.stringify({id: listId, name: listName}),
            success: function (data) {
                getAndCreateTodoListRow(data.id);

            },
            error: function (data) {
                console.log("error at ajax call:", data);
            }
        });
    }
}

/**
 * Load the todoLists from the backend and creates the list
 */
function loadLists() {
    $.get("/api/v1/todolist-names", function (data) {
        let cList = $('ul.todo-list');
        cList.empty();
        data.forEach(item => {

            let li = $('<li/>')
                .attr("id", "item" + item.listId)
                .appendTo(cList);
            console.log(item)
            createTodoListRow(li, item);

        });
    });
}

/**
 * Creates a row in the todoNameList
 * The line consists of an id,list name, a span with edit icon and delete icon
 * @param parent The parent HTML element
 * @param data consist of count, fromDate, listId, listName, toDate, fromDate
 *
 */
function createTodoListRow(parent, data) {
    let todoListRow = $('<div/>')
        .addClass('todo-list-row')
        .appendTo(parent)


    // List Name
    let todoTitle = $('<a/>')
        .attr("href", "/todo-items?listId=" + data.listId)
        .addClass('todo-list-name-item')
        .text(data.listName)
        .appendTo(todoListRow);

    // Actions
    let todoActions = $('<span/>')
        .addClass('todo-actions')
        .appendTo(todoListRow)

    // Edit icon
    let editAttr = $('<a/>')
        .attr("id", data.listId) // to know item id!
        .attr("onclick", "editTodoListName(this)")
        .appendTo(todoActions);

    let editIcon = $('<i/>')
        .addClass('material-icons')
        .text('edit')
        .appendTo(editAttr);

    // Delete icon
    let deleteAttr = $('<a/>')
        .attr("id", data.listId) // to know item id!
        .attr("onclick", "deleteTodoList(this)")
        .appendTo(todoActions);

    let deleteIcon = $('<i/>')
        .addClass('material-icons')
        .text('delete')
        .appendTo(deleteAttr);
}

/**
 * Deletes a todoList in the backend and deletes it from the list
 * @param ele one list item int the todoLists
 */
function deleteTodoList(ele) {
    let itemId = $(ele).attr("id"); // get the item id!
    $.ajax({
        type: "DELETE",
        url: "/api/v1/todolist-names/" + itemId,
        success: function (data) {
            let oldItem = $("#item" + itemId);
            cuteHide(oldItem);
        },
        error: function (data) {
        }
    });
}

/**
 * Take the List Name ("todo-list-name-item") from the list and copy it the the listNameTextField
 * @param ele one list item int the todoLists
 */
function editTodoListName(ele) {
    // first get item id
    let itemId = $(ele).attr("id");
    // then get list item we created before.
    let listItem = $("#item" + itemId);
    let titleLink = listItem.find(".todo-list-name-item");

    // set the text field
    let listNameTextField = $("#listNameTextField");
    listNameTextField.val(titleLink.text());
    // set the attribute that we are editing!
    listNameTextField.attr("isEditing", true);
    listNameTextField.attr("editingItemId", itemId);
}

/**
 * Get the todoList from the backend and updates the list
 * @param listId the id of one list item
 */
function getAndCreateTodoListRow(listId) {
    $.get("/api/v1/todolist-names/" + listId, function (data) {
        console.log("getAndCreateTodoListRow", listId, data);
        let newListItem = $('<li/>')
            .attr("id", "item" + data.listId);

        createTodoListRow(newListItem, data);

        // Replace the old one by the new one
        let oldListItem = $("#item" + data.listId);
        oldListItem.replaceWith(newListItem);
    });
}

function getUUID() {
    var result = '';
    var hexcodes = "0123456789abcdef".split("");

    for (var index = 0; index < 32; index++) {
        var value = Math.floor(Math.random() * 16);

        switch (index) {
            case 8:
                result += '-';
                break;
            case 12:
                value = 4;
                result += '-';
                break;
            case 16:
                value = value & 3 | 8;
                result += '-';
                break;
            case 20:
                result += '-';
                break;
        }
        result += hexcodes[value];
    }
    return result;
}
