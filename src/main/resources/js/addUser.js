async function createNewUser(user) {
    const response = await fetch("/api/admin", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(user)
    });


}

async function checkUserExists(email) {
    const response = await fetch(`/api/admin`);
    const data = await response.json();
    return data && data.email === email;
}

async function addNewUserForm() {
    const newUserForm = document.getElementById("newUser");
    const errorMessages = document.getElementById("errorMessages");

    newUserForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        errorMessages.innerHTML = '';

        // Получаем значения полей формы
        const nameField = newUserForm.querySelector("#name");
        const ageField = newUserForm.querySelector("#age");
        const emailField = newUserForm.querySelector("#email");
        const passwordField = newUserForm.querySelector("#password");

        const rolesSelected = document.getElementById("roles");

        let roles = [];
        for (let option of rolesSelected.selectedOptions) {
            if(option.value === ROLE_USER.roleName) {
                roles.push(ROLE_USER);
            } else if (option.value === ROLE_ADMIN.roleName) {
                roles.push(ROLE_ADMIN);
            }
        }

        const name = nameField.value.trim();
        const age = ageField.value.trim();
        const email = emailField.value.trim();
        const password = passwordField.value.trim();

        let isValid = true;
        if (!name) {
            nameField.classList.add("invalid-input");
            isValid = false;
        } else {
            nameField.classList.remove("invalid-input");
        }

        if (!age) {
            ageField.classList.add("invalid-input");
            isValid = false;
        } else {
            ageField.classList.remove("invalid-input");
        }

        if (!email) {
            emailField.classList.add("invalid-input");
            isValid = false;
        } else {
            emailField.classList.remove("invalid-input");
        }

        if (!password) {
            passwordField.classList.add("invalid-input");
            isValid = false;
        } else {
            passwordField.classList.remove("invalid-input");
        }

        if (!isValid) {
            document.getElementById("errorMessages").innerHTML = '<div class="bg-danger text-white mt-3">Please fill out all fields.</div>';
            return;
        }

        if (age <= 0) {
            ageField.classList.add("invalid-input");
            document.getElementById("errorMessages").innerHTML = '<div class="bg-danger text-white mt-3">Age must be a positive number.</div>';
            return;
        } else {
            ageField.classList.remove("invalid-input");
        }


        const emailPattern = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,3}$/;
        if (!email.match(emailPattern)) {
            emailField.classList.add("invalid-input");
            document.getElementById("errorMessages").innerHTML = '<div class="bg-danger text-white mt-3">Invalid email format.</div>';
            return;
        } else {
            emailField.classList.remove("invalid-input");
        }

        const userExists = await checkUserExists(email);
        if (userExists) {
            emailField.classList.add("invalid-input");
            document.getElementById("errorMessages").innerHTML = '<div class="bg-danger text-white mt-3">User with this email already exists.</div>';
            return;
        }


        const newUserData = {
            name: name,
            age: age,
            email: email,
            password: password,
            roles: roles
        };

        if (roles.length === 0) {
            newUserData.roles = [ROLE_USER];
        } else {
            newUserData.roles = roles;
        }

        try {

            await createNewUser(newUserData);


            newUserForm.reset();


            document.querySelector('a#show-users-table').click();
            await fillTableOfAllUsers();
        } catch (error) {

            errorMessages.innerHTML = `<div class="bg-danger text-white mt-3">${error.message}</div>`;
        }
    });
}

addNewUserForm();
