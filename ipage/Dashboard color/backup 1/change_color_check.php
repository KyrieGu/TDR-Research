<?php
	if(isset($_POST['login'])){
		include_once('conn_db.php');

		$component_name = $_POST['component_name'];
		$component_color = $_POST['component_color'];
                if ($component_name == 'Chi'){
                      $_SESSION['Chi_color'] = $component_color;
                }
                if ($component_name == 'Tian'){
                      $_SESSION['Tian_color'] = $component_color;
                }
		if ($component_name == 'Di'){
                      $_SESSION['Di_color'] = $component_color;
                }
                if ($component_name == 'Ren'){
                      $_SESSION['Ren_color'] = $component_color;
                }

		echo "Change Succeed!!!";
		echo "<script>window.location = 'dashboard.php';</script>";
		//echo $_SESSION['query'];
	}
        if(isset($_POST['cancel'])){
               echo "<script>window.location = 'dashboard.php';</script>";
        }

  if(isset($_POST['autoChange'])){
    include_once('conn_db.php');
    $q = "select * from records, users where users.uid = records.uid AND records.type = 'chiTotal' AND users.email = '$selectuser' AND records.datetime BETWEEN DATE_SUB( NOW( ) , INTERVAL 1 DAY ) AND NOW()";
    $result=mysql_query($q);
    $rows = array();
    
    $row=mysql_fetch_assoc($result);
    $_SESSION['Chi_color'] = 'G';
    if($row["value"]=='A' || $row["value"]=='a'||$row["value"]=='Abnormal');
      $_SESSION['Chi_color'] = 'R';

    $q1 = "select * from records, users where users.uid = records.uid AND records.type = 'fertilizer' AND users.email = '$selectuser'";
    $result=mysql_query($q1);
    $rows = array();
    $row=mysql_fetch_assoc($result);
    echo $row;
    echo $row["value"];
    $_SESSION['Ren_color'] = 'G';
    echo "Value:";
    echo $row["value"];
    if($row["value"]==3){
      $_SESSION['Tian_color'] = 'R';
      $_SESSION['Ren_color'] = 'Y';}
    if($row["value"]!=3){
      $_SESSION['Tian_color'] = 'Y';
      $_SESSION['Ren_color'] = 'R';}
    echo $_SESSION['Ren_color'];

    
    


    // $_SESSION['Chi_color'] = $component_color;
    // $_SESSION['Tian_color'] = $component_color;
    // $_SESSION['Di_color'] = $component_color;
    // $_SESSION['Ren_color'] = $component_color;

    echo "Change Succeed!!!";
    echo "<script>window.location = 'dashboard.php';</script>";
    //echo $_SESSION['query'];
  }

?>