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
$bike_x=isset($_POST['bike_x']) ? $_POST['bike_x'] : '';
$bike_y=isset($_POST['bike_y']) ? $_POST['bike_y'] : '';

if ($uid !=""){

  $sql="select user_name, owner_bid, bike_type, bike_x, bike_y, bike_gender from Bike b, Owner o, User u, Velow v where b.id = o.owner_bid and u.id = o.owner_uid and u.id = velower_id and velowee_id = '$uid' and b.bike_status = 0 order by (bike_x - '$bike_x')*(bike_x - '$bike_x') + (bike_y - '$bike_y')*(bike_y - '$bike_y');";
  // bike_x, bike_y: 자전거의 위치
  // $bike_x, $bike_y: user의 위치

  // 현재는 모든 자전거가 다 보이게 해둔상태! 조건 VELOW VELOWING으로 추가

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
