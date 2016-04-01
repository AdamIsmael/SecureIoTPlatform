<?php
   	include("connect.php");
 
   	$link=Connection();
	
	$temp=$_POST["temperatureHex"];
 
	$query = "INSERT INTO tempLog (tempHex) 
		VALUE ('".$temp."')"; 
 
   	mysql_query($query,$link);
	mysql_close($link);
?>