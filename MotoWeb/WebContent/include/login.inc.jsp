
<script type="text/javascript">
$(document).ready(function(){		
	
	
	jQuery.validator.messages.required = "";
	
	// VALIDATOR
	$("#login").validate({
		onfocusout: false,
		errorPlacement: function(error, element) {
			error.appendTo( element.parent("div").next("div") );	//.next("div")
		},
		invalidHandler: function(form, validator) {
			scrollTo(":input.error:first", 250.0, 500);
		},
		showErrors: function(errorMap, errorList) {
				
			var errors = this.numberOfInvalids();
			if (errors) {
			
				var message = errors == 1
				? "<b>Attenzione:</b> è necessario completare correttamente il campo evidenziato."
				: "<b>Attenzione:</b> è necessario completare correttamente i campi evidenziati.";

				if ($("#error_box").is(":hidden")) {$("#error_box").slideDown();}
				$("#error_box").html(message);
				
			} else {
				$("#error_box").slideUp();
			}	
			
			this.defaultShowErrors();
		}

	})
	
	
});
</script>

<div style="margin-top: 50px;">


	<!-- Error box -->
	<% 
	// getAttribut:  Login servlet
	// getParameter: JSP Forward on ui.inc.jps or home.inc.jsp
	if (request.getSession().getAttribute("login_error") != null || request.getParameter("error") != null || (request.getParameter("action") != null && !(((String)request.getParameter("action")).equals("logout")))){
		
		// Generate error message
		String error_msg = "";
		if (request.getSession().getAttribute("login_error") == "UserNotFound" || request.getSession().getAttribute("login_error") == "EmptyField" )	
			error_msg = "<h1>Accesso non autorizzato</h1>Si prega di verificare <b>nome utente</b> e <b>password</b>.";
		else if (request.getParameter("error") != null || (request.getParameter("action") != null && !(((String)request.getParameter("action")).equals("logout"))))
			error_msg = "<h1>Accesso non autorizzato</h1>E' necessario autenticarsi al sistema.";
		else 
			error_msg = "<h1>Accesso non autorizzato</h1>"+ request.getSession().getAttribute("login_error");	
			
	%>
		<div class=error_box id="error_box" style='width: 595px'>
			<center><%=error_msg %></center>
		</div><br>
	<%
		request.getSession().setAttribute("login_error", null);
	
	}else {
	%>
		<div class="error_box" id="error_box" style='width: 595px; display: none'></div><br>
	<%		
	}
	%>
	

	<!-- Main login box -->	
	<div id="access_box_main">
	
		<!-- Logo -->
		<div id="access_box_logo">
			<img src="${pageContext.request.contextPath}/pics/logo.png" alt="Moto: Movie Theater Office." title="Moto: Movie Theater Office.">
		</div>
	
	
		<!-- Login Box -->
		<div id="access_box_form">
		
			<h1>Accedi a Moto</h1>
			
			<form action="${pageContext.request.contextPath}/Login" id="login" method="post" class="access">
			<input type="hidden" name="action" value="login">
			
				<!-- Username -->
				<div class="access" style="margin-top: 10px">
					<div>Nome utente</div>
					<div><input type="text" id="username" name="username" title="Nome utente" class="required" /></div>  
				</div>
				
				<!-- Password -->
				<div class="access" style="margin-top: 10px">
					<div>Password</div>
					<div><input type="password" id="password" name="password" title="Password" class="required" /></div>
				</div>
			
				<!-- Register Link -->
				<div style="float: left; line-height: 32px; color: #333333; margin-top: 10px"">
					Se non sei ancora registrato, <a href="${pageContext.request.contextPath}/registrazione.jsp"><b>registrati</b></a>.
				</div>
				
				<!-- Submit Button -->
				<div style="float: right; margin-top: 10px">
					<button type="submit">Accedi</button>
				</div>
				<div style="clear: both;"></div>
				
			</form>
			
		</div>
				
	</div>


</div>