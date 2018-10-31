<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
<c:import url="/WEB-INF/css/bootstrap.min.css"/>
<c:import url="/WEB-INF/css/font-awesome.css"/>
<c:import url="/WEB-INF/css/main.css"/>
</style>
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
        </div>
    </header>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <div class="label label-default pull-right">
                        id: ${computer.id}
                    </div>
                    <h1>Edit Computer</h1>

                    <form action="editcomputer" method="POST" id="form">
                        <input type="hidden" value="${computer.id}" name="id" id="id"/>
                        <fieldset>
                            <div class="form-group">
                                <label for="computerName">Computer name</label>
                                <input type="text" class="form-control" name="computername" id="computername" placeholder="${computer.name}">
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" name="introduced" id="introduced" placeholder="${computer.introduced}">
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" name="discontinued" id="discontinued" placeholder="${computer.discontinued}">
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" name="companyId" id="companyId" >
                                    <option value="0">--</option>
                                	<c:forEach varStatus="loopCounter" items="${companies}" var="company">
                                    	<option value="${company.id}">${company.name}</option>
                                    </c:forEach>
                                </select>
                            </div>            
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" value="Edit" class="btn btn-primary">
                            or
                            <a href="dashboard" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
    <script type="text/javascript" src='<c:url value="/js/jquery.min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/bootstrap.min.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/dashboard.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/js/validation.js"/>'></script>
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