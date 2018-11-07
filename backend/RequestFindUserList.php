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

$findtxt=isset($_POST['findtxt']) ? $_POST['findtxt'] : '';
$Queryfindtxt = "%" . $findtxt . "%";

if ($findtxt !=""){
  //$sql="SELECT id, user_name, user_id FROM User WHERE user_name LIKE '$Queryfindtxt';";
  $sql="SELECT u.id, user_name, bike_type FROM User u, Owner o, Bike b WHERE o.owner_uid = u.id and o.owner_bid = b.id and u.user_email LIKE '$Queryfindtxt' UNION select id, user_name, bike_type from (SELECT id, user_name, owner_uid as bike_type, user_email FROM User u LEFT JOIN Owner o on o.owner_uid = u.id) as T where bike_type is null and user_email LIKE '$Queryfindtxt';";

  $result=mysqli_query($link,$sql);

  $Array = array();
  while($line = mysqli_fetch_row($result)){
    $Array[] = $line;
  }
  if($result){
    echo json_encode($Array);

  } else {
    echo "SQL error : 1";
    echo mysqli_error($link);
  }
} else {
  echo "SQL error : 2";
  echo mysqli_error($link);
}

mysqli_close($link);
?>
