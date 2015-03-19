package edu.luc.etl.cs313.scala.uidemo
package controller

import android.graphics.Color
import android.view.{MotionEvent, View}

import scala.collection.mutable.ArrayBuffer

import model.Dots
import view.DotView

/** Listen for taps. */
class TrackingTouchListener(dots: Dots, m : DotView) extends View.OnTouchListener {

  val tracks = new ArrayBuffer[Int]
  def MONSTER_DISPLAY = m.MonsterDiameter;

  override def onTouch(v: View, evt: MotionEvent): Boolean = {
    val action = evt.getAction
    action & MotionEvent.ACTION_MASK match {
      case MotionEvent.ACTION_DOWN | MotionEvent.ACTION_POINTER_DOWN =>
        val idx = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
          MotionEvent.ACTION_POINTER_INDEX_SHIFT
        tracks += evt.getPointerId(idx)
      case MotionEvent.ACTION_POINTER_UP =>
        val idx = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
          MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        tracks -= evt.getPointerId(idx)
      case MotionEvent.ACTION_MOVE =>
        for (i <- tracks) {
          val idx = evt.findPointerIndex(i)
          for (j <- 0 until evt.getHistorySize) {
            killMonster(dots,
              evt.getX(idx),
              evt.getY(idx),MONSTER_DISPLAY

            )

          }
        }
      case _ => return false
    }

    for (i <- tracks) {
      val idx = evt.findPointerIndex(i)
      killMonster(dots,
        evt.getX(idx),
        evt.getY(idx),MONSTER_DISPLAY

      )

    }

    true
  }

  private def killMonster(dots: Dots, x: Float, y: Float, display: Int) =
    dots.removeSingleMonster(x, y, display)
}
