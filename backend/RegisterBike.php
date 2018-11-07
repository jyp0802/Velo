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
$type=isset($_POST['type']) ? $_POST['type'] : '';
$size=isset($_POST['size']) ? $_POST['size'] : '';
$gender=isset($_POST['gender']) ? $_POST['gender'] : '';
$pw=isset($_POST['pw']) ? $_POST['pw'] : '';
$x='0';
$y='0';
$photo=isset($_POST['fileName']) ? $_POST['fileName'] : '';
$status='1'; // 0 = 사용 가능, 1 = 사용 불가능, 2 = 사용중


mysqli_query($link,"START TRANSACTION");
if ($uid !="" and $type !=""){ // 다른것도 empty 확..인..

  $sql="INSERT INTO Bike(bike_type,bike_size,bike_gender,bike_pw,bike_x,bike_y,bike_photo,bike_status) VALUES('$type','$size','$gender','$pw','$x','$y','$photo','$status');";

  $result=mysqli_query($link,$sql);

  if($result){
    $bid = mysqli_insert_id($link);

    $sql = "INSERT INTO Owner(owner_uid, owner_bid) VALUES('$uid','$bid');";
    $result=mysqli_query($link,$sql);
    if($result) {
      mysqli_query($link,"COMMIT");
      echo "SQL success";
    } else {
      mysqli_query($link,"ROLLBACK");
      echo "SQL error : 1";
      echo $uid;
      echo $bid;
      echo mysqli_error($link);
    }
  } else {
    mysqli_query($link,"ROLLBACK");
    echo "SQL error : 2";
    echo mysqli_error($link);
  }
} else {
  mysqli_query($link,"ROLLBACK");
  echo "input data";
}

mysqli_close($link);
?>
