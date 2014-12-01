<?php
// HEADER
header('Content-type: application/json; utf-8');

// Database connection
$mysqli = new mysqli('localhost', 'cinema', 'cinema', 'cinema');
if ($mysqli->connect_errno) {
  errorHandling('400', 'I can\' connect!');
}

// Read POST input
$inputData = json_decode(file_get_contents('php://input'));

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
$insertSeaSQL = 'INSERT INTO cinemovies_seats VALUES (?,?)';
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

if(!$stmtSea->bind_param('ii', $cinemovie_id, $seat)) {
  errorHandling('400', 'I can\'t bind my data!');
}

foreach($seats as $seat) {
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
