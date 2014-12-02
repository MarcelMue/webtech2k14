<!DOCTYPE html>
<html>
<head>
  <title>CineYellow</title>
  <meta charset="UTF-8">
  <link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" type="text/css" href="stylesheets/app.css">

  <script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
  <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places&sensor=true"></script>
  <script type="text/javascript" src="javascripts/app.js"></script>
</head>
<body>
  <header>
    <h1 class="title">CineYellow</h1>
  </header>
  <nav>
    <ul>
      <li><a href="http://www.google.com">Google</a></li>
      <li><a href="http://www.rwth-aachen.de">RWTH</a></li>
    </ul>
  </nav>
  <main role="main">
    <div class="left-col">
      <h2>Select cinema...</h2>
      <ul id="cinemaList" class="cinemas">
        <li>Nothing</li>
      </ul>
      <h2>Select movie...</h2>
      <ul id="movieList" class="movies">
        <li>Nothing</li>
      </ul>
    </div>
    <div class="right-col">
      <h2>Select seats...</h2>
      <canvas id="hall">

      </canvas>
      <h2>Fill in...</h2>
      <form id="reservation">
        <input type="hidden" name="cinemovie_id" value="0">
        <p>
          Name <br>
          <input type="text" name="name">
        </p>
        <p>
          E-Mail <br>
          <input type="email" name="email">
        </p>
        <p>
          Selected Seats<br>
          <ul id="selected_seats">

          </ul>
        </p>
        <p>
          <input type="submit"  value="Submit">
        </p>
      </form>
    </div>


  </main>
  <div id="hiddenmap"></div>
</body>
</html>
