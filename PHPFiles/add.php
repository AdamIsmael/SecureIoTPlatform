<?php
   	include("connect.php");
 
   	$link=Connection();
 
 
	$query = "INSERT INTO tempLog (temp) 
		VALUE ('".$_POST["temperature"]."')"; 
 
   	mysql_query($query,$link);
	mysql_close($link);
 
   	header("Location: index.php");
?>