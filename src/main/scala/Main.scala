import scala.xml.XML
import scala.util.matching.Regex


object tractaxml extends App {

  val xmlleg=new java.io.InputStreamReader(new java.io.FileInputStream("32509.xml"), "UTF-8")
  val xmllegg = XML.load(xmlleg)
  // obtinc el titol
  val titol=(xmllegg \\ "title").text
  // obtinc el contingut de la pàgina
  val contingut = (xmllegg \\ "text").text

  // identifico referències
  val ref = new Regex("\\[\\[[^\\]]*\\]\\]")
  println("La pagina es: " + titol)
  println("i el contingut: ")
  println(contingut)
  val refs=(ref findAllIn contingut).toList

  // elimino les que tenen :
  val kk = refs.filterNot(x=> x.contains(':'))

  // caldrà eliminar-ne més?

  for (r<-refs) println(r)
  println(refs.length)
  println(kk.length)
}