$(document).ready(function() {
    $("#button.now").on("click", function(event) {
        var request = $.ajax({
            url: "/now",
            dataType: "json",
            cache: false
        });
        request.done(function(data, textStatus, jqXHR) {
            console.log("data: " + data + " : textStatus: " + textStatus + " : jqXHR: " + jqXHR);
            $("#text.now").text(jqXHR.responseJSON);
        });
        request.fail(function(jqXHR, textStatus, errorThrown) {
            console.log( "text status: " + textStatus + " : error thrown" + errorThrown);
        });
    });
})