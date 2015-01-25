package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, Image}

object Mode {
  val MenuID = 0
  val GameID = 1
  val PauseID = 2
  val OptionsID = 3
  val GameOverID = 4
}

trait Mode {
  def update(gc: GameContainer, delta: Int): Int
  def render(gc: GameContainer, g: Graphics): Unit
}
