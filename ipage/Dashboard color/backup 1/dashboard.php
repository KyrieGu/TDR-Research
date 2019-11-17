<?php
include_once("conn_db.php");
include_once("menu.php");
?>

<!-- Custom CSS -->
<link href="css/half-slider.css" rel="stylesheet">

<!-- Page Heading -->

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">
			Dashboard <small><?php
								if (($_SESSION['selectuser'])) {
									echo "Showing Data For Email: ";
									echo $_SESSION['selectuser'];
								} else {
									echo "Showing Data";
								}

								$flag = 0;
								?></small>
		</h1>
		<ol class="breadcrumb">
			<li class="active">
				<i class="fa fa-dashboard"></i>
				Statistics Overview

				<form method="POST" action="dashboard1.php">
					<p> sql: <input type="text" name="sql"></p>

					<input type="submit" value="submit" name="submit">
				</form>
			</li>
		</ol>
	</div>
</div>

<!-- /.row -->
<header id="myCarousel-lg" class="carousel slide hidden-xs">
	<!-- Wrapper for Slides -->
	<div class="carousel-inner">

		<div class="item active">
			<!-- air temperature -->
			<div class="col-xs-3">
				<?php if ($_SESSION['Chi_color'] == 'B') : ?>
					<div class="panel panel-primary">
					<?php elseif ($_SESSION['Chi_color'] == 'Y') : ?>
						<div class="panel panel-yellow">
						<?php elseif ($_SESSION['Chi_color'] == 'R') : ?>
							<div class="panel panel-red">
							<?php else : ?>
								<div class="panel panel-primary">
								<?php endif; ?>
								<div class="panel-heading">
									<div class="row">
										<div class="col-xs-3">
											<i class="fa fa-eyedropper fa-5x"></i>
										</div>
										<div class="col-xs-9 text-right">
											<div class="huge">Chi 氣</div>
										</div>
									</div>
								</div>
								<a href="source.php?type=ChiMonitor">
									<div class="panel-footer">
										<span class="pull-left">View Details</span>
										<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
										<div class="clearfix"></div>
									</div>
								</a>
							</div>
						</div>
						<!-- fertilizer -->
						<div class="col-xs-3">
							<?php if ($_SESSION['Tian_color'] == 'B') : ?>
								<div class="panel panel-primary">
								<?php elseif ($_SESSION['Tian_color'] == 'Y') : ?>
									<div class="panel panel-yellow">
									<?php elseif ($_SESSION['Tian_color'] == 'R') : ?>
										<div class="panel panel-red">
										<?php else : ?>
											<div class="panel panel-primary">
											<?php endif; ?>
											<div class="panel-heading">
												<div class="row">
													<div class="col-xs-3">
														<i class="fa fa-eyedropper fa-5x"></i>
													</div>
													<div class="col-xs-9 text-right">
														<div class="huge">Tian 天</div>
													</div>
												</div>
											</div>
											<a href="source_notChi.php?type=Tian">
												<div class="panel-footer">
													<span class="pull-left">View Details</span>
													<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
													<div class="clearfix"></div>
												</div>
											</a>
										</div>
									</div>
									<!--light-->
									<div class="col-xs-3">
										<?php if ($_SESSION['Di_color'] == 'B') : ?>
											<div class="panel panel-primary">
											<?php elseif ($_SESSION['Di_color'] == 'Y') : ?>
												<div class="panel panel-yellow">
												<?php elseif ($_SESSION['Di_color'] == 'R') : ?>
													<div class="panel panel-red">
													<?php else : ?>
														<div class="panel panel-primary">
														<?php endif; ?>
														<div class="panel-heading">
															<div class="row">
																<div class="col-xs-3">
																	<i class="fa fa-heartbeat fa-5x"></i>
																</div>
																<div class="col-xs-9 text-right">
																	<div class="huge">Di 地</div>
																</div>
															</div>
														</div>
														<a href="source_notChi.php?type=Di">
															<div class="panel-footer">
																<span class="pull-left">View Details</span>
																<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																<div class="clearfix"></div>
															</div>
														</a>
													</div>
												</div>
												<!--moisture-->
												<div class="col-xs-3">
													<?php if ($_SESSION['Ren_color'] == 'B') : ?>
														<div class="panel panel-primary">
														<?php elseif ($_SESSION['Ren_color'] == 'Y') : ?>
															<div class="panel panel-yellow">
															<?php elseif ($_SESSION['Ren_color'] == 'R') : ?>
																<div class="panel panel-red">
																<?php else : ?>
																	<div class="panel panel-primary">
																	<?php endif; ?>
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-medkit fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">Ren 人</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=Ren">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
														</div>


														<div class="item">
															<!-- air temperature -->
															<div class="col-xs-3">
																<div class="panel panel-primary">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-eyedropper fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">AIR TEMP</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=temp">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
															<!-- fertilizer -->
															<div class="col-xs-3">
																<div class="panel panel-primary">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-eyedropper fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">FERTILIZER</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=fertilizer">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
															<!--light-->
															<div class="col-xs-3">
																<div class="panel panel-green">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-heartbeat fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">LIGHT</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=light">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
															<!--moisture-->
															<div class="col-xs-3">
																<div class="panel panel-yellow">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-medkit fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">MOISTURE</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=moisture">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
														</div>

														<div class="item">
															<!--spo2-->
															<div class="col-xs-3">
																<div class="panel panel-primary">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-eyedropper fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">SPO2</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=spo2">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
															<!--systolic-->
															<div class="col-xs-3">
																<div class="panel panel-red">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-stethoscope fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">SYSTOLIC</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=systolic">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
															<!--diastolic-->
															<div class="col-xs-3">
																<div class="panel panel-red">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-stethoscope fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">DIASTOLIC</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=diastolic">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
															<!--pulse-->
															<div class="col-xs-3">
																<div class="panel panel-red">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-stethoscope fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">PULSE</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=pulse">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>
														</div>

														<div class="item">
															<!--ekg-->
															<div class="col-xs-3">
																<div class="panel panel-red">
																	<div class="panel-heading">
																		<div class="row">
																			<div class="col-xs-3">
																				<i class="fa fa-stethoscope fa-5x"></i>
																			</div>
																			<div class="col-xs-9 text-right">
																				<div class="huge">EKG</div>
																			</div>
																		</div>
																	</div>
																	<a href="source_notChi.php?type=EKG">
																		<div class="panel-footer">
																			<span class="pull-left">View Details</span>
																			<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																			<div class="clearfix"></div>
																		</div>
																	</a>
																</div>
															</div>

															<!--Bloodpressure-->
															<div class="col-xs-3">
																<?php
																$q = $_SESSION['latest'];
																$result = mysql_query($q);
																$rows = array();
																//calculate the average value
																$total = 0;
																while ($row = mysql_fetch_assoc($result)) {
																	//get the latest time
																	if ($total == 0) {
																		$temstamp = $row['datetime'];
																	}
																	$total += $row['value'];
																}
																$average = $total / mysql_num_rows($result);

																//decide the color
																if ($average <= 25) {
																	$color = "blue";
																}
																if ($average <= 50 && $average > 25) {
																	$color = "green";
																}
																if ($average <= 75 && $average > 50) {
																	$color = "yellow";
																}
																if ($average <= 100 && $average > 75) {
																	$color = "red";
																}

																//fetch the color pattern
																$patterns = "SELECT * FROM patterns WHERE patterns.pattern_ID BETWEEN 101 AND 104";
																$result = mysql_query($patterns);
																$rows = array();

																//get the pattern_ID
																while ($row = mysql_fetch_assoc($result)) {
																	if ($color == $row['descrption']) {
																		$ID = $row['pattern_ID'];
																		break;
																	}
																}

																//fetch the previous nodeID
																$previous = "SELECT * FROM events WHERE events.EventGraph_ID = $uid AND users.uid = $uid ";
																$previous = $previous . "order by previous_nodeID desc limit 1";
																$result = mysql_query($patterns);
																$rows = array();
																$row = mysql_fetch_assoc($result);
																if (is_null($row['EventGraph_ID'])) {
																	$previous_ID = NULL;
																	$node_ID = 1;
																} else {
																	$previous_ID = $row['previous_nodeID'];
																	$node_ID = previous_ID + 1;
																}
																//ready to insert into the events graph
																$EventGraph_ID = $uid;
																$node_value = $average;
																$pattern_ID = $ID;
																$strength = 0;

																$sql = "INSERT INTO events VALUES ('$EventGraph_ID', '$node_ID', '$node_value', '$previous_ID', '$pattern_ID', '$strength', '$temstamp') ";
																mysql_query($sql);
																?>

																<?php if ($average <= 25) : ?>
																	<div class="panel panel-primary">
																	<?php elseif ( $average > 25 && $average <= 50) : ?>
																		<div class="panel panel-green">
																		<?php elseif ( $average > 50 && $average <= 75) : ?>
																			<div class="panel panel-yellow">
																			<?php elseif ( $average > 75) : ?>
																				<div class="panel panel-red">
																				<?php endif; ?>
																				<div class="panel-heading">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-stethoscope fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">Brainwave</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChibrainwave.php?type=brainwave">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>

																		<!--gaze-->
																		<div class="col-xs-3">
																			<div class="panel panel-red">
																				<div class="panel-heading">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-stethoscope fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">Gaze</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChigaze.php?type=ReadingBehavior">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>
																		<!--temp-->
																		<div class="col-xs-3">
																			<div class="panel panel-green">
																				<div class="panel-heading">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-heartbeat fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">TEMP</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChi.php?type=temp">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>
																		<!--1-->
																		<!--1-->
																		<!--1-->
																		<!--1-->
																		<!--1-->
																		<!--1-->

																	</div>

																	<div class="item">
																		<div class="col-xs-3">
																			<div class="panel panel-green">
																				<div class="panel-heading fatigue" id="fatigue">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-heartbeat fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">fatigue</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChi.php?type=fatigue">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>

																		<div class="col-xs-3">
																			<div class="panel panel-green">
																				<div class="panel-heading">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-heartbeat fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">pulse</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChi.php?type=pulse">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>

																		<div class="col-xs-3">
																			<div class="panel panel-green">
																				<div class="panel-heading weakBreadth" id="weakbreadth">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-heartbeat fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">weakBreadth</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChi.php?type=weakBreadth">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>

																		<div class="col-xs-3">
																			<div class="panel panel-green">
																				<div class="panel-heading sweaty" id="sweaty">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-heartbeat fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">sweaty</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChi.php?type=sweaty">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>
																	</div>

																	<div class="item">
																		<div class="col-xs-3">
																			<div class="panel panel-green">
																				<div class="panel-heading chiTotal" id="chitotal">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-heartbeat fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">chiTotal</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChi.php?type=chiTotal">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>

																		<div class="col-xs-3">
																			<div class="panel panel-green">
																				<div class="panel-heading tongue" id="tongue">
																					<div class="row">
																						<div class="col-xs-3">
																							<i class="fa fa-heartbeat fa-5x"></i>
																						</div>
																						<div class="col-xs-9 text-right">
																							<div class="huge">tongue</div>
																						</div>
																					</div>
																				</div>
																				<a href="source_notChi.php?type=tongue">
																					<div class="panel-footer">
																						<span class="pull-left">View Details</span>
																						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
																						<div class="clearfix"></div>
																					</div>
																				</a>
																			</div>
																		</div>
																		<!--1-->
																		<!--1-->
																		<!--1-->
																		<!--1-->
																		<!--1-->
																		<!--1-->

																	</div>

																	<!-- Indicators -->
																	<ol class="carousel-indicators">
																		<li data-target="#myCarousel-lg" data-slide-to="0" class="active"></li>
																		<li data-target="#myCarousel-lg" data-slide-to="1"></li>
																		<li data-target="#myCarousel-lg" data-slide-to="2"></li>
																	</ol>
																	<!-- Controls -->
																	<a class="left carousel-control" href="#myCarousel-lg" data-slide="prev">
																		<span class="icon-prev"></span>
																	</a>
																	<a class="right carousel-control" href="#myCarousel-lg" data-slide="next">
																		<span class="icon-next"></span>
																	</a>


																</div>



