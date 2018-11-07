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

$email=isset($_POST['email']) ? $_POST['email'] : '';
$pw=isset($_POST['pw']) ? $_POST['pw'] : '';

$array = array(
  "login" => "0",
);

if ($email !="" and $pw !=""){

  $sql = "SELECT * FROM User WHERE user_email='$email' AND user_pw='$pw'";
  $result = mysqli_query($link,$sql);

  if ($result) {
    if($row = mysqli_fetch_assoc($result)){
      $array = array(
        "login" => "1",
        "uid" => $row["id"],
        "name" => $row["user_name"],
        "id" => $row["user_id"],
        "phone" => $row["user_phone"],
        "studentid" => $row["user_studentid"],
      );
    }
  }
}

$json = json_encode($array);
echo $json;

mysqli_close($link);
?>
