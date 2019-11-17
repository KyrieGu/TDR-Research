<?php
        if(isset($_POST['login'])){
		include_once('conn_db.php');
		$email = $_SESSION['email'];

                $option = $_POST['option'];
		$_SESSION['displaytime'] = $option;
                $uid = $_SESSION['uid'];                

                $query = "SELECT * FROM records, users WHERE users.uid = records.uid AND users.uid = $uid";
		
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
                  $_SESSION['query'] = $_SESSION['query']." order by datetime desc";
                  echo "<script>window.location = 'dashboard.php';</script>";
      }
      if(isset($_POST['cancel'])){
             echo "<script>window.location = 'dashboard.php';</script>";
       }
?>