Typelevel
---------
>Typelevel project feature tests:
1. Cats
2. Doobie
3. FS2
4. Http4s
5. Monocle
6. Refined
7. Shapeless

Test
----
1. sbt clean test

Run
---
1. sbt run
2. open browser to: http://localhost:7777
3. CTRL-C to shutdown Http4sApp ( Sbt throws an ignorable exception. )

Warning
-------
>As of 2023.6.6, **sbt clean test** works! See build.sbt for details.

>Typelevel library changes easily break code. :)