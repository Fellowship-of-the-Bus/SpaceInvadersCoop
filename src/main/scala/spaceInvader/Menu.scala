package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, Image}

object Menu extends Mode {
  def update(gc: GameContainer, delta: Int) = {
    import Mode._
    val input = gc.getInput

    if (input.isKeyDown(Input.KEY_P)) {
      GameID
    } else MenuID
  }

  def render(gc: GameContainer, g: Graphics) = {
    g.drawString("Space Invader", 0, 0)
    g.drawString("Press 'P' to play!", 0, 100)
  }
}
