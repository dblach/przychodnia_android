<?php
include "connect.php";
$a=array();
if($l){
	$limit=$_POST['limit'];
	$q=mysqli_query($l,"select wizyty.id as id,data,time_format(czas_rozpoczecia,\"%H:%i\") as czas_rozpoczecia,time_format(czas_zakonczenia,\"%H:%i\") as czas_zakonczenia,nazwisko,imie,zdjecie,specjalizacje.ikona from wizyty join lekarze on wizyty.lekarz_id=lekarze.id join godziny_przyjec on (godzina_rozpoczecia<=czas_rozpoczecia and godzina_zakonczenia<=czas_zakonczenia) join specjalizacje on godziny_przyjec.specjalizacja=specjalizacje.id where pacjent_id=$userid and wizyty.odwolana=0 order by data desc limit $limit");
	while($r=mysqli_fetch_assoc($q)) $a[]=$r;
}
if(count($a)>0) $s=array('success'=>'1'); else $s=array('success'=>0);
echo json_encode($a,JSON_UNESCAPED_UNICODE);
?>