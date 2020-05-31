<?php
include "connect.php";
$a=array();
if($l){
	$c=2;
	$idd=$_POST['doctor_id'];
	$date=$_POST['date'];
	$day=date('w',strtotime($date));
	if($day==0) $day=7;
	$tf="%H:%i";
	
	$a[0]=array('type'=>'start')+mysqli_fetch_assoc(mysqli_query($l,"select time_format(min(godzina_rozpoczecia),\"$tf\") as d from godziny_przyjec where id_lekarza=$idd and dzien_tygodnia=$day"));
	$a[1]=array('type'=>'stop')+mysqli_fetch_assoc(mysqli_query($l,"select time_format(max(godzina_zakonczenia),\"$tf\") as d from godziny_przyjec where id_lekarza=$idd and dzien_tygodnia=$day"));
	
	$td=array('type'=>'admission');
	$q=mysqli_query($l,"select time_format(godzina_rozpoczecia,\"$tf\") as godzina_rozpoczecia,time_format(godzina_zakonczenia,\"$tf\") as godzina_zakonczenia from godziny_przyjec where id_lekarza=$idd and dzien_tygodnia=$day");
	while($r=mysqli_fetch_assoc($q)){
		$a[$c]=$td+$r;
		$c++;
	}
	
	$td=array('type'=>'appointment');
	$q=mysqli_query($l,"select time_format(czas_rozpoczecia,\"$tf\") as godzina_rozpoczecia,time_format(czas_zakonczenia,\"$tf\") as godzina_zakonczenia from wizyty where lekarz_id=$idd and data=\"$date\"");
	while($r=mysqli_fetch_assoc($q)){
		$a[$c]=$td+$r;
		$c++;
	}
}

//if(count($a)>0) $s=array('success'=>'1'); else $s=array('success'=>0);
echo json_encode($a,JSON_UNESCAPED_UNICODE);
?>