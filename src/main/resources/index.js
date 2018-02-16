$(document).ready(function() {
    $('#button.now').on('click', function(event) {
        $.ajax({
            url: '/now',
            success: function(result) {
                console.log('current time: ' + result);
                $('#text.now').html('<strong>' + result + '</strong>');
            }
        });
    });
});