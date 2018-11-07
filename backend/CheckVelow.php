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
$vid=isset($_POST['vid']) ? $_POST['vid'] : '';

if ($uid !="" && $vid !=""){
  $sql="SELECT * FROM User, Velow WHERE velower_id=User.id AND User.user_id='$uid' AND velowee_id='$vid'";
  $result=mysqli_query($link,$sql);

  $Array = array();
  while($line = mysqli_fetch_row($result)){
    $Array[] = $line;
  }
  if($result){
    echo json_encode($Array);
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
