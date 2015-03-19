package edu.luc.etl.cs313.scala.uidemo
package controller

import android.app.Activity
import android.graphics.Color
import android.view.View.OnKeyListener
import android.view.{MenuItem, Menu, KeyEvent, View}

import view.DotView
import model.Dots

import scala.util.Random

/** Controller mixin (stackable trait) for Android UI demo program */
trait Controller extends Activity with TypedActivityHolder {

  val dotModel: Dots

  private var dotView: DotView = _

  // TODO consider using State pattern


  var isDotsView = true

  def toggleView(): Unit = {
    if (isDotsView) {
      isDotsView = false
      setContentView(R.layout.list)
      connectListView()
    } else {
      isDotsView = true
      setContentView(R.layout.main)
      connectDotsView()
    }
  }

  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater.inflate(R.menu.simple_menu, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem) = item.getItemId match {
    case R.id.menu_clear => dotModel.clearDots() ; true
    case R.id.menu_toggleView => toggleView() ; true
    case _ => super.onOptionsItemSelected(item)
  }

  def connectDotsView(): Unit = {
    dotView = findView(TR.dots)

    dotView.setDots(dotModel)

    dotView.setOnTouchListener(new TrackingTouchListener(dotModel,dotView ))
    dotView.setOnKeyListener(new OnKeyListener {
      override def onKey(v: View, keyCode: Int, event: KeyEvent): Boolean =
      if (KeyEvent.ACTION_DOWN == event.getAction)
        keyCode match {
          case KeyEvent.KEYCODE_SPACE => makeDot(dotModel, Color.RED, 0); true
          case KeyEvent.KEYCODE_ENTER => makeDot(dotModel, Color.CYAN, 1); true
          case _ => false
        }
      else
        false
    })

    findView(TR.button1).setOnClickListener(new View.OnClickListener {
      override def onClick(v: View) = makeDot(dotModel, Color.RED, 0)
    })
    findView(TR.button2).setOnClickListener(new View.OnClickListener {
      override def onClick(v: View) = makeDot(dotModel, Color.CYAN, 1)
    })

    // This listener provides a tiny bit of mediation from model to view.
    // Conceptually, it represents the dashed arrow (events) from model to view.
    dotModel.setDotsChangeListener(new Dots.DotsChangeListener {
      def onDotsChange(dots: Dots) = {
        val d = dots.getLastDot
        findView(TR.text1).setText(if (null == d) "" else d.x.toString)
        findView(TR.text2).setText(if (null == d) "" else d.y.toString)
        dotView.invalidate()
      }
    })
  }

  /**
   * @param dots the dots we're drawing
   * @param color the color of the dot
   * @param vulnerability // determines monsters vulnerability
   */
  def makeDot(dots: Dots, color: Int, vulnerability : Int): Unit = {
    val pad = (DOT_DIAMETER + 2) * 2
    dots.addDot(
      DOT_DIAMETER + (Random.nextFloat() * (dotView.getWidth - pad)),
      DOT_DIAMETER + (Random.nextFloat() * (dotView.getHeight - pad)),
      color,vulnerability,
      DOT_DIAMETER)
  }

  def sumOfMonsters = (dotView.getWidth /( (DOT_DIAMETER * 2)+ 2) ) + (dotView.getHeight/( (DOT_DIAMETER * 2)+ 2) )
  def getSumOfMonster = sumOfMonsters
  def connectListView(): Unit = {
    val scroll = findView(TR.scroll)
    scroll.post(new Runnable() { override def run() = scroll.fullScroll(View.FOCUS_DOWN) })
    val listView = findView(TR.list)
    for (d <- dotModel.getDots)
      listView.append(d.toString + String.format("%n"))
    // This listener provides a tiny bit of mediation from model to view.
    // Conceptually, it represents the dashed arrow (events) from model to view.
    dotModel.setDotsChangeListener(new Dots.DotsChangeListener {
      override def onDotsChange(dots: Dots) = {
        val d = dots.getLastDot
        if (d == null)
          listView.setText("")
        else
          listView.append(d.toString + String.format("%n"))
      }
    })
  }
}