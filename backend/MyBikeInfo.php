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

if ($uid !=""){

  $sql="select bike_x, bike_y from Bike b, Owner o where owner_uid = '$uid' and owner_bid = b.id;";
  $result=mysqli_query($link,$sql);

  if($result){
    echo json_encode(mysqli_fetch_assoc($result));
  } else {
    echo "SQL error : ";
    echo mysqli_error($link);
  }
} else {
  echo "SQL error : ";
  echo mysqli_error($link);
}

mysqli_close($link);
?>
