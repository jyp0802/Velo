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
$addid=isset($_POST['addid']) ? $_POST['addid'] : '';

mysqli_query($link,"START TRANSACTION");
if ($uid !="" && $addid !=""){
  $sql="DELETE FROM Velow WHERE velower_id='$uid' AND velowee_id='$addid'";
  $result=mysqli_query($link,$sql);

  if($result){
    mysqli_query($link,"COMMIT");
    echo "1";
  } else {
    #mysqli_query($link,"ROLLBACK");
    mysqli_query($link,"COMMIT");
    echo "0";
  }
} else {
  mysqli_query($link,"ROLLBACK");
  echo mysqli_error($link);
}

mysqli_close($link);
?>
