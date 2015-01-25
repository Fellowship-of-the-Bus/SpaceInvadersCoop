package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, Image}

object Mode {
  val Menu = 0
  val Game = 1
  val Pause = 2
  val Options = 3
  val GameOver = 4
}

trait Mode {
  def update(gc: GameContainer, delta: Int): Unit
  def render(gc: GameContainer, g: Graphics): Unit
}
