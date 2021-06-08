<?php
//error_reporting(0);
header('Content-Type: text/html; charset=utf-8');
$hostname="localhost";
$database="eskulap";
$username=$_POST['username'];
$password=$_POST['password'];
$l=mysqli_connect($hostname,$username,$password,$database);
$userid=-1;
if($l){
	$a=mysqli_fetch_array(mysqli_query($l,"select id from pacjenci where login='$username'"));
	if(isset($a[0])) $userid=$a[0];
}
?>