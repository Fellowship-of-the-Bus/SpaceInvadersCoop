package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, Image}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}

object Menu extends BasicGameState {
  import KeyMap._

  val choices = List("Play Game", "Options", "Quit")
  var curChoice = 0

  var time = 0
  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    import Mode._
    val input = gc.getInput

    // if (input.isKeyDown(Input.KEY_P)) {
    //   SpaceInvader.game.reset()
    //   game.enterState(Mode.GameID)
    // }
    time += delta
    if (time > 250) {
      if (input.isKeyDown(keyMap(Confirm))) {
        choices(curChoice) match {
          case "Play Game" =>
            SpaceInvader.game.reset()
            game.enterState(Mode.GameID)
          case "Quit" => System.exit(0)
        }
      } else if (input.isKeyDown(keyMap(Up))) {
        curChoice = (curChoice-1+choices.length) % choices.length
        time = 0
      } else if (input.isKeyDown(keyMap(Down))) {
        curChoice = (curChoice+1) % choices.length
        time = 0
      }
    }
  }

  def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    import SpaceInvader.{Width, Height}
    //g.drawImage(background.....)
    //g.drawImage(logo......)
    var counter = 0
    for ( item <- choices ) {
      if (counter == curChoice) {
        g.setColor(new Color(255, 0, 0))
      }

      SpaceInvader.drawCentred(item, 200+counter*30,g)
      counter += 1

      g.setColor(Color.white)
    }
    g.drawString("Space Invader", 0, 0)
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    
  }

  def getID() = Mode.MenuID
}

//object Option extends BasicGameState {
  //
//}
