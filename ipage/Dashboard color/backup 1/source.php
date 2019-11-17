<?php
include_once("conn_db.php");
include_once("menu.php");
$type = $_GET["type"];
$email = $_SESSION['email'];
if (!$_SESSION['selectuser']){
	$selectuser = $email;
	$_SESSION['selectuser'] = $selectuser;
}

if ($type == 'follow'){
    $type = $_SESSION['recordtype']; 
	
	
	$selectuser = $_SESSION['selectuser'];
	$sql1 = " SELECT * FROM users WHERE users.email = '$selectuser' ";
	$rrrr = mysql_query($sql1);
	$rrrs = mysql_fetch_assoc($rrrr);
	$uid = $rrrs['uid'];

	$sql2 = " insert into msgTOsis(originator,source, flag,uid,requester) values ('Androidphone2', '$type', '0', '$uid' ,'$email')  ";
	$rs = mysql_query($sql2);
	
}
$_SESSION['recordtype'] = $type;

?>



<!-- Page Heading -->
<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">
			Detail List <small><?php echo " for e-mail: ".$_SESSION['selectuser'];?></small>
		</h1>
		<ol class="breadcrumb">
			<li class="active">
				<i class="fa fa-list-alt"></i> Statistics Details
			</li>
		</ol>
	</div>
</div>

<!-- /.row -->
<table class="table table-bordered table-hover table-striped">
	<tr>
		<th>Record ID</th><th>Uid</th><th>Date</th><th>Source</th><th>Type</th><th>Reading</th><th>Originator</th>
	</tr>
	<?php
	$q = $_SESSION['selectuser'] ? "select * from records, users where users.uid = records.uid AND records.source != 'Similar' AND users.email = '$selectuser'" : "select * from records, users where users.uid = records.uid AND records.source != 'Similar' AND users.email = '$email'";
         if ($_SESSION['displaytime'] == 1) {
			 $q =  $q . " AND records.datetime BETWEEN DATE_SUB( NOW( ) , INTERVAL 1 DAY ) AND NOW()";
	 }
	 else if ($_SESSION['displaytime'] == 2) {
		          $q =  $q . " AND records.datetime BETWEEN DATE_SUB( NOW( ) , INTERVAL 1 WEEK ) AND NOW()";
	  }
	 else if ($_SESSION['displaytime'] == 3) {
			  $q =  $q . " AND records.datetime BETWEEN DATE_SUB( NOW( ) , INTERVAL 1 MONTH ) AND NOW()";
        }
        $q = $q. "order by datetime desc";
        //echo $q;
        $_SESSION['query']=$q;
	$result=mysql_query($q);
	$rows = array();
	while($row=mysql_fetch_assoc($result))
	{

                if($row["source"]=="ReadingBehavior"){
                        echo"<tr><th>".$row["rid"]."</th><th>".$row["uid"]."</th><th>".$row["datetime"]."</th><th>".$row["source"]."</th><th>".$row["type"]."</th><th>".$row["value"]."</th><th>Machine</th>";
                }
                else{
                       echo"<tr><th>".$row["rid"]."</th><th>".$row["uid"]."</th><th>".$row["datetime"]."</th><th>".$row["source"]."</th><th>".$row["type"]."</th><th>".$row["value"]."</th><th>".$row["originator"]."</th>";
                }
		
	}
	?>
</table>

<table class="table table-bordered table-hover table-striped">
	<tr>
	<td><a href="source.php?type=follow"><input type="submit" value="View Details" /></a>
	</td>
	<td><a href="http://ksiresearchorg.ipage.com/chronobot/analysis_data.php?type=<?php echo $type;?>"><input type="submit" value="Draw Graphs" /></a>
        </td>
        <td>
<a href="http://ksiresearchorg.ipage.com/chronobot/dashboard_chi.php?type=<?php echo $type;?>"><input type="submit" value="Visualize" /></a>
	</td>
	<td><a href="analyzeChidata.php?type=follow"><input type="submit" value="Analyze Data" /></a>
	</td>
	
	
	
	<td><a href="setErrorRate_admin.php?type=chi"><input type="submit" value="Set Error Rate" /></a>
	</td>
	
	
	
	
	
	<td><a href="findsimilar.php?type=follow"><input type="submit" value="Find Similar" /></a>
	</td>
	<td><a href="http://ksiresearchorg.ipage.com/chronobot/enter_data.php"><input type="submit" value="Enter Data" /></a>
	</td>
	
	
	</tr>
</table>
	

		
<?php
include_once("bottom.php");
?>