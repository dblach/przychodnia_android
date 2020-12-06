<?php
include "connect.php";
$a=array();
if($l){
	$idw=$_POST['id'];
	$r=mysqli_fetch_assoc(mysqli_query($l,"select * from wizyty where id=$idw"));
	if($r['pacjent_id']==$userid){
		$idl=$r['lekarz_id'];
		$data=$r['data'];
		$gr=$r['czas_rozpoczecia'];
		$gk=$r['czas_zakonczenia'];
		$r['lekarz']=implode(' ',mysqli_fetch_assoc(mysqli_query($l,"select imie,nazwisko from lekarze where id=$idl")));
		$r=array_merge($r,mysqli_fetch_assoc(mysqli_query($l,"select nazwa as poradnia from specjalizacje where id=(select specjalizacja from godziny_przyjec where dzien_tygodnia=weekday(\"$data\")+1 and godzina_rozpoczecia<=\"$gr\" and godzina_zakonczenia>=\"$gk\")")));
		$r=array_merge($r,mysqli_fetch_assoc(mysqli_query($l,"select pomieszczenie from godziny_przyjec where dzien_tygodnia=weekday(\"$data\")+1 and godzina_rozpoczecia<=\"$gr\" and godzina_zakonczenia>=\"$gk\" and id_lekarza=$idl")));
		echo '[';
		echo json_encode($r,JSON_UNESCAPED_UNICODE);
		echo ']';
	}
}
?>