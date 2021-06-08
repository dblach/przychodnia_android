<?php
$allowed_folders=array('doctor','clinic','images');

include "connect.php";
if(!$l||!in_array($_POST['folder'],$allowed_folders)) return;

$i=imagecreatefromstring(base64_decode($_POST['data']));
$f=md5($_POST['data']).".jpg";
imagejpeg($i,$_POST['folder']."/$f",100);
imagedestroy($i);
if($_POST['name_old']!='') unlink($_POST['folder'].'/'.$_POST['name_old']);
echo $f;
?>