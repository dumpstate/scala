package scala.tools.nsc.classpath

import scala.reflect.io.AbstractFile

case class AggregateFlatClasspath(aggregates: Seq[FlatClasspath]) extends FlatClasspath {
  def packages(inPackage: String): Seq[PackageEntry] = {
    val aggregatedPkgs = aggregates.map(_.packages(inPackage)).flatten.distinct
    aggregatedPkgs
  }
  def classes(inPackage: String): Seq[ClassfileEntry] = {
    val aggreagatedClasses = aggregates.map(_.classes(inPackage)).flatten
    aggreagatedClasses
  }
  def list(inPackage: String): (Seq[PackageEntry], Seq[ClassfileEntry]) = {
    val (packages, classes) = aggregates.map(_.list(inPackage)).unzip
    (packages.flatten.distinct, classes.flatten)
  }
  def findClassFile(className: String): Option[AbstractFile] = {
    val lastIndex = className.lastIndexOf('.')
    val (pkg, simpleClassName) = if (lastIndex == -1) (FlatClasspath.RootPackage, className) else {
      (className.substring(0, lastIndex), className.substring(lastIndex+1))
    }
    classes(pkg).find(_.name == simpleClassName).map(_.file)
  }
}
