async function sendDataEditUser(user) {
    await fetch("/api/admin", {
        method: "PUT",
        headers: { 'Content-type': 'application/json' },
        body: JSON.stringify(user)
    });
}

const modalEdit = document.getElementById("editModal");

let initialRoles = [];

async function EditModalHandler() {
    await fillModal(modalEdit);


    const errorMessages = document.getElementById("editErrorMessages");
    errorMessages.innerHTML = "";


    const inputFields = modalEdit.querySelectorAll(".form-control");
    inputFields.forEach(field => {
        field.classList.remove("invalid-input");
    });


    setInitialSelectedRoles();
}

function setInitialSelectedRoles() {
    const rolesSelected = document.getElementById("rolesEdit");
    const userRoles = getUserRoles();

    for (let option of rolesSelected.options) {
        if (userRoles.includes(option.value)) {
            option.classList.add("selected-role");
        }
    }
}



modalEdit.addEventListener("submit", async function(event) {
    event.preventDefault();

    const rolesSelected = document.getElementById("rolesEdit");

    let roles = [];
    for (let option of rolesSelected.selectedOptions) {
        if (option.value === ROLE_USER.roleName) {
            roles.push(ROLE_USER);
        } else if (option.value === ROLE_ADMIN.roleName) {
            roles.push(ROLE_ADMIN);
        }
    }

    const id = document.getElementById("idEdit").value;
    const name = document.getElementById("nameEdit").value.trim();
    const age = document.getElementById("ageEdit").value.trim();
    const email = document.getElementById("emailEdit").value.trim();
    const password = document.getElementById("passwordEdit").value.trim();

    // Очистка предыдущих ошибок
    const errorMessages = document.getElementById("editErrorMessages");
    errorMessages.innerHTML = "";


    let isValid = true;

    if (!name) {
        document.getElementById("nameEdit").classList.add("invalid-input");
        errorMessages.innerHTML += '<div class="bg-danger text-white mt-3">Name cannot be empty.</div>';
        isValid = false;
    }

    if (!age) {
        document.getElementById("ageEdit").classList.add("invalid-input");
        errorMessages.innerHTML += '<div class="bg-danger text-white mt-3">Age cannot be empty.</div>';
        isValid = false;
    } else if (isNaN(age) || age <= 0 || age > 95) {
        document.getElementById("ageEdit").classList.add("invalid-input");
        errorMessages.innerHTML += '<div class="bg-danger text-white mt-3">Age must be a number between 1 and 95.</div>';
        isValid = false;
    }

    const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,3}$/;
    if (!email.match(emailPattern)) {
        document.getElementById("emailEdit").classList.add("invalid-input");
        errorMessages.innerHTML += '<div class="bg-danger text-white mt-3">Invalid email format.</div>';
        isValid = false;
    }

    if (!password) {
        document.getElementById("passwordEdit").classList.add("invalid-input");
        errorMessages.innerHTML += '<div class="bg-danger text-white mt-3">Password cannot be empty.</div>';
        isValid = false;
    }

    if (!isValid) {
        return;
    }


    if (roles.length === 0) {
        roles = initialRoles;
    }

    const user = {
        id: id,
        name: name,
        age: age,
        email: email,
        password: password,
        roles: roles
    };



    await sendDataEditUser(user);
    await fillTableOfAllUsers();

    const modalBootstrap = bootstrap.Modal.getInstance(modalEdit);
    modalBootstrap.hide();
});
