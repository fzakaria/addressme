organization  := "com.github.fzakaria"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-caching" % sprayV,
    "io.spray"            %%  "spray-client"  % sprayV,
    "io.spray"            %%  "spray-json"    % "1.3.0",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "net.ceedubs" 		  %%  "ficus" 		  % "1.1.1",
    "com.roundeights"     %% "hasher"         % "1.0.0",
    "com.github.nscala-time" %% "nscala-time" % "1.4.0"
  )
}

resolvers ++= Seq(
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
  "RoundEights"            at "http://maven.spikemark.net/roundeights"
)

Revolver.settings

Revolver.enableDebugging(port = 5050, suspend = false)

scalariformSettings

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)