$(document).ready(function() {
    $("#button.now").on("click", function(event) {
        var request = $.ajax({
            url: "/now",
            method: "GET",
            dataType: "text",
            cache: false
        });
        request.done(function(now) {
            console.log("current time: " + now);
            $("#text.now").text(now);
        });
        request.fail(function(xhr, failure) {
            console.log( "failure: " + failure);
        });
    });
})