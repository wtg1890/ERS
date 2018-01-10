<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<meta charset="UTF-8">
<title>All Employees</title>
</head>
<body class="container">

	<div class="row">
		<h2>All Employees</h2>
			<div class="navbar">
			<div class="navbar-header">
				<ul class="nav navbar-nav" >
					<li><a  href="http://localhost:8080/ERS/html/manager.jsp" onclick="home();">Home</a></li>
					<li><a  href="http://localhost:8080/ERS/html/allEmployees.jsp" onclick="myEmployees();">My Employees</a></li>
					<li><a  href="http://localhost:8080/ERS/html/resolvedRequests.jsp" onclick="resolvedRequests();">Resolved Requests</a></li>
					<li><a href="http://localhost:8080/ERS/logout" onclick="logout();">Log Out</a></li>
				</ul>
			</div>
		</div>
		<table class="table table-bordered table-striped table-hover">
		<thead class="table-header">
			<tr>
				<th scope="col">ID</th>
				<th scope="col">Username</th>
				<th scope="col">Name</th>
				<th scope="col">Email</th>
			</tr>		
		</thead>
		<tbody id="allEmployees">
			
		</tbody>	
	</table>
	
	
	
	
	
	</div>
<script type="text/javascript" src="../js/managerAllEmployees.js"></script>
</body>
</html>