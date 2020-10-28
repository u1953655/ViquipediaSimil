package main

import java.io.File

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import mapreduce._

object tractaxml extends App {

  val parseResult= ViquipediaParse.parseViquipediaFile()
  println(parseResult)
  parseResult
}

object exampleMapreduce extends App {

  val nmappers = 4
  val nreducers = 2
  val f1 = new java.io.File("f1")
  val f2 = new java.io.File("f2")
  val f3 = new java.io.File("f3")
  val f4 = new java.io.File("f4")
  val f5 = new java.io.File("f5")
  val f6 = new java.io.File("f6")
  val f7 = new java.io.File("f7")
  val f8 = new java.io.File("f8")

  val fitxers: List[(File, List[String])] = List(
    (f1, List("hola", "adeu", "per", "palotes", "hola","hola", "adeu", "pericos", "pal", "pal", "pal")),
    (f2, List("hola", "adeu", "pericos", "pal", "pal", "pal")),
    (f3, List("que", "tal", "anem", "be")),
    (f4, List("be", "tal", "pericos", "pal")),
    (f5, List("doncs", "si", "doncs", "quin", "pal", "doncs")),
    (f6, List("quin", "hola", "vols", "dir")),
    (f7, List("hola", "no", "pas", "adeu")),
    (f8, List("ahh", "molt", "be", "adeu")))


  val compres: List[(String,List[(String,Double, String)])] = List(
    ("bonpeu",List(("pep", 10.5, "1/09/20"), ("pep", 13.5, "2/09/20"), ("joan", 30.3, "2/09/20"), ("marti", 1.5, "2/09/20"), ("pep", 10.5, "3/09/20"))),
    ("sordi", List(("pep", 13.5, "4/09/20"), ("joan", 30.3, "3/09/20"), ("marti", 1.5, "1/09/20"), ("pep", 7.1, "5/09/20"), ("pep", 11.9, "6/09/20"))),
    ("canbravo", List(("joan", 40.4, "5/09/20"), ("marti", 100.5, "5/09/20"), ("pep", 10.5, "7/09/20"), ("pep", 13.5, "8/09/20"), ("joan", 30.3, "7/09/20"), ("marti", 1.5, "6/09/20"))),
    ("maldi", List(("pepa", 10.5, "3/09/20"), ("pepa", 13.5, "4/09/20"), ("joan", 30.3, "8/09/20"), ("marti", 0.5, "8/09/20"), ("pep", 72.1, "9/09/20"), ("mateu", 9.9, "4/09/20"), ("mateu", 40.4, "5/09/20"), ("mateu", 100.5, "6/09/20")))
  )




  def mappingInvInd(tupla:(File, List[String])) :List[(String, File)] =
    tupla match {
      case (file, words) =>
        for (word <- words) yield (word, file)
    }

  def reducingInvInd(tupla:(String,List[File])):(String,Set[File]) =
    tupla match {
      case (word, files) => (word, files.toSet)
    }

  val systema = ActorSystem("sistema")
  println("Llencem l'index invertit")
  // Al crear l'actor MapReduce no cal passar els tipus com a paràmetres ja que amb els propis paràmetres dels constructor SCALA ja pot inferir els tipus.
  //  val indexinvertit = systema.actorOf(Props(new MapReduce[File,String,String,File,Set[File]](fitxers,mappingInvInd,reducingInvInd)), name = "masterinv")
  val indexinvertit = systema.actorOf(Props(new MapReduce(fitxers,mappingInvInd,reducingInvInd)), name = "masterinv")




  def mappingWC(tupla:(File, List[String])) :List[(String, Int)] =
    tupla match {
      case (_, words) =>
        for (word <- words) yield (word, 1) // Canvi file per 1
    }

  def reducingWC(tupla:(String,List[Int])):(String,Int) =
    tupla match {
      case (word, nums) => (word, nums.sum)
    }


  println("Llencem el wordCount")
  //val wordcount = systema.actorOf(Props(new MapReduce[File,String,String,Int,Int](fitxers,mappingWC,reducingWC )), name = "mastercount")
  val wordcount = systema.actorOf(Props(new MapReduce(fitxers,mappingWC,reducingWC )), name = "mastercount")

  // afegir el missatge de compute com a start
  // https://alvinalexander.com/scala/akka-actor-how-to-send-message-wait-for-reply-ask/
 //   var resutltwordcount = wordcount ? compute
  // com tancar el sistema d'actors.
  /*

  EXERCICIS:

  Useu el MapReduce per saber quant ha gastat cada persona.

  Useu el MapReduce per saber qui ha fet la compra més cara a cada supermercat

  Useu el MapReduce per saber quant s'ha gastat cada dia a cada supermercat.
   */


  println("tot enviat, esperant... a veure si triga en PACO")
}



