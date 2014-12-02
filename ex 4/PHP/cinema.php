<?php
// HEADER
header('Content-type: application/json; utf-8');

// Database connection
$mysqli = new mysqli('localhost', 'cinema', 'cinema', 'cinema');
if ($mysqli->connect_errno) {
  errorHandling('400', 'I can\' connect!');
}

// Get Movies with seeds
if($_GET['action'] == 'getMovies') {
  $selectMoviesSQL = 'SELECT cm.id, m.name,GROUP_CONCAT(cs.seat) as seats
    FROM cinemas_movies as cm
      LEFT join movies as m on m.id=cm.movie_id
      LEFT join cinemovies_seats as cs on cm.id=cs.cinemovie_id
      WHERE cinema = ? GROUP by cm.id;';
  $stmtMovies =  $mysqli->stmt_init();

  if(!($stmtMovies->prepare($selectMoviesSQL))) {
    errorHandling('400', 'I can\'t prepare my movies!');
  }

  if(!$stmtMovies->bind_param('s', $_GET['cinema'])) {
    errorHandling('400', 'I can\'t bind cinema!');
  }

  $stmtMovies->execute();
  $stmtMovies->bind_result($cinemovie_id, $movie, $seats);
  $responseArray = array();
  while ($stmtMovies->fetch()){
    $json = array();
    $json['cinemovie_id'] = $cinemovie_id;
    $json['name'] = $movie;
    if(empty($seats)) {
      $json['seats'] = array();
    } else {
      $json['seats'] =  array_map('intval', explode(',', $seats));
    }
    array_push($responseArray, $json);
  }
  echo json_encode($responseArray);
  $mysqli->close();
  exit();
}

// Read POST input
$inputData = json_decode(file_get_contents('php://input'));

// Seed cinema
if($_GET['action'] == 'seedCinema') {
  $sqlInsertRandom = 'INSERT INTO cinemas_movies SELECT null, id, ? FROM movies order by rand() LIMIT 4;';
  $stmtRandom =  $mysqli->stmt_init();
  $stmtRandom->prepare($sqlInsertRandom);
  var_dump($inputData);
  $stmtRandom->bind_param('s', $cinema);
  foreach($inputData as $cine) {
    $cinema = $cine;
    $stmtRandom->execute();
  }
  $stmtRandom->close();
  $mysqli->close();
  exit();
}

// Return error if fields below are not set.
$requiredFields = array('name', 'email', 'cinemovie_id', 'seats');
foreach($requiredFields as $field) {
  if(empty($inputData->{$field})) {
    errorHandling('400', 'you don\'t fill in the required fields!');
  }
  // Declare variables
  $$field = $inputData->{$field};
}

// Prepare SQL Statements
$insertResSQL = 'INSERT INTO reservations  VALUES (NULL, ?,?,?)';
$insertSeaSQL = 'INSERT INTO cinemovies_seats VALUES (?,?,?)';
$stmtRes =  $mysqli->stmt_init();
$stmtSea =  $mysqli->stmt_init();

// Using Transaction
$mysqli->autocommit(FALSE);

if(!($stmtRes->prepare($insertResSQL)) || !($stmtSea->prepare($insertSeaSQL))) {
  errorHandling('400', 'I can\'t prepare my data!');
}

if(!$stmtRes->bind_param('ssi', $name, $email, $cinemovie_id)) {
  errorHandling('400', 'I can\'t bind my data!');
}

if(!$stmtRes->execute()){
  $sqlError = true;
}

if(!$stmtSea->bind_param('iii', $cinemovie_id, $reser_id, $seat)) {
  errorHandling('400', 'I can\'t bind my data!');
}

foreach($seats as $seat) {
  $reser_id = $stmtRes->insert_id;
  if(!$stmtSea->execute() && !$sqlError){
    $sqlError = true;
  }
}

if($sqlError) {
  $mysqli->rollback();
  errorHandling('500', 'seat is taken!');
} else {
  $mysqli->commit();
}


$successResponse['id'] = $stmtRes->insert_id;
echo json_encode($successResponse);

// Disconnect mysql
$stmtSea->close();
$stmtRes->close();

// Reset Transaction
$mysqli->autocommit(TRUE);

$mysqli->close();

function errorHandling($header, $message) {
  header('HTTP/1.0 '.$header);
  $error['message'] = 'Ain\'t no sunshine when '.$message;
  echo json_encode($error);
  $mysqli->close();
  die();
}
?>