</header>


<header id="myCarousel-sm" class="carousel slide visible-xs-block">
	<!-- Wrapper for Slides -->
	<div class="carousel-inner">
		<div class="col-lg-12 item active">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-eyedropper fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">Chi 氣</div>
						</div>
					</div>
				</div>
				<a href="source.php?type=ChiMonitor">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!-- fertilizer -->
		<div class="col-lg-12 item">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-eyedropper fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">Tian 天</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=Tian">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!--light-->
		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">Di 地</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=Di">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!--moisture-->
		<div class="col-lg-12 item">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-medkit fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">Ren 人</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=Ren">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<!-- air temperature -->
		<div class="col-lg-12 item">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-eyedropper fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">AIR TEMP</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=temp">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!-- fertilizer -->
		<div class="col-lg-12 item">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-eyedropper fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">FERTILIZER</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=fertilizer">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!--light-->
		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">LIGHT</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=light">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!--moisture-->
		<div class="col-lg-12 item">
			<div class="panel panel-yellow">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-medkit fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">MOISTURE</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=moisture">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<!--spo2-->
		<div class="col-lg-12 item">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-eyedropper fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">SPO2</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=spo2">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!--systolic-->
		<div class="col-lg-12 item">
			<div class="panel panel-red">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-stethoscope fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">SYSTOLIC</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=systolic">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!--diastolic-->
		<div class="col-lg-12 item">
			<div class="panel panel-red">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-stethoscope fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">DIASTOLIC</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=diastolic">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!--pulse-->
		<div class="col-lg-12 item">
			<div class="panel panel-red">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-stethoscope fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">PULSE</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=pulse">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<!--ekg-->
		<div class="col-lg-12 item">
			<div class="panel panel-red">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-stethoscope fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">EKG</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=EKG">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<!-- bloodpressure -->
		<div class="col-lg-12 item">
			<div class="panel panel-red">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-stethoscope fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">Brainwave</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=brainwave">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<div class="col-lg-12 item">
			<div class="panel panel-red">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-stethoscope fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">Gaze</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=ReadingBehavior">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>


		<!--temp-->
		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">TEMP</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=temp">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>
		<!---------------------CHI---->

		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading tongue" id="tongue">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">tongue</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=tongue">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading fatigue" id="fatigue">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">fatigue</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=fatigue">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">pulse</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=pulse">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading weakBreadth" id="weakbreadth">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">weakBreadth</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=weakBreadth">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading sweaty" id="sweaty">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">sweaty</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=sweaty">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>

		<div class="col-lg-12 item">
			<div class="panel panel-green">
				<div class="panel-heading chiTotal" id="chitotal">
					<div class="row">
						<div class="col-xs-3">
							<i class="fa fa-heartbeat fa-5x"></i>
						</div>
						<div class="col-xs-9 text-right">
							<div class="huge">chiTotal</div>
						</div>
					</div>
				</div>
				<a href="source_notChi.php?type=chiTotal">
					<div class="panel-footer">
						<span class="pull-left">View Details</span>
						<span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
						<div class="clearfix"></div>
					</div>
				</a>
			</div>
		</div>



		<!----------------------CHI---->>
		<!-- Controls -->
		<a class="left carousel-control" href="#myCarousel-sm" data-slide="prev">
			<span class="icon-prev"></span>
		</a>
		<a class="right carousel-control" href="#myCarousel-sm" data-slide="next">
			<span class="icon-next"></span>
		</a>


	</div>
