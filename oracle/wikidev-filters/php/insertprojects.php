<?php

if($_GET['xml']) {
    $username = "wikidev";
    $password = "wikidev989";
    $hostname = "localhost";
    $database = "wikidev";    
    $dbh = mysql_connect($hostname, $username, $password) or die("Unable to connect to MySQL");
    $selected = mysql_select_db("wikidev",$dbh) or die("Could not select database");
    
	//Since we're already using PHP5, why don't we exploit their easy to use file_get_contents() command?
	$xmlFileData = file_get_contents($_GET['xml']);
	//Here's our Simple XML parser!
	$xmlData = new SimpleXMLElement($xmlFileData);
	//And here's the output.
	print_r($xmlData);
    
	
	//Outputing all of our XML to people
	foreach($xmlData->project as $project) {
		$projectname = $project->projectname;
		$projectid = $project->projectid;
		$select = mysql_query("SELECT * FROM wikidev_projects WHERE projectid = '$projectid'");
		if(mysql_num_rows($select) == 0) {
			$sql = "INSERT INTO wikidev_projects VALUES ('$projectid','$projectname')";
			
			if (mysql_query($sql)) {
		         print "successfully inserted record";
		    }
		    else {
		          print "Failed to insert record";
		    }
		}
		else {
			print "Tuple already exists";
		}
	    
	}
    
    mysql_close($dbh);
}
else {
	print "Missing argument";
}
?>