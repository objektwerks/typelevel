$(document).ready(function() {
    $("#button.now").on("click", function(event) {
    $.ajax({
        url: "/now",
        cache: false
    }).done(function(text) {
        alert("current time: " + result);
        $("#text.now").text(result);
    }).fail(function(jqXHR, textStatus) {
        alert( "Request failed: " + textStatus);
    });
});