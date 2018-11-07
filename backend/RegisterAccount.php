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

$name=isset($_POST['name']) ? $_POST['name'] : '';
$id=isset($_POST['id']) ? $_POST['id'] : '';
$pw=isset($_POST['pw']) ? $_POST['pw'] : '';
$email=isset($_POST['email']) ? $_POST['email'] : '';
$phone=isset($_POST['phone']) ? $_POST['phone'] : '';
$studentid=isset($_POST['studentid']) ? $_POST['studentid'] : '';

// 조건확인
// id 중복체크

include "./password.php";

if ($name !="" and $id !="" and $pw !="" and $email !="" and $phone != "" and $studentid !="" ){

  $sql="SELECT * from user where user_email = '$email';";

  $result=mysqli_query($link,$sql);

  if(mysqli_num_rows($result) == 0){

    $sql="INSERT INTO User(user_name,user_id,user_pw,user_email,user_phone,user_studentid) values('$name','$id','$pw','$email','$phone','$studentid')";

    $result=mysqli_query($link,$sql);

    if($result){
       echo "success";
    } else{
      echo "error : ";
      echo mysqli_error($link);
    }

  } else {
    echo "duplicated";
  }
} else {
  echo "input data";
}


mysqli_close($link);
?>
