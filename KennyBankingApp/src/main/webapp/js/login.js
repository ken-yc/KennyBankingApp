$('#loginForm').submit(function(event) {
    event.preventDefault();

    var postData = $(this).serialize();
    $.post('login', postData, function(data, status){
    	console.log('Data: ' + data);
    	console.log('Status: ' + status);
    	document.location.replace('welcome.html');
    },'json')
    .fail(function(){
    	console.log('Failed');
    	$('#errormsg').html('Invalid Login');
    });

});