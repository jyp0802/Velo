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

$bid = isset($_POST['bid']) ? $_POST['bid'] : '';
$uid = isset($_POST['uid']) ? $_POST['uid'] : '';

if ($uid!="" and $bid!=""){

  mysqli_query($link,"START TRANSACTION");

  $sql = "SELECT bike_status from Bike where id = '$bid';";
  $result = mysqli_query($link, $sql);

  if($result){

    $donterase = mysqli_fetch_row($result); // 이거 없으면 오류남.. 왠지 모르겠다 ㅇㅅaㅇ ㅇㅅㅇ/''

    if(mysqli_fetch_row($result)[0] != '0'){

      $sql="UPDATE Bike SET bike_status = 2 WHERE id='$bid';";
      $result=mysqli_query($link,$sql);

      if($result) {

        $sql="INSERT INTO Borrow(borrow_uid, borrow_bid) VALUES('$uid','$bid');";
        $result=mysqli_query($link,$sql);

        if($result) {
          echo "true";
          mysqli_query($link, "COMMIT");

        } else {
          mysqli_query($link, "ROLLBACK");
          echo "SQL error : 1"; // borrow에서 오류
          echo mysqli_error($link);
        }
      } else {
        mysqli_query($link, "ROLLBACK");
        echo "false";
      }
    } else {
      mysqli_query($link, "ROLLBACK");
      echo "SQL error : 2"; // bid에 해당하는 자전거 상태 != 대여가능
      echo mysqli_error($link);
    }
  } else {
    mysqli_query($link, "ROLLBACK");
    echo "SQL error : 3"; // bid에 해당하는 자전거 존재하지 않음
    echo mysqli_error($link);
  }
} else {
  echo "input data";
}

mysqli_close($link);
?>
