fork := true

val (os, separator) = System.getProperty("os.name").split(" ")(0).toLowerCase match {
  case "linux" => "linux" -> ":"
  case "mac" => "macosx" -> ":"
  case "windows" => "windows" -> ";"
  case "sunos" => "solaris" -> ":"
  case x => x -> ":"
}

javaOptions += "-Djava.library.path=" + System.getProperty("java.library.path") + 
  separator + "./lib/native/" + os
