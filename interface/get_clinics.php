<?php
include "connect.php";
$a=array();
if($l){
	$q=mysqli_query($l,"select * from specjalizacje order by nazwa");
	while($r=mysqli_fetch_assoc($q)) $a[]=$r;
}
if(count($a)>0) $s=array('success'=>'1'); else $s=array('success'=>0);
echo json_encode($a,JSON_UNESCAPED_UNICODE);
?>