package com.github.fellowship_of_the_bus
package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color}
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.gui.AbstractComponent

import lib.game.GameConfig.{Height,Width}
import lib.slick2d.ui.{drawCentred,Pane,TextField,AnchoredImage,Label}
import lib.math.max
import KeyMap._
import gameObject._
import IDMap._

class GameState extends BasicGameState {
  implicit val bg = Color.transparent
  var gameState = new Game(() => gameover())
  var name: TextField = null
  var gameoverUI = new Pane(0, 0, Width, Height, Color.transparent)(new Color(255, 0, 0, (0.5 * 255).asInstanceOf[Int]))

  // reset all local state to a fresh state
  def reset(): Unit = {
    name.text = ""
    gameState = new Game(() => gameover())
    pauseTimer = 0
  }

  def gameover(): Unit = {
    name.setFocus(true)
    updateGameoverUI()
  }

  var pauseTimer = 0
  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    implicit val input = gc.getInput
    if (KeyMap.isKeyDown(Pause) && pauseTimer == 0) {
      pauseTimer = 30
      gc.setPaused(!gc.isPaused) // toggle paused
    }

    if (! gc.isPaused) {
      gameState.update(gc, game, delta)
    }

    pauseTimer = Math.max(0, pauseTimer-1)
  }

  def accuracyString: String = {
    val numHit = gameState.numHit
    val numShot = gameState.numShot
    val accuracy: java.lang.Double = 100.0*numHit/max(numShot,1)
    s"$numHit / $numShot ... ${String.format("%1$,.2f", accuracy)} "
  }

  def scoreString: String = s"Score: ${gameState.score}"

  def totalString = s"Total: ${gameState.finalScore.asInstanceOf[Int]}"

  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    import IDMap._

    val player = gameState.player
    val alliedProjectiles = gameState.alliedProjectiles
    val enemies = gameState.enemies
    val enemyProjectiles = gameState.enemyProjectiles
    val powerUps = gameState.powerUps

    g.drawImage(images(BackgroundID), 0, 0)
    g.drawImage(images(TopBorderID), 0, 0)
    if (player.active) {
      val (px, py) = player.topLeftCoord
      g.drawImage(images(player.id), px, py)
    }

    // draw everything else
    def drawAll(objs: List[GameObject]*): Unit =
      for {
        xs <- objs
        o <- xs
        if (o.active)
        // (x,y) =
      } {
        val (x, y) = o.topLeftCoord
        g.drawImage(images(o.id), x, y)
      }

    drawAll(alliedProjectiles, enemies, enemyProjectiles, powerUps)

    for (i <- 0 until player.getHp) {
      g.drawImage(images(HeartID), 20*i, Height-20)
    }

    if (gameState.isGameOver) {
      gameoverUI.render(gc, game, g)
      name.setFocus(true)
    } else {
      drawCentred(accuracyString, Height-40,g)
      drawCentred(scoreString, Height-20, g)
    }
  }

  var gameoverUI = new Pane(0, 0, Width, Height, Color.transparent)(new Color(255, 0, 0, (0.5 * 255).asInstanceOf[Int]))

  def init(gc: GameContainer, game: StateBasedGame) = {
    implicit val font = gc.getDefaultFont
    val nameWidth = font.getWidth("AAAAAAAAAAAAAAAAAAAA") // allow space for up to 20 characters
    val padding = 20 // start location of each item is padding*N

    // set up name entry field and label
    val text = "Enter your initials: "
    val textWidth = font.getWidth(text)
    val textHeight = font.getHeight(text)
    val label = new Label(text, 0, 0, textWidth, textHeight)
    name = new TextField(gc, textWidth, 0, nameWidth, textHeight, (source: AbstractComponent) => {
      println(name.text)
    })
    val nameEntry = new Pane(0, padding*3, textWidth+name.width, textHeight, Color.transparent)
    nameEntry.addChildren(label, name)

    // accuracy, score, total strings
    val accuracy = new Label(accuracyString, nameEntry.width, 0)
    val score = new Label(scoreString, nameEntry.width, padding)
    val total = new Label(totalString, nameEntry.width, padding*2)

    // set up center pane
    val areaWidth = nameEntry.width
    val areaHeight = padding*4
    val pane = new Pane(Width/2-areaWidth/2, Height/2-areaHeight/2, areaWidth, areaHeight, Color.transparent)
    pane.addChildren(accuracy, score, total, nameEntry)

    // setup GameOver image and center pane
    val image = new AnchoredImage(images(GameOverID), 0, 0, Width, Height)
    gameoverUI.addChildren(image, pane)
    gameoverUI.init(gc, game)
  }

  def getID() = Mode.GameID
}
