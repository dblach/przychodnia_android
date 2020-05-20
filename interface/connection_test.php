<?php
include "connect.php";
if($l) $r["success"]=1; else $r["success"]=0;
$r["userid"]=$userid;
echo json_encode($r);
?>