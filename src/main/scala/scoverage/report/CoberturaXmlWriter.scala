package scoverage.report

import scoverage.{MeasuredMethod, MeasuredClass, MeasuredPackage, Coverage}
import scala.xml.Node
import java.io.File
import org.apache.commons.io.FileUtils

/** @author Stephen Samuel */
class CoberturaXmlWriter(baseDir: File, outputDir: File) {

  def write(coverage: Coverage): Unit = {
    FileUtils.write(new File(outputDir.getAbsolutePath + "/cobertura.xml"), xml(coverage).toString())
  }

  def method(method: MeasuredMethod): Node = {
    <method name={method.name}
            signature="()V"
            line-rate={method.statementCoverageFormatted}
            branch-rate={method.branchCoverageFormatted}>
      <lines>
        {method.statements.map(stmt =>
          <line
          number={stmt.line.toString}
          hits={stmt.count.toString}
          branch="false"/>
      )}
      </lines>
    </method>
  }

  def klass(klass: MeasuredClass): Node = {
    <class name={klass.name}
           filename={klass.source.replace(baseDir.getAbsolutePath, "")}
           line-rate={klass.statementCoverageFormatted}
           branch-rate={klass.branchCoverageFormatted}
           complexity="0">
      <methods>
        {klass.methods.map(method)}
      </methods>
    </class>
  }

  def pack(pack: MeasuredPackage): Node = {
    <package name={pack.name}
             line-rate={pack.statementCoverageFormatted}
             branch-rate={pack.branchCoverageFormatted}
             complexity="0">
      <classes>
        {pack.classes.map(klass)}
      </classes>
    </package>
  }

  def xml(coverage: Coverage): Node = {
    <coverage line-rate={coverage.statementCoverageFormatted}
              branch-rate={coverage.branchCoverageFormatted}
              version="1.0"
              timestamp={System.currentTimeMillis.toString}>
      <sources>
        <source>/src/main/scala</source>
      </sources>
      <packages>
        {coverage.packages.map(pack)}
      </packages>
    </coverage>
  }
}