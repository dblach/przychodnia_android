<?php
include "connect.php";
$a=array();
if($l){
	$c=0;
	$idc=$_POST['clinic_id'];
	
	$td=array('type'=>'doctor');
	$q=mysqli_query($l,"select distinct(id_lekarza),imie,nazwisko,zdjecie from godziny_przyjec join lekarze on id=id_lekarza where godziny_przyjec.specjalizacja=$idc order by id_lekarza");
	while($r=mysqli_fetch_assoc($q)){
		$a[$c]=$td+$r;
		$dl[$c]=$r['id_lekarza'];
		$c++;
	}
	
	$td=array('type'=>'admission');
	$q=mysqli_query($l,"select id_przyjecia,id_lekarza,specjalizacja,dzien_tygodnia,time_format(godzina_rozpoczecia,\"%H:%i\") as godzina_rozpoczecia,time_format(godzina_zakonczenia,\"%H:%i\") as godzina_zakonczenia,pomieszczenie from godziny_przyjec where specjalizacja=$idc");
	while($r=mysqli_fetch_assoc($q)){
		$a[$c]=$td+$r;
		$c++;
	}
}
//if(count($a)>0) $s=array('success'=>'1'); else $s=array('success'=>0);
echo json_encode($a,JSON_UNESCAPED_UNICODE);
?>