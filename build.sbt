name := "scala-quartz"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies += "org.quartz-scheduler" % "quartz" % "2.3.2"
libraryDependencies += "org.quartz-scheduler" % "quartz-jobs" % "2.3.2"
libraryDependencies += "org.jboss.spec.javax.transaction" % "jboss-transaction-api_1.1_spec" % "1.0.1.Final"

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.2.4"
libraryDependencies += "com.typesafe" % "config" % "1.4.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed"         % "2.6.12"
libraryDependencies += "com.typesafe.akka" %% "akka-stream"              % "2.6.12"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json"     % "10.2.4"
