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

  /**REMOVING MONSTERS one at a time ;
   * coordinates are determined */
  def removeSingleMonster(x:Float, y:Float, diameter:Int)
  {
    for (monster <- dots) //each monster in monsters get coord.
    {
        if(  monster.x > x - diameter && monster.x < x + diameter && monster.y > y - diameter && monster.y < y + diameter && monster.vulnerability >= 1)
            dots -= monster; //remove a monster

    }
    notifyListener()
  }


  /**monsters change color based on vulnerability
    * toggles a given monster's color based on vuln. state
    * vulnerable = cyan ; not vulnerable = red*/
  def colorSwitch{
        for (monster <- dots)
        {
            if(monster.vulnerability > 0 &&  monster.color == Color.RED) {
              monster.color == Color.CYAN // not vulnerable turn cyan
            }

              else {

                  monster.color == Color.RED // vulnerable turn red

            }

        }

    notifyListener()
  }

  /**random display of monsters on the screen;
    * k monsters
    * appear at random coordinates of grid*/

  def randomMonsters (ranMonster: Float){

    for (monsters <- dots) {
      var display = Random.nextInt(4) + 1;
      if (Random.nextFloat() < ranMonster && monsters.vulnerability == 0 ){


            monsters.vulnerability = display; //get monsters state; vuln. or invuln.



      }


    }

    notifyListener()
  }

  /*increase vulnerability of monster as time progress to
  create difficulty*/
 def makeMonsterVulnerable {
      for (monster <- dots) //each monster in monsters determine state
        if (monster.vulnerability <=0 ) //increase  vulnerability of red monster
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








}
