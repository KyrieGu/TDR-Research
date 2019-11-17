<?php
     if(isset($_POST['login'])){
		include_once('conn_db.php');
		$email = $_SESSION['email'];

                $option = $_POST['option'];
		$selectuser =  $_POST['selectuser'];
                $_SESSION['selectuser'] = $selectuser;
                $_SESSION['displaytime'] = $option;
                $query = "SELECT * FROM records, users WHERE users.uid = records.uid AND users.email = '$selectuser'";

                $q1 = "SELECT * FROM users WHERE users.email = '$selectuser'";
                $result = mysql_query($q1);
                $rows = array();
                $row=mysql_fetch_assoc($result);
                $_SESSION['selectuser_uid'] =  $row["uid"];
                //echo $_SESSION['selectuser_uid'];

	        if ($option == 1) {
			 $_SESSION['query'] =  $query . " AND records.datetime BETWEEN DATE_SUB( NOW( ) , INTERVAL 1 DAY ) AND NOW()";
	         }
	        else if ($option == 2) {
		          $_SESSION['query'] =  $query . " AND records.datetime BETWEEN DATE_SUB( NOW( ) , INTERVAL 1 WEEK ) AND NOW()";
	         }
	         else if ($option == 3) {
			  $_SESSION['query'] =  $query . " AND records.datetime BETWEEN DATE_SUB( NOW( ) , INTERVAL 1 MONTH ) AND NOW()";
		  }
		  else if ($option == 4) {
			  $_SESSION['query'] = $query;
		  }
                 
                 if ($selectuser == null){
                     $_SESSION['query'] = $_SESSION['query'] . " and users.email = '$email'";
                 }
                 else{
                    $_SESSION['query'] = $_SESSION['query'] . " and users.email = '$selectuser'";
                 } 
                 $_SESSION['query'] = $_SESSION['query']." order by datetime desc";
                 //echo $_SESSION['query'];
                  echo "<script>window.location = 'dashboard.php';</script>";
                  //echo $_SESSION['query'];
        }
       if(isset($_POST['cancel'])){
             echo "<script>window.location = 'dashboard.php';</script>";
       }
?>