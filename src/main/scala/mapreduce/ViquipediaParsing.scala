package mapreduce

import scala.util.matching.Regex
import scala.xml.XML

object ViquipediaParse {
  val exampleFilename="32509.xml"

  case class ResultViquipediaParsing(titol: String, contingut: String, refs: List[String])

  def testParse= this.parseViquipediaFile(exampleFilename)

  def parseViquipediaFile(filename: String=this.exampleFilename) = {
    val xmlleg = new java.io.InputStreamReader(new java.io.FileInputStream(filename), "UTF-8")
    val xmllegg = XML.load(xmlleg)
    // obtinc el titol
    val titol = (xmllegg \\ "title").text
    // obtinc el contingut de la pàgina
    val contingut = (xmllegg \\ "text").text

    // identifico referències
    val ref = new Regex("\\[\\[[^\\]]*\\]\\]")
    println("La pagina es: " + titol)
    println("i el contingut: ")
    println(contingut)
    val refs = (ref findAllIn contingut).toList

    // elimino les que tenen :
    val filteredRefs = refs.filterNot(_.contains(':'))

    // caldrà eliminar-ne més?

    for (r <- refs) println(r)
    println(refs.length)
    println(filteredRefs.length)
    ResultViquipediaParsing(titol, contingut, filteredRefs)
  }
}