var selectedCinema = 0;
var cinemaSeating = [];
var c;
var ctx;
var relationMap = [];
var numberOfReservations=0;
function init(){
	c = document.getElementById("seating");
	c.width = 400;
	c.height = 400;
	ctx = c.getContext("2d");
}

function requestLocationInfo(position){
	var map;
	var service;
	var location = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
	//man braucht anscheinend ne map um die api zu benutzen? Ich versteck das ding einfach mal in nem div
	map = new google.maps.Map(document.getElementById('hiddenmap'), {
	    mapTypeId: google.maps.MapTypeId.ROADMAP,
	    center: location,
	    zoom: 15
	  });
	var request = {
	    location: location,
	    radius: '5000',
	    types: ['movie_theater']
	  };
	service = new google.maps.places.PlacesService(map);
	service.search(request, callback);
}

function callback(results, status){
	if (status == google.maps.places.PlacesServiceStatus.OK) {
	  cinemaSeating = [];
	  for (var i = 0; i < results.length; i++) {
	    fillCinemaList(results[i],i);
	    var tmp = [];
	    var rndm =0;
	    for (var s = 0; s < 100; s++){
	    	rndm = Math.random(); 
	    	if(rndm <0.25){
	    		tmp[s] = 1;
	    	}else{
	    		tmp[s] = 0;
	    	}
	    }
	    cinemaSeating[i] = tmp;
	  }
	}
	selectedCinema =0;
	$("#"+selectedCinema).addClass("selected");
	drawSeating();
}

function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(requestLocationInfo);
    } else {
        x.innerHTML = "Geolocation is not supported by this browser.";
    }
}

function fillCinemaList(cinema,id){
	$(".cinemaList").append("<div class='cinema' id='"+id+"'>"+cinema.name+"</div>");
}

function clearCinemaList(){
	$(".cinema").remove();
}

function drawSeating(){
	infofield();
	ctx.clearRect(0, 0, c.width, c.height);
	ctx.fillStyle = "#000000";
	ctx.fillRect(30,20,335,20);
	relationMap = [];
	var y = 0;
	for(var i = 0; i < cinemaSeating[selectedCinema].length; i++){
		var rx = 30+(i-(y*10))*35,
		    ry = 60+(y*10)*3;
		relationMap[i] = {x:rx,y:ry};
		if(cinemaSeating[selectedCinema][i] == 0){
			ctx.fillStyle = "#00FF00";
		}
		if(cinemaSeating[selectedCinema][i] == 1){
			ctx.fillStyle = "#FF0000";
		}
		if(cinemaSeating[selectedCinema][i] == 2){
			ctx.fillStyle = "#0000FF";
		}
		ctx.fillRect(rx,ry,20,20);
		if((i-(y*10))==9){
			y = y+1;
		}
	}
	ctx.fillStyle = "#00FF00";
	ctx.fillRect(30,370,20,20);
	ctx.fillText("available",55,385)
	ctx.fillStyle = "#FF0000";
	ctx.fillRect(135,370,20,20);
	ctx.fillText("not available",160,385);
	ctx.fillStyle = "#0000FF";
	ctx.fillRect(240,370,20,20);
	ctx.fillText("your reservation",265,385)

}

function reserveSeat(seat){
	if(cinemaSeating[selectedCinema][seat] == 0){
		cinemaSeating[selectedCinema][seat] = 2;
		numberOfReservations = numberOfReservations +1;
	}else{
		if(cinemaSeating[selectedCinema][seat] == 2){
			cinemaSeating[selectedCinema][seat] = 0;
			numberOfReservations = numberOfReservations -1;
		}
	}
	drawSeating();
}

function infofield(){
	$(".infotext").remove();
	$("#info").append("<p class='infotext'>Enter the following data to confirm your reservation of "+ numberOfReservations + " seats.</p>");
}

$(".cinemaList").on("click",".cinema", function(e) {
	$("#"+selectedCinema).removeClass("selected");
	selectedCinema = parseInt($(this).attr("id"));
	$(this).addClass("selected");
	drawSeating();
	});



$("canvas").click(function(e) {

    var pos = getMousePos(c , e);
    var x = pos.x;
    var y = pos.y;
    for(var i = 0; i<relationMap.length; i++){
    	if(relationMap[i].x <= x && (relationMap[i].x+20) >= x && relationMap[i].y <= y && (relationMap[i].y+20) >= y){
    		reserveSeat(i);
    	}
    }
});

function getMousePos(c, evt) {
    var rect = c.getBoundingClientRect();
    return {
        x: evt.clientX - rect.left,
        y: evt.clientY - rect.top
    };
}

init();
getLocation();