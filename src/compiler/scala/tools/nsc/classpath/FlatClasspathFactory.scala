package scala.tools.nsc.classpath

import scala.tools.nsc.util.ClassPath
import scala.reflect.io.AbstractFile

class FlatClasspathFactory extends ClasspathFactory[FlatClasspath] {
  def expandPath(path: String, expandStar: Boolean = true): List[String] = ClassPath.expandPath(path, expandStar)
  def expandDir(extdir: String): List[String] = ClassPath.expandDir(extdir)
  
  def newClassPath(file: AbstractFile): FlatClasspath = {
    if (file.hasExtension("jar") || file.hasExtension("zip")) {
//      new JarFlatClasspath(file.file)
      // if you want to disable ZipFileIndex caching, uncomment the line below
      // ZipFileIndex.clearCache()
      //new ZipFileIndexFlatClasspath(file.file)
      ZipArchiveFlatClasspath.create(file.file)
    } else if (file.isDirectory) {
      new DirectoryFlatClasspath(file.file)
    } else {
      sys.error(s"Unsupported classpath element: $file")
    }
  }
    
  
  def sourcesInPath(path: String): List[FlatClasspath] = {
    // TODO: implement properly
    Nil
  }
}
