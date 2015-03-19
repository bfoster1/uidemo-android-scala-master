package edu.luc.etl.cs313.scala.uidemo.model



import android.graphics.Color

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * A dot: the coordinates, color and size.
 * @param x horizontal coordinate.
 * @param y vertical coordinate.
 * @param color the color.
 * @param diameter dot diameter.
 * @param vulnerability  //monster in vulnerable state
 */
case class Dot(x: Float, y: Float, color: Int, diameter: Int, var vulnerability: Int)

object Dots {
  trait DotsChangeListener {
    /** @param dots the dots that changed. */
    def onDotsChange(dots: Dots): Unit
  }
}

/** A list of dots. */
class Dots {


  private val dots = new ListBuffer[Dot]

  private var dotsChangeListener: Dots.DotsChangeListener = _

  /** @param l set the change listener. */
  def setDotsChangeListener(l: Dots.DotsChangeListener) = dotsChangeListener = l

  /** @return the most recently added dot. */
  def getLastDot(): Dot = if (dots.size <= 0) null else dots.last // TODO convert to option

  /** @return immutable list of dots. */
  def getDots(): List[Dot] = dots.toList

  /**
   * @param x dot horizontal coordinate.
   * @param y dot vertical coordinate.
   * @param color dot color.
   * @param diameter dot size.
   */
  def addDot(x: Float, y: Float, color: Int, diameter: Int, vulnerability:Int): Unit = {
    dots += Dot(x, y, color, diameter, vulnerability )
    notifyListener()
  }

  //REMOVING MONSTERS
  def removeSingleMonster(x:Float, y:Float, diameter:Int)
  {
    for (monster <- dots)
    {
        if(  monster.x > x - diameter && monster.x < x + diameter && monster.y > y - diameter && monster.y < y + diameter && monster.vulnerability >= 1)
            dots -= monster;

    }
    notifyListener()
  }


  //monsters change color based on vulnerability
  def colorSwitch{
        for (monster <- dots)
        {
            if(monster.vulnerability > 0 &&  monster.color == Color.RED) {
              monster.color == Color.CYAN
            }

              else {

                  monster.color == Color.RED

            }

        }

    notifyListener()
  }

  //random display of monsters

  def randomMonsters (ranMonster: Float){

    for (monsters <- dots) {
      var display = Random.nextInt(5) + 1;
      if (Random.nextFloat() < ranMonster && monsters.vulnerability == 0 ){


            monsters.vulnerability = display;



      }


    }

    notifyListener()
  }

  //increase vulnerability of monster
 def makeMonsterVulnerable {
      for (monster <- dots)
        if (monster.vulnerability <=0 )
              { monster.vulnerability += 1}

          notifyListener();

 }


  /** Remove all dots. */
  def clearDots(): Unit = {
    dots.clear()
    notifyListener()
  }

  private def notifyListener(): Unit =
    if (null != dotsChangeListener)
      dotsChangeListener.onDotsChange(this)




  def totalMonsters():Int ={

      dots.length

  }




}
