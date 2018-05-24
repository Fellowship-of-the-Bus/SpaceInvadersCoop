package com.github.fellowship_of_the_bus
package spaceInvader
import java.util.logging.{Level, Logger}
import org.newdawn.slick.{AppGameContainer, GameContainer, SlickException, Image}
import org.newdawn.slick.state.{StateBasedGame}

import lib.game.GameConfig
import lib.util.Native

class SpaceInvader(gamename: String) extends StateBasedGame(gamename) {
  def initStatesList(gc: GameContainer) = {
    gc.setShowFPS(true)
    addState(Menu)
    addState(SpaceInvader.game)
    addState(Options)
    addState(KeyBindOption)
  }
}

object SpaceInvader extends App {
  def makeImg(loc: String) = new Image(loc)

  GameConfig.Width = 800
  GameConfig.Height = 600
  GameConfig.FrameRate = 60
  lazy val game = new GameState

  try {
    import lib.game.GameConfig._
    Native.loadLibraryFromJar()
    val appgc = new AppGameContainer(new SpaceInvader("Intergalactic Interlopers"))
    appgc.setDisplayMode(Width, Height, false)
    appgc.setTargetFrameRate(FrameRate)
    appgc.setVSync(true)
    appgc.start()
  } catch {
    case ex: SlickException => Logger.getLogger(SpaceInvader.getClass.getName()).log(Level.SEVERE, null, ex)
    case t: Throwable =>
      println("Library path is: " + System.getProperty("java.library.path"))
      t.printStackTrace
  }
}
