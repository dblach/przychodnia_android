<?php
include "connect.php";
$a=array();
if($l){
	$q=mysqli_query($l,"select ustawienie,wartosc from konfiguracja where ustawienie like 'czas_na_edycje'");
	while($r=mysqli_fetch_array($q)) $a[$r[0]]=$r[1];
}
if(count($a)>0) $s=array('success'=>'1'); else $s=array('success'=>0);
echo '[';
echo json_encode(array_merge($s,$a),JSON_UNESCAPED_UNICODE);
echo ']';
?>