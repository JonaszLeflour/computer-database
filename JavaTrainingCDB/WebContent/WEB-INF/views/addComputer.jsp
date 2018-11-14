<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- Bootstrap -->
<style>
<c:import url="/WEB-INF/css/bootstrap.min.css"/>
<c:import url="/WEB-INF/css/font-awesome.css"/>
<c:import url="/WEB-INF/css/main.css"/>
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
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1>Add Computer</h1>
                    <form action="addcomputer" method="POST" id="form">
                        <fieldset>
                        
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" name="computername" id="computername" placeholder="Computer name">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" name="introduced" id="introduced" placeholder="Introduced date">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" name="discontinued" id="discontinued" placeholder="Discontinued date">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyid" name="companyid" >
                                	<option value="0">--</option>
                                	<c:forEach varStatus="loopCounter" items="${companies}" var="company">
                                    	<option value="${company.id}">${company.name}</option>
                                    </c:forEach>
                                </select>
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or 
                            <a href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
    <script src="./js/jquery.min.js"/></script>
	<script src="./js/bootstrap.min.js"/></script>
	<script src="./js/dashboard.js"/></script>
	<script src="./js/validation.js"/></script>
    <script type="text/javascript" >
	    $.validator.addMethod("dateValid", function(value, element, params) {
	    	if(value === '' || $(params).val() === ''){
	    		return true
	    	}
	    	
	    	if (!/Invalid|NaN/.test(new Date(value))) {
	    		return new Date(value) > new Date($(params).val());
	    	}
	     	return isNaN(value) && isNaN($(params).val())
	    			|| (Number(value) > Number($(params).val()));
	    }, 'Erreur de saisie');
	    
	    $(document).ready(function(){
			$("#form").validate({
				rules: {
					computername: {required: true},
					discontinued: { dateValid: "#introduced",required : false }
				},
				messages: {
						name: "Required Field"
					}
				});
			});
    </script>
</body>
</html>