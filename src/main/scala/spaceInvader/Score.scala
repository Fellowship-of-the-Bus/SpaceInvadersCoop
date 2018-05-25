package com.github.fellowship_of_the_bus
package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Font}
import org.newdawn.slick.state.StateBasedGame

import lib.slick2d.ui.{Pane,Label,Button,UIElement}
import lib.math.max

case class Score(name: String, score: Int, numHit: Int, numShot: Int, power: Int) {
  val accuracy: java.lang.Double = 100.0*numHit/max(numShot,1)

  def accuracyFormat: String = s"${String.format("%1$,.2f", accuracy)}"
  def accuracyString: String = s"$numHit / $numShot ... ${accuracyFormat}"
  def scoreString: String = s"Score: ${score}"

  def totalString = s"Total: ${score.asInstanceOf[Int]}"
}

object ScoreUtil {
  object Header {
    val name = "Name"
    val score = "Score"
    val hit = "Hit"
    val fired = "Fired"
    val power = "Power"
    val accuracy = "Accuracy"
  }
  import Header._
  import scala.math.max

  val numScores       = 10
  val minDist         = 3
  val longestName     = "A"*(max(20, name.length)+minDist)
  val longestScore    = "9"*(max(9, score.length)+minDist)
  val longestHit      = "9"*(max(9, hit.length)+minDist)
  val longestShot     = "9"*(max(9, fired.length)+minDist)
  val longestPower    = "9"*(max(4, power.length)+minDist)
  val longestAccuracy = "9"*(max(6, accuracy.length)+minDist)

  val headerString =
    name     + " "*(longestName.length-name.length) +
    score    + " "*(longestScore.length-score.length) +
    hit      + " "*(longestHit.length-hit.length) +
    fired    + " "*(longestShot.length-fired.length) +
    power    + " "*(longestPower.length-power.length) +
    accuracy + " "*(longestAccuracy.length-accuracy.length)
}
import ScoreUtil._

// a single row in the score table
class ScoreUI(private var s: Score, val x: Float, val y: Float, val width: Float, val height: Float)(implicit font: Font) extends UIElement {
  val textHeight   = font.getHeight(longestName)
  val nameText     = new Label(longestName,     x,                          y, font.getWidth(longestName      ), textHeight)
  val scoreText    = new Label(longestScore,    nameText.bottomRight._1,    y, font.getWidth(longestScore     ), textHeight)
  val numHitText   = new Label(longestHit,      scoreText.bottomRight._1,   y, font.getWidth(longestHit       ), textHeight)
  val numShotText  = new Label(longestShot,     numHitText.bottomRight._1,  y, font.getWidth(longestShot      ), textHeight)
  val powerText    = new Label(longestPower,    numShotText.bottomRight._1, y, font.getWidth(longestPower     ), textHeight)
  val accuracyText = new Label(longestAccuracy, powerText.bottomRight._1,   y, font.getWidth(longestAccuracy  ), textHeight)

  val children = List(nameText, scoreText, numHitText, numShotText, powerText, accuracyText)

  def score: Score = s
  def score_=(newScore: Score): Unit = {
    s = newScore
    nameText.text = newScore.name
    scoreText.text = newScore.score.toString
    numHitText.text = newScore.numHit.toString
    numShotText.text = newScore.numShot.toString
    powerText.text = newScore.power.toString
    accuracyText.text = newScore.accuracyFormat
  }

  def draw(gc: GameContainer, sbg: StateBasedGame, g: Graphics): Unit = {
    for (child <- children) {
      child.render(gc, sbg, g)
    }
  }

  def update(gc: GameContainer, sbg: StateBasedGame, delta: Int) = ()
}

// a display for the score table
class ScoreboardUI(x: Float, y: Float, width: Float, height: Float, gotoMainMenu: () => Unit, isSelectable: () => Boolean)(implicit font: Font) extends Pane(x, y, width, height)(Color.darkGray) {
  val padding = 5
  val scoreHeight = font.getHeight(headerString)

  val header = new Label(headerString, padding, 0, width, scoreHeight)

  val scores = for (i <- 0 until numScores)
    yield new ScoreUI(Score("", 0, 0, 1, 1), padding, (i+1)*scoreHeight, width, scoreHeight)

  val buttonWidth = 120
  val mainMenu = new Button("Main Menu", width/2-buttonWidth/2, height-scoreHeight, buttonWidth, scoreHeight, () => gotoMainMenu()).setSelectable(isSelectable)

  addChildren(header::(scores.toList:+mainMenu))
  def updateScore(newScores: List[Score]): Unit = {
    for ((scoreUI, newScore) <- scores zip newScores) {
      scoreUI.score = newScore
    }
  }
}
