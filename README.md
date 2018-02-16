Typelevel
---------
>The purpose of this project is to prototype and test features of Typelevel projects.

Warning
-------
>Atom/Ensime, Eclipse and Intellij are all confused by Typelevel libraries. Sbt is your only friend.

Test
----
1. sbt clean test

Run
---
>Select numbered app:

1. sbt clean compile run

 [1] objektwerks.app.NowHttp4sApp
 [2] objektwerks.app.OrdersFreeMonadApp

Enter number: 1
[info] Running objektwerks.app.NowHttp4sApp 
browser: http://localhost:7777/

Enter number: 2
[info] Running objektwerks.app.OrdersFreeMonadApp 
Buying 100.0 of APPL
Buying 10.0 of MSFT
Selling 110.0 of GOOG
