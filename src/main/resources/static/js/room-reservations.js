fetch("http://localhost:8080/rooms/reservations/approved")
    .then(response => response.json())
    .then(result => {
        for (let reservation of result) {
            let table = ``;

            table += `<tr className="text-center">
                <td>${reservation.userFullName}</td>
                <td>${reservation.dateOfReservation}</td>
                <td>${reservation.room.name}</td>
            </tr>`;

            $('#approvedRes').append(table);

    }})
    .catch(error => console.log('error', error));

fetch("http://localhost:8080/rooms/reservations/denied")
    .then(response => response.json())
    .then(result => {
        for (let reservation of result) {
            let table = ``;

            table += `<tr className="text-center">
                <td>${reservation.userFullName}</td>
                <td>${reservation.dateOfReservation}</td>
                <td>${reservation.room.name}</td>
            </tr>`;

            $('#deniedRes').append(table);

        }})
    .catch(error => console.log('error', error));