<?php
$con = mysqli_connect("ksiresearchorg.ipagemysql.com","duncan","duncan","chronobot");

//1、Records — GazeRelation条件
if($_POST['opt']==1){
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqla = "select * from records where source = 'Gaze'  and rid not in(select RecordID from GazeRelation) limit 0,".$_POST['num'];
	$resulta = mysqli_query($con,$sqla);
	while($rsa = mysqli_fetch_array($resulta)){
		if($rsa['type']=="GazeX"){
			$sqlaa = "insert into GazeRelation(RecordID,GazeX,UserID,Timestamp,Originator) values(".$rsa['rid'].",'".$rsa['value']."','".$rsa['uid']."','".date('Y-m-d H:i:s')."','".$rsa['originator']."')";	
		}elseif($rsa['type']=="GazeY"){
			$sqlaa = "insert into GazeRelation(RecordID,GazeY,UserID,Timestamp,Originator) values(".$rsa['rid'].",'".$rsa['value']."','".$rsa['uid']."','".date('Y-m-d H:i:s')."','".$rsa['originator']."')";	
		}
		$rs = mysqli_query($con,$sqlaa);
	}

	if($resulta){
		echo 1;
	}
	mysql_close($con);
}elseif($_POST['opt']==2){
	//2、Records —> SPO2条件
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqlb = "select * from records where source = 'SPO2' and rid not in(select RecordID from SPO2Relation) limit 0,".$_POST['num'];
	$resultb = mysqli_query($con,$sqlb);
	while($rsb = mysqli_fetch_array($resultb)){
		$sqlbb = "insert into SPO2Relation(RecordID,UserID,TimeStamp,SPO2,Source,Originator) values(".$rsb['rid'].",'".$rsb['uid']."','".date('Y-m-d H:i:s')."','".$rsb['value']."','SPO2','".$rsb['originator']."')";	
		$rs = mysqli_query($con,$sqlbb);
	}
	if($resultb){
		echo 1;
	}
	mysql_close($con);
	
}elseif($_POST['opt']==3){
	//3、Records —> BloodPressure
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqlc = "select * from records where source = 'BloodPressure' and rid not in(select RecordID from BloodPressure) limit 0,".$_POST['num'];
	$resultc = mysqli_query($con,$sqlc);
	while($rsc = mysqli_fetch_array($resultc)){
		$sqlcc = "insert into BloodPressure(RecordID,UserID,type,TimeStamp,BloodPressure,Source,Originator) values(".$rsc['rid'].",'".$rsc['uid']."','".$rsc['type']."','".date('Y-m-d H:i:s')."','".$rsc['value']."','BloodPressure','".$rsc['originator']."')";	
		$rs = mysqli_query($con,$sqlcc);
	}
	if($resultc){
		echo 1;
	}
	mysqli_close($con);	
	
}elseif($_POST['opt']==4){
	//4、Records —> GestureRelation
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqld = "select * from records where source = 'GestureRecognizer::key36@pitt.edu' and rid not in(select RecordID from GestureRelation) limit 0,".$_POST['num'];
	$resultd = mysqli_query($con,$sqld);
	while($rsd = mysqli_fetch_array($resultd)){
		$sqldd = "insert into GestureRelation(RecordID,UserID,TimeStamp,Gesture,type,Source,Originator) values(".$rsd['rid'].",'".$rsd['uid']."','".date('Y-m-d H:i:s')."','".$rsd['value']."','".$rsd['type']."','GestureRecognizer::key36@pitt.edu','".$rsd['originator']."')";	
		$rs = mysqli_query($con,$sqldd);
	}
	if($resultd){
		echo 1;
	}
	mysql_close($con);	
	
}elseif($_POST['opt']==5){
	//5、EKGRelation
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqle = "select * from records where source = 'EKG'  and rid not in(select RecordsID from EKGRelation)  limit 0,".$_POST['num'];
	$resulte = mysqli_query($con,$sqle);
	while($rse = mysqli_fetch_array($resulte)){
		$sqlee = "insert into EKGRelation(RecordsID,UserID,TimeStamp,EKG,type,originator) values(".$rse['rid'].",'".$rse['uid']."','".date('Y-m-d H:i:s')."','".$rse['value']."','".$rse['type']."','".$rse['originator']."')";	
		$rs = mysqli_query($con,$sqlee);
	}
	if($resulte){
		echo 1;
	}
	mysqli_close($con);	
	
}elseif($_POST['opt']==6){
	//6、Records->Parrot1Relation
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqlf = "select * from records where source = 'FlowerPower2'  and rid not in(select RecordID from ParrotRelation) limit 0,".$_POST['num'];
	$resultf = mysqli_query($con,$sqlf);
	while($rsf = mysqli_fetch_array($resultf)){
		$sqlff = "insert into ParrotRelation(RecordID,UserID,TimeStamp,Type,Value,originator) values(".$rsf['rid'].",'".$rsf['uid']."','".date('Y-m-d H:i:s')."','".$rsf['type']."','".$rsf['value']."','".$rsf['originator']."')";	
		$rs = mysqli_query($con,$sqlff);
	}
	if($resultf){
		echo 1;
	}
	mysql_close($con);
	
}elseif($_POST['opt']==7){
	//7、Records->Brainwave
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqlf = "select * from records where source = 'BCIFilter'  and rid not in(select rid from BrainWave) limit 0,".$_POST['num'];
	$resultf = mysqli_query($con,$sqlf);
	while($rsf = mysqli_fetch_array($resultf)){
		$sqlff = "insert into BrainWave(rid,uid,timeStamp,source,probability,value,originator) values(".$rsf['rid'].",'".$rsf['uid']."','".date('Y-m-d H:i:s')."','BCIFilter','".$rsf['value']."','".$rsf['value']."','".$rsf['originator']."')";	
		$rs = mysqli_query($con,$sqlff);
	}
	if($resultf){
		echo 1;
	}
	mysql_close($con);
}elseif($_POST['opt']==8){
	if (!$con){die('Could not connect: ' . mysql_error());}
	$sqlg = "select * from records where source = 'EmotionDiary'  and rid not in(select rid from EmotionDiary) limit 0,".$_POST['num'];
	$resultg = mysqli_query($con,$sqlg);
	while($rsg = mysqli_fetch_array($resultg)){
	    $sqlgg = "insert into EmotionDiary(rid,uid,date,source,emotion, emotionValue) values(".$rsg['rid'].",".$rsg['uid'].",'".date('Y-m-d H:i:s')."','EmotionDiary','".$rsg['type']."','".$rsg['value']."')";	
		$rs = mysqli_query($con,$sqlgg);
	}
	if($resultg){
		echo 1;
	}
	mysql_close($con);
}elseif($_POST['opt']==9){
        if (!$con){die('Could not connect: ' . mysql_error());}
        $sqlh = "select * from records where source = 'EatingDisorder' and rid not in(select rid from DietDailyUserFood) limit 0,".$_POST['num'];
        $resulth = mysqli_query($con,$sqlh);
        while($rsh = mysqli_fetch_array($resulth)){
              $sqlhh = "insert into DietDailyUserFood(rid,userId,foodname,dateandtime,number) values(".$rsh['rid'].",'".$rsh['uid']."','".$rsh['type']."','".date('Y-m-d H:i:s')."','".$rsh['value']."')";
                $rs = mysqli_query($con,$sqlhh);
        }
	if($resulth){
		echo 1;
	}
	mysql_close($con);
}	
?>
