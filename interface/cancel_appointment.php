<?php
include "connect.php";
$idw=$_POST['idw'];
$r=mysqli_fetch_assoc(mysqli_query($l,"select * from wizyty where id=$idw"));
if($r['pacjent_id']==$userid){
	$q=mysqli_query($l,"update wizyty set odwolana=1 where id=\"$idw\"");
	echo json_encode(array('result'=>mysqli_affected_rows($l)));
}