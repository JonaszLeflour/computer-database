<!DOCTYPE html>
<html>
<head>
	<title>Computer Database</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<link rel="stylesheet" href="/css/bootstrap.min.css">
	<link rel="stylesheet" href="/css/font-awesome.css">
	<link rel="stylesheet" href="/css/main.css">
    <style>
	<jsp:include page="/WEB-INF/css/bootstrap.min.css"/>
	<jsp:include page="/WEB-INF/css/font-awesome.css"/>
	<jsp:include page="/WEB-INF/css/main.css"/>
	</style>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">	
			<div class="alert alert-danger">
				Error 500: An error has occured!
				<br/>
				<!-- stacktrace -->
			</div>
		</div>
	</section>

	<script><jsp:include page="/WEB-INF/js/jquery.min.js"/></script>
	<script><jsp:include page="/WEB-INF/js/bootstrap.min.js"/></script>
	<script><jsp:include page="/WEB-INF/js/dashboard.js"/></script>

</body>
</html>