<?php
include "connect.php";
$a=array();
if($l){
	$a['user']=mysqli_fetch_array(mysqli_query($l,"select concat_ws(' ',imie,nazwisko) from pacjenci where id=$userid"))[0];
}
echo '['.json_encode($a,JSON_UNESCAPED_UNICODE).']';
?>