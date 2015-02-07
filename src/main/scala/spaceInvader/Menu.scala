package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, Image, KeyListener}
import org.newdawn.slick.state.{BasicGameState, StateBasedGame}
object MenuTimer {
	var time: Int = 0
}

object Menu extends BasicGameState {
  import KeyMap._

  val choices = List("Play Game", "Options", "Quit")
  var curChoice = 0

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    import Mode._
    implicit val input = gc.getInput

    MenuTimer.time += delta
    if (MenuTimer.time > 250) {
      if (KeyMap.isKeyDown(Confirm)) {
        choices(curChoice) match {
          case "Play Game" =>
            SpaceInvader.game.reset()
            game.enterState(Mode.GameID)
          case "Options" =>
            MenuTimer.time = 0
            game.enterState(Mode.OptionsID)
          case "Quit" => System.exit(0)
        }
      } else if (KeyMap.isKeyDown(Up)) {
        curChoice = (curChoice-1+choices.length) % choices.length
        MenuTimer.time = 0
      } else if (KeyMap.isKeyDown(Down)) {
        curChoice = (curChoice+1) % choices.length
        MenuTimer.time = 0
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
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    
  }

  def getID() = Mode.MenuID
}

object Options extends BasicGameState {
  import KeyMap._

  val choices = List("Key Binding", "Back")
  var curChoice = 0

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    import Mode._
    val input = gc.getInput

    MenuTimer.time += delta
    if (MenuTimer.time > 250) {
      if (input.isKeyDown(keyMap(Confirm))) {
        choices(curChoice) match {
          case "Key Binding" =>
            MenuTimer.time = 0
            game.enterState(Mode.KeyBindOptionID)
          case "Back" => 
            MenuTimer.time = 0
            game.enterState(Mode.MenuID)
        }
      } else if (input.isKeyDown(keyMap(Up))) {
        curChoice = (curChoice-1+choices.length) % choices.length
        MenuTimer.time = 0
      } else if (input.isKeyDown(keyMap(Down))) {
        curChoice = (curChoice+1) % choices.length
        MenuTimer.time = 0
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
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    
  }

  def getID() = Mode.OptionsID
}

object Listener extends KeyListener {
  var curKey = 0
  def keyPressed(k: Int, c: Char) = {
    curKey = k
  }
  def inputEnded(): Unit = {}
  def inputStarted(): Unit = {}
  def isAcceptingInput(): Boolean = true
  def setInput(input: org.newdawn.slick.Input): Unit = {}
  def keyReleased(k: Int,c: Char): Unit = {}
}

object KeyBindOption extends BasicGameState {
  import KeyMap._

  val choices = List("Left", "Right", "Up", "Down", "Shoot", "Confirm", "Back")
  var curChoice = 0

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    import Mode._
    implicit val input = gc.getInput

    MenuTimer.time += delta
    if (MenuTimer.time > 250) {
      if (KeyMap.isKeyDown(NextOption)) {
        curChoice = (curChoice + 1) % choices.length
        MenuTimer.time = 0
      } else if (KeyMap.isKeyDown(Confirm) && curChoice == choices.length-1) {
        game.enterState(Mode.OptionsID)
        MenuTimer.time = 0
        curChoice = 0
      } else if (curChoice == choices.length-1) {
            
      } else {
        if (input.isKeyDown(Listener.curKey)) {
          KeyMap.setKeyBind(curChoice, Listener.curKey)
          MenuTimer.time = 0
        }
      }
    }
  }

  def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    import SpaceInvader.{Width, Height}
    //g.drawImage(background.....)
    //g.drawImage(logo......)
    var counter = 0
    SpaceInvader.drawCentred("Press Tab to select next option", 200, g)
    for ( item <- choices ) {
      if (counter == curChoice) {
        g.setColor(new Color(255, 0, 0))
      }
      if (counter != choices.length-1) {
        SpaceInvader.drawCentred(s"$item: ${Input.getKeyName(keyMap(counter))}", 250+counter*30,g)
      } else {
        SpaceInvader.drawCentred(item, 250+counter*30,g)
      }
      counter += 1

      g.setColor(Color.white)
    }
  }

  def init(gc: GameContainer, game: StateBasedGame) = {
    implicit val input = gc.getInput
    input.addKeyListener(Listener)
  }

  def getID() = Mode.KeyBindOptionID
}
