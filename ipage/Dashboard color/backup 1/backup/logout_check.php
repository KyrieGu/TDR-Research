<?php
	if(isset($_POST['login'])){
		include_once('conn_db.php');

		
		$delete_user_uid = $_POST['delete_user_uid'];
		
		

		$sql = "DELETE FROM Name WHERE userName = '$delete_user_uid' "; 
		$result = mysql_query($sql);

                //$sql = "SELECT * FROM users WHERE email = '$email' AND password = '$password'";
		
		
		echo "Delete Succeed!!!";
		//echo "<script>window.location = 'index.php';</script>";
		echo "http://ksiresearchorg.ipage.com/chronobot/index.php"; 
		//echo $_SESSION['query'];
	}
        if(isset($_POST['cancel'])){
                echo "<script>window.location = 'dashboard.php';</script>";
        }

?>