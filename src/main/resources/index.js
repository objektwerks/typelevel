$(document).ready(function() {
    $("#button-now").click(function() {
        var request = $.ajax({
            url: "/now",
            dataType: "json",
            cache: false
        });
        request.done(function(now, status, xhr) {
            $("#text-now").text(now.time);
        });
        request.fail(function(xhr, status, error) {
            console.log("xhr: " + xhr + " : status: " + status + " : error" + error);
        });
    });
})