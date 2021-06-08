<?php
include "connect.php";

function get_icon($d,$gp,$gk,$idl){
	global $l;
	$ids=mysqli_fetch_array(mysqli_query($l,"select specjalizacja from godziny_przyjec where id_lekarza=$idl and dzien_tygodnia=weekday('$d')+1 and godzina_rozpoczecia<='$gp' and godzina_zakonczenia>='$gk'"))[0];
	return mysqli_fetch_array(mysqli_query($l,"select ikona from specjalizacje where id=$ids"))[0];
}

$a=array();
if($l){
	$limit=$_POST['limit'];
	$q=mysqli_query($l,"select wizyty.id as id,data,time_format(czas_rozpoczecia,\"%H:%i\") as czas_rozpoczecia,time_format(czas_zakonczenia,\"%H:%i\") as czas_zakonczenia,nazwisko,imie,zdjecie,lekarz_id from wizyty join lekarze on wizyty.lekarz_id=lekarze.id where pacjent_id=$userid and wizyty.odwolana=0 order by data desc limit $limit");
	while($r=mysqli_fetch_assoc($q)){
		$r['ikona']=get_icon($r['data'],$r['czas_rozpoczecia'],$r['czas_zakonczenia'],$r['lekarz_id']);
		$a[]=$r;
	}
}
if(count($a)>0) $s=array('success'=>'1'); else $s=array('success'=>0);
echo json_encode($a,JSON_UNESCAPED_UNICODE);
?>