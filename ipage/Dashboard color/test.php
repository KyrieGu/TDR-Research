<?php
include_once("conn_db.php");
include_once("menu.php");

//fetch the previous nodeID
$previous = "SELECT * FROM events WHERE events.EventGraph_ID = 6 order by node_ID desc limit 1";
$result = mysql_query($previous);
$rows = array();
$row = mysql_fetch_assoc($result);
if (is_null($row)) {
    $previous_ID = NULL;
    $node_ID = 1;
} else {
    $previous_ID = $row['node_ID'];
    $node_ID = $previous_ID + 1;
}
//ready to insert into the events graph
$EventGraph_ID = $_SESSION['test'];
$node_value = $_SESSION['test'];
$pattern_ID = 104;
$strength = 0;
$wellBing = 15;
$temstamp = "2019-07-02 17:10:23";
$sql = "INSERT INTO events (EventGraph_ID, node_ID, node_value, previous_nodeID, pattern_ID, strength, temstamp) VALUES ('$EventGraph_ID', '$node_ID', '$node_value', '$previous_ID', '$pattern_ID', '$strength', '$temstamp') ";
mysql_query($sql);
echo "<script>window.location = 'dashboard.php';</script>";
?>