$(document).ready(function() {
    console.log("document is ready!")
    $("#button-now").click(function() {
        console.log("button-now clicked!")
        var request = $.ajax({
            url: "/now",
            dataType: "json",
            cache: false
        });
        request.done(function(data, textStatus, jqXHR) {
            console.log("data: " + data + " : textStatus: " + textStatus + " : jqXHR: " + jqXHR);
            $("#text-now").text(jqXHR.responseJSON);
        });
        request.fail(function(jqXHR, textStatus, errorThrown) {
            console.log("jqXHR: " + jqXHR + " : textStatus: " + textStatus + " : errorThrown" + errorThrown);
        });
    });
})