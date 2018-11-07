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

$uid=isset($_POST['uid']) ? $_POST['uid'] : '';

if ($uid !=""){
  $sql="SELECT * FROM Bike b, Owner o WHERE o.owner_uid = '$uid' AND b.id = o.owner_bid AND b.bike_status = 0;";
  $result=mysqli_query($link,$sql);

  if(mysqli_num_rows($result)==0){
    echo "0"; // 자전거의 상태 : not available
  } else {
    echo "1"; // 자전거의 상태 : availabe
  }
} else {
  echo "SQL error : ";
  echo mysqli_error($link);
}

mysqli_close($link);
?>
