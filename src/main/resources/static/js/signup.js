document.getElementById('signupForm').addEventListener('submit', function(event) {
    event.preventDefault();

    var name = document.getElementById('name').value;
    var email = document.getElementById('email').value;
    var password = document.getElementById('password').value;

		let user = {
				name: name,
				email: email,
				password: password
		};

    fetch('http://localhost:8080/api/users/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            alert(data.error);
        } else {
            console.log('Signup GOOOOD');
            console.log(JSON.stringify(user));
            alert(data.message);
            window.location.href = '/login.html';
        }
    })
    .catch((error) => {
        console.error('Error:', error);
    });
});