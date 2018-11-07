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

if ($uid !=""){
  $sql="SELECT * FROM Owner o, User u WHERE u.id = '$uid' AND u.id = o.owner_uid;";
  $result=mysqli_query($link,$sql);

  if(mysqli_num_rows($result)==0){
    echo "0"; // user가 등록한 bike 없음
  } else {
    echo "1"; // user가 등록한 bike 있음
  }
} else {
  echo "SQL error : ";
  echo mysqli_error($link);
}

mysqli_close($link);
?>
