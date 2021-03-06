<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="../styles/employee.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=Raleway" rel="stylesheet">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
	crossorigin="anonymous">
<title>Manager ERS Portal</title>
</head>
<body class="container">
<%@ page import="com.gtrain.model.Manager" %>
<% Manager manager = (Manager) request.getSession().getAttribute("authorizedUser"); %>
	<div class="row">
		<h2>Welcome to the Manager Reimbursement Portal, <%= manager.getFirstname() %></h2>
		
		<div class="navbar">
			<div class="navbar-header">
				<ul class="nav navbar-nav" >
					<li><a  href="http://ec2-52-90-166-210.compute-1.amazonaws.com:8080/ERS/html/manager.do" >Home</a></li>
					<li><a  href="http://ec2-52-90-166-210.compute-1.amazonaws.com:8080/ERS/html/allEmployees.do">My Employees</a></li>
					<li><a  href="http://ec2-52-90-166-210.compute-1.amazonaws.com:8080/ERS/html/resolvedRequests.do" >Resolved Requests</a></li>
					<li><a href="http://ec2-52-90-166-210.compute-1.amazonaws.com:8080/ERS/logout.do">Log Out</a></li>
				</ul>
			</div>
		</div>
	<h2>Current Pending Requests</h2>
	<table class="table table-bordered table-striped table-hover">
		<thead class="table-header">
			<tr>
				<th scope="col">Request ID</th>
				<th scope="col">Amount</th>
				<th scope="col">Reason</th>
				<th scope="col">Time Created</th>
			</tr>		
		</thead>
		<tbody id="tblCurrentPendingRequests">
			
		</tbody>	
	</table>
	
	
	</div>
	
	
<script type="text/javascript" src="../js/managerCompanion.js"></script>
<script type="text/javascript" src="../js/managerRouting.js"></script>
</body>
</html>