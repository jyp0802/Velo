<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

$link=mysqli_connect("127.0.0.1","velo","z9mpp123A.","velo_db");
if (!$link)
{
  echo "MySQL error : ";
  echo mysqli_connect_error();
  exit();
}

mysqli_set_charset($link,"utf8");

$uid = isset($_POST['uid']) ? $_POST['uid'] : '';
$bike_x = isset($_POST['bike_x']) ? $_POST['bike_x'] : '';
$bike_y = isset($_POST['bike_y']) ? $_POST['bike_y'] : '';

if ($uid!=""){

  mysqli_query($link,"START TRANSACTION");

  $sql="UPDATE Bike b, Owner o SET bike_status = 0, bike_x = '$bike_x', bike_y = '$bike_y' WHERE o.owner_uid='$uid' and o.owner_bid=b.id;;";
  $result=mysqli_query($link,$sql);

  if($result) {
    echo "true";
    mysqli_query($link, "COMMIT");

  } else {
    mysqli_query($link, "ROLLBACK");
    echo "SQL error";
  }
} else {
  echo "input data";
}

mysqli_close($link);
?>
