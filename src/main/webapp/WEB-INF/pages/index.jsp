<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="press release with topic models">
<meta name="author" content="thang nguyen">
<link rel="shortcut icon"
	href="<c:url value="/resources/images/favicon.ico"/>">

<title>Press Visualization</title>

<!-- Bootstrap core CSS -->
<link href="<c:url value="/resources/css/bootstrap.min.css"/>"
	rel="stylesheet">

<!-- JQuery UI CSS -->
<link href="<c:url value="/resources/css/jquery-ui-1.10.4.custom.css"/>"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="<c:url value="/resources/css/index.css"/>" rel="stylesheet">
<link href="<c:url value="/resources/css/theme.blue.css"/>"
	rel="stylesheet">
<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>
	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation"
		id="topheader">

		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".navbar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a href="#" onclick="location.reload(true); return false;"
					class="navbar-brand" title="refresh page">Press Visualization </a>

			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="#" id="about" title="about">About</a></li>
					<li><a href="#" id="contact" title="contact">Contact</a></li>
				</ul>

			</div>

		</div>
	</div>

	<div class="container">
		<div class="algorithmArea">
			<h1>---------DEMO------------</h1>
			<div class="ui-widget">
				<label for="algorithmName">Algorithm: </label> <input id="algorithmName">
				<button id="showVisualization" class="btn">Show Visualization</button>
			</div>
		</div>

	</div>
	<!-- /.container -->
	<!-- context of this page -->
	<script>
		var ctx = "${pageContext.request.contextPath}"
	</script>

	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="<c:url value="/resources/js/jquery-1.11.0.min.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery-ui-1.10.4.custom.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.js"/>"></script>
	<script src="<c:url value="/resources/js/docs.min.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery.tablesorter.min.js"/>"></script>
	<script
		src="<c:url value="/resources/js/jquery.tablesorter.widgets.min.js"/>"></script>
	<script src="<c:url value="/resources/js/index.js"/>"></script>
</body>
</html>
