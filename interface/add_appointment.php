<?php
include "connect.php";
if($l){
	$idp=$userid;
	$idd=$_POST['doctor_id'];
	$date=$_POST['date'];
	$gp=$_POST['czas_rozpoczecia'];
	$gk=$_POST['czas_zakonczenia'];
	$q=mysqli_query($l,"INSERT INTO `wizyty` (`id`, `pacjent_id`, `lekarz_id`, `data`, `czas_rozpoczecia`, `czas_zakonczenia`, `notatka`, `odwolana`) VALUES (NULL, '$idp', '$idd', '$date', '$gp', '$gk', '', '0')");
	echo mysqli_affected_rows($l);
	//while($r=mysqli_fetch_array($q)) echo print_r($r);
}
?>