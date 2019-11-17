<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>SIS Web Interface</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/sis-admin.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
		<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
		<script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
	<![endif]-->

</head>

<body>
	<div class="container-fluid" style="width:500px">
	
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title">Change color</h3>
		</div>
		<div class="panel-body">
			<form role="form" method="post" action="change_color_check.php">
				<div class="form-group">
					<label for="add_user_name">Please select the component:</label>
				</div>
                                <div class="form-group">
					<select name="component_name">
					<option value="Chi">Chi</option>
					<option value="Tian">Tian</option>
                                        <option value="Di">Di</option>
					<option value="Ren">Ren</option>
					</select>
				</div>
                                <div class="form-group">
					<label for="add_user_name">Please select the color:</label>
				</div>
                                <div class="form-group">
					<select name="component_color">
					<option value="G">Green</option>
					<option value="B">Blue</option>
                                        <option value="Y">Yellow</option>
					<option value="R">Red</option>
					</select>
				</div><!--
				<div class="checkbox">
					<label><input type="checkbox"> Remember me</label>
				</div>-->
				<button name="login" type="submit" class="btn btn-default">Submit</button>
                               <button name="cancel" type="submit" class="btn btn-default">Cancel</button>
                              <button name="autoChange" type="submit" class="btn btn-default">Automatically Update</button>
			</form>
		</div>
	</div>
	
	</div>
	<!-- /#wrapper -->

	<!-- jQuery -->
	<script src="js/jquery.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>

</body>

</html>