package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, Image}

object Menu extends Mode {
  def update(gc: GameContainer, delta: Int) = {

  }

  def render(gc: GameContainer, g: Graphics) = {
    g.drawString("Space Invader", 0, 0)
    g.drawString("Press ", 0, 0)
  }
}
