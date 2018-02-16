$(document).ready(function() {
    $("#button.now").on("click", function(event) {
        var request = $.ajax({
            url: "/now",
            cache: false
        });
        request.done(function(now) {
            alert("current time: " + now);
            $("#text.now").text(now);
        });
        request.fail(function(xhr, failure) {
            alert( "failure: " + failure);
        });
    });
})