name := "devblog"

version := "1.0"

scalaVersion := "2.11.7"

lazy val `devblog` = (project in file(".")).enablePlugins(
  PlayJava,
  net.litola.SassPlugin
)

libraryDependencies ++= Seq(javaWs,
  "org.mariadb.jdbc"          % "mariadb-java-client"           % "latest.integration",
  "org.springframework"       % "spring-context"                % "latest.integration",
  "javax.inject"              % "javax.inject"                  % "latest.integration",
  "org.springframework.boot"  % "spring-boot-starter-data-jpa"  % "latest.integration",
  "org.springframework"       % "spring-expression"             % "latest.integration",
  "org.hibernate"             % "hibernate-entitymanager"       % "latest.integration",
  "org.projectlombok"         % "lombok"                        % "latest.integration",
  "javax.persistence"         % "persistence-api"               % "latest.integration",
  "org.springframework"       % "spring-aspects"                % "latest.integration"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

CoffeeScriptKeys.bare := true