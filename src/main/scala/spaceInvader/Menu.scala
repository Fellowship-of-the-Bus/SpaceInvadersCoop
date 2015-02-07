package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, Image}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

object Menu extends BasicGameState {
  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    import Mode._
    val input = gc.getInput

    if (input.isKeyDown(Input.KEY_P)) {
      SpaceInvader.game.reset()
      game.enterState(Mode.GameID)
    }
  }

  def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    g.drawString("Space Invader", 0, 0)
    g.drawString("Press 'P' to play!", 0, 100)
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    
  }

  def getID() = Mode.MenuID
}
