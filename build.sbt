name := "SChip8"
version := "0.0.1"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8", "-Xfatal-warnings", "-language:postfixOps")

mainClass := Some("SChip8.Emulator")

libraryDependencies ++= Seq(
  "org.scalaz"                  %% "scalaz-core"                    % "7.1.5",
  "org.scalatest"               %% "scalatest"                      % "3.0.0"  % "test",
  "org.scalacheck"              %% "scalacheck"                     % "1.12.1" % "test"
)