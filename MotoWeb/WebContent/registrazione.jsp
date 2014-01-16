<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<meta http-equiv="Content-Language" content="it-it" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" >
	<link rel=stylesheet type="text/css" href=css/style.css media=all>
	<script type="text/javascript" src="js/jquery.min.js"></script>
	<script type="text/javascript" src="js/jquery.validate.js"></script>
	<title>Moto: Movie Theater Office</title>
</head>

<body>
<center>


<script type="text/javascript">
$(document).ready(function(){		
	
	// TYPE VALUE CONTROLLER
	$('#type').change(function() {
		if($('#type').val()=="TicketSeller"){
			$('#WebCustomerData').slideUp();
		} else {
			$('#WebCustomerData').slideDown();
		}
	});

	
	jQuery.validator.messages.required = "";
 
	
	// VALIDATOR
	$("#register").validate({
		onfocusout: false,
		errorPlacement: function(error, element) {
			error.appendTo( element.parent("div").next("div") );	//.next("div")
		},
		invalidHandler: function(form, validator) {
			scrollTo(":input.error:first", 250.0, 500);
		},
		rules: {
			email: {
				required: {
					depends: function(element) {
						return ($('#type').val() == 'WebCustomer');
						}
				},
				email: true
			},
			cf: {
				required: {
					depends: function(element) {
						return ($('#type').val() == 'WebCustomer');
						}
				},
				rangelength: [16, 16]				
			}
	    },
		messages: {
			email: {
				required: "",
			   	   email: " Indicare un indirizzo email valido."
			},
	    	cf: {
	    		required: "",
	    		rangelength: "Il codice fiscale è formato da 16 caratteri."		
	    	}
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
	if (request.getSession().getAttribute("register_error") != null){
		
		// Generate error message
		String error_msg = "";
		if (request.getSession().getAttribute("register_error") == "UserAlreadyExists")	
			error_msg = "<h1>Registrazione fallita</h1>Il nome utente <b>" + request.getParameter("username") + "</b> è già stato utilizzato.";
			
		else if (request.getSession().getAttribute("register_error") == "UserTypeDoesNotExistException")
			error_msg = "<h1>Registrazione fallita</h1>La tipologia di utenza selezionata non esiste.";
		
		else if (request.getSession().getAttribute("register_error") == "EmptyField")
			error_msg = "<h1>Registrazione fallita</h1>E' necessario completare correttamente tutti i campi riportati.";
		else
			error_msg = "<h1>Registrazione fallita</h1>" + request.getSession().getAttribute("register_error");
			
	%>
		<div class=error_box id="error_box" style='width: 595px'>
			<center><%=error_msg %></center>
		</div><br>
	<%
	} else {
	%>
		<div class="error_box" id="error_box" style='width: 595px; display: none'></div><br>
	<%		
	}
	%>

	
	
	<!-- Main login box -->	
	<div id="access_box_main">
	
		<!-- Logo -->
		<div id="access_box_logo">
			<img src=pics/logo.png alt="Moto: Movie Theater Office." title="Moto: Movie Theater Office.">
		</div>
	
	
		<!-- Login Box -->
		<div id="access_box_form">
		
			<h1>Registrazione</h1>
			
		
			<form action="Register" id="register" method="post" class="access">
			
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
		
				<!-- User Type -->
				<div class="access" style="margin-top: 10px; padding-top: 10px; padding-bottom: 15px; border-top: dotted 1px #999999; border-bottom: dotted 1px #999999;">
					<div>Tipologia utente</div>
					<div>					
						<select name="type" id="type" title="Tipologia utente" style="width: 300px"> 
						<option value="WebCustomer" selected>Web Customer</option>
						<option value="TicketSeller" >Ticket Seller</option>
						</select>
					</div>
				</div>
			
				<!-- Name -->
				<div class="access" style="margin-top: 10px">
					<div>Nome</div>
					<div><input type="text" id="name" name="name" title="Nome" class="required" /></div>  
				</div>
				
				<!-- Surname -->
				<div class="access" style="margin-top: 10px">
					<div>Cognome</div>
					<div><input type="text" id="surname" name="surname" title="Cognome" class="required" /></div>
				</div>	
			
				<div id="WebCustomerData">
			
					<!-- Email -->
					<div class="access" style="position: relative; margin-top: 10px">
						<div>Email</div>
						<div><input type="text" id="email" name="email" title="Email" /></div>  
						<div style='width: 205px; text-align: right; position: absolute; top: 15px; left: -238px; display: inline; height: 25px; line-height: 25px; margin-left: 10px; font-size: 11px; font-style: italic; color: #ff0000'></div>
					</div>
					
					<!-- Codice Fiscale -->
					<div class="access" style="position: relative; margin-top: 10px">
						<div>Codice Fiscale</div>
						<div><input type="text" id="cf" name="cf" title="Codice Fiscale" /></div>
						<div style='width: 205px; text-align: right; position: absolute; top: 15px; left: -238px; display: inline; height: 25px; line-height: 25px; margin-left: 10px; font-size: 11px; font-style: italic; color: #ff0000'></div>
					</div>	
				
				</div>
							
			
				<!-- Login page -->
				<div style="float: left; line-height: 32px; color: #333333; margin-top: 10px"">
					Torna alla pagina di <a href=index.jsp><b>Login</b></a>.
				</div>
			
				<!-- Submit and Reset button -->
				<div style="float: right; margin-left: 5px; margin-top: 10px">
					<button type="submit" id="submit" name="submit">Registrati</button>
				</div>
				
				<div style="float: right; margin-top: 10px">
					<button type="reset">Annulla</button>
				</div>
			
			
			<div style="clear: both;"></div>
			</form>
			
						
		</div>
				
	</div>

</div>

</center>
</body>
</html>