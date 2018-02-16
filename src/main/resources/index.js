$(document).ready(function() {
    $("#button.now").on("click", function(event) {
    $.ajax({
        url: "/now",
        cache: false
    }).done(function(now) {
        alert("current time: " + now);
        $("#text.now").text(now);
    }).fail(function(xhr, failure) {
        alert( "failure: " + failure);
    });
});