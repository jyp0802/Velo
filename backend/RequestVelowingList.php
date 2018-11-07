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
  $sql="SELECT u.id, user_name, bike_type FROM User u, Velow v, Owner o, Bike b WHERE o.owner_uid = u.id and o.owner_bid = b.id and u.id=velowee_id and velower_id='$uid' UNION select id, user_name, bike_type from (SELECT id, user_name, owner_uid as bike_type, user_email FROM User u LEFT JOIN Owner o on o.owner_uid = u.id) as T, velow where bike_type is null and id=velowee_id and velower_id='$uid';";
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
