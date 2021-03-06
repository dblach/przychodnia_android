<?php
include "connect.php";

function czy_termin_wolny(){
	global $l;
	global $gp;
	global $gk;
	global $date;
	global $idd;
	$k=true;
	$q=mysqli_query($l,"select id,czas_rozpoczecia,czas_zakonczenia from wizyty where lekarz_id=$idd and data=\"$date\"");
	$tgp=strtotime($gp);
	$tgk=strtotime($gk);
	while($r=mysqli_fetch_assoc($q)){
		$ti=$r['id'];
		$wp=$r['czas_rozpoczecia'];
		$wk=$r['czas_zakonczenia'];
		$twp=strtotime($wp);
		$twk=strtotime($wk);
		if(($tgp<=$twp&&$tgk<=$twp)||($tgp>=$twk&&$tgk>=$twk)){
			// brak kolizji
		}
		else{
			// kolizja
			$k=false;
		}
	}
	if(!$k) echo json_encode(array('error_busy'=>'1'));
	return $k;
}


function czy_lekarz_przyjmuje(){
	global $l;
	global $gp;
	global $gk;
	global $date;
	global $idd;
	
	$k=false;
	$tgp=strtotime($gp);
	$tgk=strtotime($gk);	
	$q=mysqli_query($l,"select godzina_rozpoczecia,godzina_zakonczenia from godziny_przyjec where id_lekarza=$idd and dzien_tygodnia=weekday(\"$date\")+1");
	while($r=mysqli_fetch_assoc($q)){
		$tp=$r['godzina_rozpoczecia'];
		$tk=$r['godzina_zakonczenia'];
		$ttp=strtotime($tp);
		$ttk=strtotime($tk);
		if($tgp>=$ttp&&$tgk<=$ttk) $k=true;
	}
	if(!$k) echo json_encode(array('error_no_appointment'=>'1'));
	return $k;
}

function zapisz(){
	global $l;
	global $idp;
	global $idd;
	global $idw;
	global $date;
	global $gp;
	global $gk;
	$q=mysqli_query($l,"update wizyty set data=\"$date\",czas_rozpoczecia=\"$gp\",czas_zakonczenia=\"$gk\",odwolana=0 where id=\"$idw\"");
	echo json_encode(array('result'=>mysqli_affected_rows($l)));
}

if($l){
	$idp=$userid;
	$idd=$_POST['doctor_id'];
	$date=$_POST['date'];
	$gp=$_POST['czas_rozpoczecia'];
	$gk=$_POST['czas_zakonczenia'];
	$idw=$_POST['idw'];
	
	if(czy_lekarz_przyjmuje()&&czy_termin_wolny()) zapisz();
}
?>