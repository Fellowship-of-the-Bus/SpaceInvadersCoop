package com.github.fellowship_of_the_bus
package spaceInvader
import org.newdawn.slick.{AppGameContainer, GameContainer, Graphics, SlickException,Color, Input, Image}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

import lib.game.GameConfig.{Height,Width}
import lib.slick2d.ui.drawCentred
import lib.math.max
import gameObject.IDMap._
import KeyMap._
import gameObject._

class GameState extends BasicGameState {
  var gameState = new Game

  def reset() = gameState = new Game

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

    if (gameState.endTimer > 2500 && KeyMap.isKeyDown(Confirm)) {
        MenuTimer.time = 0
        game.enterState(Mode.MenuID)
    }
    pauseTimer = Math.max(0, pauseTimer-1)
  }

  def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    import IDMap._

    val player = gameState.player
    val alliedProjectiles = gameState.alliedProjectiles
    val enemies = gameState.enemies
    val enemyProjectiles = gameState.enemyProjectiles
    val powerUps = gameState.powerUps
    val score = gameState.score
    val numHit = gameState.numHit
    val numShot = gameState.numShot

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

    val myDouble: java.lang.Double = 100.0*numHit/max(numShot,1)
    val acc = String.format("%1$,.2f", myDouble)
    val accString = s"$numHit / $numShot ... $acc "
    val scoreString = s"Score: $score"
    val total = gameState.finalScore.asInstanceOf[Int]
    val totalString = s"Total: ${total}"
    for (i <- 0 until player.getHp) {
      g.drawImage(images(HeartID), 20*i, Height-20)
    }

    if (gameState.isGameOver) {
      g.setColor(new Color(255, 0, 0, (0.5 * 255).asInstanceOf[Int]))
      g.fillRect(0, 0, Width, Height)
      g.drawImage(images(GameOverID), 0, 0)

      g.setColor(Color.white)
      drawCentred(accString, Height/2,g)
      drawCentred(scoreString, Height/2+20, g)
      drawCentred(totalString, Height/2+40, g)

      drawCentred("Enter your initials", Height/2+60, g)
    } else {
      drawCentred(accString, Height-40,g)
      drawCentred(scoreString, Height-20, g)
    }

  }

  def init(gc: GameContainer, game: StateBasedGame) = {

  }

  def getID() = Mode.GameID
}
