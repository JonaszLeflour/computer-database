<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet" media="screen" href="./css/bootstrap.min.css">
<link rel="stylesheet" media="screen" href="./css/font-awesome.css">
<link rel="stylesheet" media="screen" href="./css/main.css">

<!--<link rel="stylesheet" type="text/css" href="/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="/css/main.css">

<link rel="stylesheet" type="text/css" href="/WEB-INF/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/WEB-INF/css/font-awesome.css">
<link rel="stylesheet" type="text/css" href="/WEB-INF/css/main.css">

<link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.min.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/font-awesome.css" />">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css" />"> -->


</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application - Computer
				Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${nbcomputers} Computer(s) found</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="dashboard" method="GET"
						class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer" href="addcomputer">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="dashboard" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th>Computer name</th>
						<th>Introduced date</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date</th>
						<!-- Table header for Company -->
						<th>Company</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach varStatus="loopCounter" items="${page}"
						var="computerResult">
						<tr>
							<td class="editMode"><input type="checkbox"
								form="deleteForm" name="deletecomputers" class="cb"
								value="${computerResult.id}"></td>
							<td><a href='editcomputer?id=${computerResult.id}'>${computerResult.name}</a>
							</td>
							<td>${computerResult.introduced}</td>
							<td>${computerResult.discontinued}</td>
							<td>${computerResult.company}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<c:if test="${currentPage > 1}">
					<li><a href="dashboard?page=1" aria-label="Previous"> <span
							aria-hidden="true">&laquo;</span>
					</a></li>
				</c:if>

				<c:if test="${currentPage > 2}">
					<li><a href="dashboard?page=${currentPage-2}">${currentPage-2}</a></li>
				</c:if>

				<c:if test="${currentPage > 1}">
					<li><a href="dashboard?page=${currentPage-1}">${currentPage-1}</a></li>
				</c:if>


				<li><a href="dashboard?page=${currentPage}">${currentPage}</a></li>

				<c:if test="${currentPage+1 <= nbPages}">
					<li><a href="dashboard?page=${currentPage+1}">${currentPage+1}</a></li>
				</c:if>

				<c:if test="${currentPage+2 <= nbPages}">
					<li><a href="dashboard?page=${currentPage+2}">${currentPage+2}</a></li>
				</c:if>

				<c:if test="${currentPage < nbPages}">
					<li><a href="dashboard?page=${nbPages}" aria-label="Next">
							<span aria-hidden="true">&raquo;</span>
					</a></li>
				</c:if>

			</ul>

		</div>
		<div class="btn-group btn-group-sm pull-right" role="group">
			<button type="button" class="btn btn-default">10</button>
			<button type="button" class="btn btn-default">50</button>
			<button type="button" class="btn btn-default">100</button>
		</div>
	</footer>

	<script src="./js/jquery.min.js"/></script>
	<script src="./js/bootstrap.min.js"/></script>
	<script src="./js/dashboard.js"/></script>

</body>
</html>