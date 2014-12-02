/*jslint browser: true */
/*global google, alert, jQuery*/
var hall;     // Cinema hall
var hallctx;  // Canvas hall
var relSeatCoor = [];   // Relation between seat id and position
var takenSeats = [];
var resSeats = [];
var globCinema = '';    // Temp Variable

function clearForm() {
  $('input[name=name]').val('');
  $('input[name=email]').val('');
  $('input[name=cinemovie_id]').val('');
  relSeatCoor = [];
  $('#selected_seats').empty();
  resSeats = [];
}

// Click Event on seat
function reserveSeat(seat) {
  if (takenSeats.indexOf(seat) !== -1) {
    alert('Seat is taken, idiot!');
    return;
  }
  var s = relSeatCoor[seat];
  hallctx.fillStyle = "#0000FF";

  // Seat is already reserved
  if (resSeats.indexOf(seat) !== -1) {
    hallctx.fillStyle = "#00FF00";
    resSeats.splice(resSeats.indexOf(seat), 1);
    $('#s_' + seat).remove();
  } else {
    resSeats.push(seat);
    $('#selected_seats').append('<li id="s_' + seat + '">' + (seat + 1) + '</li>');
  }

  hallctx.fillRect(s.x, s.y, 20, 20);
}

function drawHall(seats) {
  takenSeats = seats;
  hallctx.clearRect(0, 0, hall.width, hall.height);
  // Screen
  hallctx.fillStyle = "#000000";
  hallctx.fillRect(30, 20, 440, 20);

  // Seats
  var seat = 0;
  var row = 0;
  for (seat = 0; seat < 52; seat++) {
    var seatX = 30 + (seat - (row * 13)) * 35,
      seatY = 60 + (row * 13) * 3;

    // Link seat id to x-y position
    relSeatCoor[seat] = {
      x: seatX,
      y: seatY
    };

    hallctx.fillStyle = "#00FF00";

    // Color seat red if it is taken
    if (takenSeats.indexOf(seat) !== -1) {
      hallctx.fillStyle = "#FF0000";
    }

    hallctx.fillRect(seatX, seatY, 20, 20);

    // Next row
    if ((seat - row * 13) === 12) {
      row = row + 1;
    }
  }

  // Legend
  hallctx.fillStyle = "#00FF00";
  hallctx.fillRect(30, 210, 20, 20);
  hallctx.fillText("available", 55, 225);
  hallctx.fillStyle = "#FF0000";
  hallctx.fillRect(135, 210, 20, 20);
  hallctx.fillText("not available", 160, 225);
  hallctx.fillStyle = "#0000FF";
  hallctx.fillRect(240, 210, 20, 20);
  hallctx.fillText("your reservation", 265, 225);
}

function clearCinemas() {
  $("#cinemaList").empty();
}
function clearMovies() {
  $("#movieList").empty();
}

function appendMovie(movie) {
  var li = $('<li id="c_' + movie.cinemovie_id + '">' + movie.name + '</li>');

  li.click(function (e) {
    e.preventDefault();
    clearForm();
    $('input[name=cinemovie_id]').val(movie.cinemovie_id);
    console.log(movie.seats);
    drawHall(movie.seats);
  });

  $("#movieList").append(li);
}

function loadMovies(cinema) {
  globCinema = cinema;
  var url = 'http://localhost:8080/Cinema/CinemaMovieAPI?' + cinema;
  $.getJSON(url, function (data) {
    $.each(data, function (i, item) {
      appendMovie(item);
    });
  })
    .fail(function () {
      $("#movieList").append('<li>Nothing</li>');
    });
}

function appendCinema(cinema) {
  var li = $('<li><h3>' + cinema.name + '</h3><span>' + cinema.vicinity + '</span></li>');

  li.click(function (e) {
    e.preventDefault();
    clearMovies();
    loadMovies(cinema.name);
  });

  $("#cinemaList").append(li);
}

function handlePlacesResponse(results, status) {
  if (status === google.maps.places.PlacesServiceStatus.OK) {
    clearCinemas();
    var i;

    var cinemaList = jQuery.map(results, function (item) {
      return item.name;
    });

    // Seed: Cinema and movies
    $.post('http://localhost:8080/Cinema/CinemaMovieAPI',
      JSON.stringify(cinemaList));

    for (i = 0; i < results.length; i++) {
      appendCinema(results[i]);
    }
  } else {
    $("#cinemaList").append('<li>Nothing</li>');
  }
}

function requestLocationInfo(pos) {
  var latlng = new google.maps.LatLng(pos.coords.latitude,
    pos.coords.longitude);

  var map = new google.maps.Map(document.getElementById('hiddenmap'), {
    mapTypeId: google.maps.MapTypeId.ROADMAP,
    center: latlng,
    zoom: 15
  });

  var request = {
    location: latlng,
    radius: '5000',
    types: ['movie_theater']
  };

  var service = new google.maps.places.PlacesService(map);
  service.search(request, handlePlacesResponse);
}

function getMousePos(hall, evt) {
  var rect = hall.getBoundingClientRect();

  return {
    x: evt.clientX - rect.left,
    y: evt.clientY - rect.top
  };
}

// Dom ready event
$(function () {
  //$.ajaxSetup({ cache: false });

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(requestLocationInfo);
  }

  // Init canvas hall
  hall = document.getElementById("hall");
  hall.width = 500;
  hall.height = 250;
  hallctx = hall.getContext("2d");

  // Canvas click event
  $(hall).click(function (e) {
    e.preventDefault();
    var position = getMousePos(hall, e);
    var x = position.x;
    var y = position.y;
    var seat;
    for (seat = 0; seat < relSeatCoor.length; seat++) {
      if (relSeatCoor[seat].x <= x && (relSeatCoor[seat].x + 20) >= x &&
          relSeatCoor[seat].y <= y && (relSeatCoor[seat].y + 20) >= y) {
        reserveSeat(seat);
      }
    }
  });

  // Submit res
  $('form').submit(function (e) {
    e.preventDefault();
    var request = {
      name: $('input[name=name]').val(),
      email: $('input[name=email]').val(),
      cinemovie_id: $('input[name=cinemovie_id]').val(),
      seats: resSeats
    };

    $.post('http://localhost:8080/Cinema/ReservationAPI',
      JSON.stringify(request),
      function (data) {
        alert('Your reservation id: ' + data.id);
        clearMovies();
        // Reload movies
        loadMovies(globCinema);
        // Color seats as taken
        $.each(resSeats, function (i, item) {
          var s = relSeatCoor[item];
          hallctx.fillStyle = "#FF0000";
          hallctx.fillRect(s.x, s.y, 20, 20);
        });
        clearForm();
      }, 'json')
      .fail(function (jqXHR) {
        var error = JSON.parse(jqXHR.responseText);
        alert(error.message);
      });
  });
});
