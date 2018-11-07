<?php
error_reporting(E_ALL);
ini_set('display_errors',1);

$link=mysqli_connect("127.0.0.1","velo","z9mpp123A.","velo_db");
if (!$link)
{
  echo "MySQL error : 0";
  echo mysqli_connect_error();
  exit();
}

mysqli_set_charset($link,"utf8");

$uid=isset($_POST['uid']) ? $_POST['uid'] : '';
$fileName=isset($_POST['fileName']) ? $_POST['fileName'] : '';

if ($uid !="" && $fileName !=""){
  #$sql="UPDATE Bike SET bike_photo='asd.jpg' WHERE Bike.id IN (SELECT Owner.owner_bid FROM Owner WHERE Owner.owner_uid=7)";
  $sql="UPDATE Bike SET bike_photo='$fileName' WHERE Bike.id IN (SELECT Owner.owner_bid FROM Owner WHERE Owner.owner_uid='$uid')";
  $result=mysqli_query($link,$sql);

  if($result){
    echo "1";
  } else {
    echo "0";
  }
} else {
  echo "SQL error : ";
  echo mysqli_error($link);
}

mysqli_close($link);
?>
