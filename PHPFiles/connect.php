<?php
 
	function Connection(){
		$server="mysql1.000webhost.com";
		$user="a4105690_adam";
		$pass="pakka47";
		$db="a4105690_tempLog";
 
		$connection = mysql_connect($server, $user, $pass);
 
		if (!$connection) {
	    	die('MySQL ERROR: ' . mysql_error());
		}
 
		mysql_select_db($db) or die( 'MySQL ERROR: '. mysql_error() );
 
		return $connection;
	}
?>