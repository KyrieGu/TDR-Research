<!doctype html>
<html>
    <head>
        <title>Line Chart</title>

        <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.js"></script> -->
        <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.bundle.min.js"></script> -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.js"></script>
        <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.6.0/Chart.min.js"></script> -->
   
        <!-- <meta name = "viewport" content = "initial-scale = 1, user-scalable = no"> -->
    </head>
    <body>

    <?php
    include_once("conn_db.php");
    ?>


    <?php
    //Set the threshold for BCI and Gaze
    $BCI_thresh = 50;
    $Gaze_thresh = 0.5;

    //Get BCIFilter values from database
    $q = "SELECT * FROM `records` where `source` = 'BCIFilter' ORDER BY `rid` DESC LIMIT 200";
    $result=mysql_query($q);
    $BciRows = array();
    while($row=mysql_fetch_assoc($result))
    {
      if($row['value'] > $BCI_thresh){
          $BciRows[] = 1;
      }
      else{
          $BciRows[] = -1;
      }
    }

    //Get Gaze values from database
    $q = "SELECT * FROM `records` where `source` = 'Gaze' ORDER BY `rid` DESC LIMIT 10";
    $result=mysql_query($q);
    $GazeRows = array();
    while($row=mysql_fetch_assoc($result))
    {
      if($row['value'] > $Gaze_thresh){
          $GazeRows[] = 1;
      }
      else{
          $GazeRows[] = -1;
      }
    }

    // scale the Gaze data to match the length of the BCI data
    // now we set Gaze:BCI = 1:20
    $cnt = 0;
    $GazeRowsNew = array();
    for ($i=0; $i < count($BciRows); $i++) { 
      if (($i+1)%20 == 0) {
        $cnt = $cnt + 1;
      }
      $GazeRowsNew[$i] = $GazeRows[$cnt];
    }

    //Compare the data from BCI and Gaze
    $Consistent = array();
    for ($i=0; $i < count($BciRows); $i++) { 
      if ($BciRows[$i] == $GazeRowsNew[$i]) {
        $Consistent[$i] = $BciRows[$i];
        $BciRows[$i] = 0;
        $GazeRowsNew[$i] = 0;
      }
      else{
        $Consistent[$i] = 0;
      }
    }

    ?>





    <div style="width:40%;">
          <canvas id="myChart" width="400" height="200"></canvas>
    </div>
    <script>
    var ctx = document.getElementById("myChart");

    var len = <?php echo json_encode($BciRows); ?>.length;
    var Lab = [];
    var BrainColor = [];
    var GazeColor = [];
    var ConsistColor = [];
    for (var i = 0; i < len; i++) {
       Lab[i] = i + 1;
       BrainColor[i] = 'rgba(255, 99, 132, 0.2)';
       GazeColor[i] = 'rgba(255, 159, 64, 0.2)';
       ConsistColor[i] = 'rgba(63, 191, 63, 0.2)';
     }

    var myChart = new Chart(ctx, {
      type: 'bar',
      data: {
        //labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
        labels: Lab,
        datasets: [{
            label: 'Brainwave',
            data: <?php echo json_encode($BciRows); ?>,
            backgroundColor: BrainColor
            // backgroundColor: [
            //   'rgba(255, 99, 132, 0.2)',
            //   'rgba(255, 99, 132, 0.2)',
            //   'rgba(255, 99, 132, 0.2)',
            //   'rgba(255, 99, 132, 0.2)',
            //   'rgba(255, 99, 132, 0.2)',
            //   'rgba(255, 99, 132, 0.2)'
            // ],
            // borderColor: [
            //   'rgba(255,99,132,1)',
            //   'rgba(255,99,132,1)',
            //   'rgba(255,99,132,1)',
            //   'rgba(255,99,132,1)',
            //   'rgba(255,99,132,1)',
            //   'rgba(255,99,132,1)'
            // ],
            // borderWidth: 0
          },
          {
            label: 'Gaze',
            data: <?php echo json_encode($GazeRowsNew); ?>,
            backgroundColor: GazeColor
            // backgroundColor: [
            //   'rgba(255, 159, 64, 0.2)',
            //   'rgba(255, 159, 64, 0.2)',
            //   'rgba(255, 159, 64, 0.2)',
            //   'rgba(255, 159, 64, 0.2)',
            //   'rgba(255, 159, 64, 0.2)',
            //   'rgba(255, 159, 64, 0.2)'
            // ],
            // borderColor: [
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)'
            // ],
            // borderWidth: 0
          },
          {
            label: 'Consistent',
            data: <?php echo json_encode($Consistent); ?>,
            backgroundColor: ConsistColor
            // backgroundColor: [
            //   'rgba(63, 191, 63, 0.2)',
            //   'rgba(63, 191, 63, 0.2)',
            //   'rgba(63, 191, 63, 0.2)',
            //   'rgba(63, 191, 63, 0.2)',
            //   'rgba(63, 191, 63, 0.2)',
            //   'rgba(63, 191, 63, 0.2)'
            // ],
            // borderColor: [
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)',
            //   'rgba(255, 159, 64, 1)'
            // ],
            // borderWidth: 0
          }
        ]
      },
      options: {
        scales: {
          yAxes: [{
            stacked: true,
            ticks: {
              beginAtZero: true,
              stepSize: 1
            }
          }],
          xAxes: [{
            stacked: true,
            categoryPercentage: 1.0,
            barPercentage: 1.0,
            ticks: {
              beginAtZero: true
            }
          }]

        }
      }
    });
    </script>
    </body>
</html>