</header>


<script>
	$("#myCarousel-lg").carousel();
	$("#myCarousel-sm").carousel();
</script>

</div>
<!-- /.container-fluid -->

<!-- /.row -->
<table class="table table-bordered table-hover table-striped">
	<tr>
		<th>Rid</th>
		<th>Uid</th>
		<th>Date</th>
		<th>Source</th>
		<th>Type</th>
		<th>Reading</th>
		<th>Originator</th>
	</tr>
	<?php
	$q = $_SESSION['query'];
	$result = mysql_query($q);
	$rows = array();
	while ($row = mysql_fetch_assoc($result)) {
		echo "<tr><th>" . $row["rid"] . "</th><th>" . $row["uid"] . "</th><th>" . $row["datetime"] . "</th><th>" . $row["source"] . "</th><th>" . $row["type"] . "</th><th>" . $row["value"] . "</th><th>" . $row["originator"] . "</th></tr>";
	}
	?>
</table>

</div>
<!-- /#page-wrapper -->

</div>
<!-- /#wrapper -->
<script>
	var ah = document.getElementById("ah");
	var xhp = new XMLHttpRequest();
	xhp.open("GET", "newchangbgcolor.php", true);
	xhp.send();
	xhp.onreadystatechange = function() {
		if (xhp.readyState == 4 && xhp.status == 200) {
			var str = xhp.responseText;
			var arr = str.split("|");
			console.log(arr[3]);
			document.getElementById("tongue").style.backgroundColor = arr[0];
			document.getElementById("fatigue").style.backgroundColor = arr[1];
			document.getElementById("sweaty").style.backgroundColor = arr[2];
			document.getElementById("weakbreadth").style.backgroundColor = arr[3];
			document.getElementById("chitotal").style.backgroundColor = arr[4];
		}
	}
</script>
</body>

</html>