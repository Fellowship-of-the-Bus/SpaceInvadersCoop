package com.github.fellowship_of_the_bus
package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color, Input, KeyListener, Sound}
import org.newdawn.slick.state.{BasicGameState => SlickBasicGameState, StateBasedGame}

import lib.slick2d.ui.drawCentred
import gameObject.IDMap._

object MenuUtil {
	var time = 0
  val done = 250

  val menuSound = new Sound("sfx/Blip.wav")

  def menuAction(): Unit = {
    menuSound.stop()
    menuSound.play()
    time = 0
  }
}

trait BasicGameState extends SlickBasicGameState {
  def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    import lib.game.GameConfig.{Width, Height}
    val fotb = images(LogoID)
    val logo = images(GameLogoID)
    g.drawImage(images(BackgroundID), 0, 0)
    g.drawImage(images(TopBorderID), 0, 0)
    g.drawImage(fotb, Width/2-fotb.getWidth/2, 3*Height/4)
    g.drawImage(logo, Width/2-logo.getWidth/2, Height/4)
  }
}

object Menu extends BasicGameState {
  import KeyMap._

  val choices = List("Play Game", "Options", "Quit")
  var curChoice = 0

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    implicit val input = gc.getInput

    MenuUtil.time += delta
    if (MenuUtil.time > MenuUtil.done) {
      if (KeyMap.isKeyDown(Confirm)) {
        MenuUtil.menuAction()
        choices(curChoice) match {
          case "Play Game" =>
            SpaceInvader.game.reset()
            game.enterState(Mode.GameID)
          case "Options" =>
            game.enterState(Mode.OptionsID)
          case "Quit" => System.exit(0)
        }

      } else if (KeyMap.isKeyDown(Up)) {
        MenuUtil.menuAction()
        curChoice = (curChoice-1+choices.length) % choices.length
      } else if (KeyMap.isKeyDown(Down)) {
        MenuUtil.menuAction()
        curChoice = (curChoice+1) % choices.length
      }
    }
  }

  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    super.render(gc, game, g)

    var counter = 0
    for ( item <- choices ) {
      if (counter == curChoice) {
        g.setColor(new Color(255, 0, 0))
      }

      drawCentred(item, 200+counter*30,g)
      counter += 1

      g.setColor(Color.white)
    }
  }

  def init(gc: GameContainer, game: StateBasedGame) = {}

  def getID() = Mode.MenuID
}

object Options extends BasicGameState {
  import KeyMap._

  val choices = List("Key Binding", "Clear High Scores", "Show FPS Counter", "Back")
  var curChoice = 0

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    val input = gc.getInput

    MenuUtil.time += delta
    if (MenuUtil.time > MenuUtil.done) {
      if (input.isKeyDown(keyMap(Confirm))) {
        MenuUtil.menuAction()
        choices(curChoice) match {
          case "Key Binding" =>
            game.enterState(Mode.KeyBindOptionID)
          case "Clear High Scores" =>
            game.enterState(Mode.ClearHighScoresID)
          case "Show FPS Counter" =>
            gc.setShowFPS(! gc.isShowingFPS())
          case "Back" =>
            game.enterState(Mode.MenuID)
        }
      } else if (input.isKeyDown(keyMap(Up))) {
        MenuUtil.menuAction()
        curChoice = (curChoice-1+choices.length) % choices.length
      } else if (input.isKeyDown(keyMap(Down))) {
        MenuUtil.menuAction()
        curChoice = (curChoice+1) % choices.length
      }
    }
  }

  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    super.render(gc, game, g)
    var counter = 0
    for ( item <- choices ) {
      if (counter == curChoice) {
        g.setColor(new Color(255, 0, 0))
      }

      drawCentred(item, 200+counter*30,g)
      counter += 1

      g.setColor(Color.white)
    }
  }

  def init(gc: GameContainer, game: StateBasedGame) = {}

  def getID() = Mode.OptionsID
}

object ClearHighScores extends BasicGameState {
  import KeyMap._

  val choices = List("Yes", "No")
  var curChoice = 0

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    val input = gc.getInput

    MenuUtil.time += delta
    if (MenuUtil.time > MenuUtil.done) {
      if (input.isKeyDown(keyMap(Confirm))) {
        MenuUtil.menuAction()
        choices(curChoice) match {
          case "Yes" =>
            ScoreUtil.getScoreboardFile().delete(swallowIOExceptions = true)
          case "No" => () // fallthrough and exit to options state
        }
        game.enterState(Mode.OptionsID)
      } else if (input.isKeyDown(keyMap(Left))) {
        MenuUtil.menuAction()
        curChoice = 0
      } else if (input.isKeyDown(keyMap(Right))) {
        MenuUtil.menuAction()
        curChoice = 1
      }
    }
  }

  val title = "Are you sure you want to erase all high scores?"
  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    import lib.game.GameConfig.Width
    super.render(gc, game, g)
    val font = g.getFont
    var counter = 0
    val totalLength = font.getWidth(choices.mkString(" "))
    val len1 = font.getWidth(choices(0)+"A") // getWidth trims strings, so need to append a non-whitespace character

    g.drawString(title, Width/2-font.getWidth(title)/2, 200)
    for ( item <- choices ) {
      if (counter == curChoice) {
        g.setColor(new Color(255, 0, 0))
      }

      if (counter == 0) {
        g.drawString(item, Width/2-totalLength/2, 240)
      } else {
        g.drawString(item, Width/2-totalLength/2+len1, 240)
      }
      counter += 1

      g.setColor(Color.white)
    }
  }

  def init(gc: GameContainer, game: StateBasedGame) = {}

  def getID() = Mode.ClearHighScoresID
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

  val choices = List("Left", "Right", "Up", "Down", "Shoot", "Confirm", "Pause", "Back")
  var curChoice = 0

  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    implicit val input = gc.getInput

    MenuUtil.time += delta
    if (MenuUtil.time > MenuUtil.done) {
      if (KeyMap.isKeyDown(NextOption)) {
        MenuUtil.menuAction()
        curChoice = (curChoice + 1) % choices.length
      } else if (KeyMap.isKeyDown(Confirm) && curChoice == choices.length-1) {
        MenuUtil.menuAction()
        game.enterState(Mode.OptionsID)
        curChoice = 0
      } else if (curChoice == choices.length-1) {

      } else {
        if (input.isKeyDown(Listener.curKey)) {
          KeyMap.setKeyBind(curChoice, Listener.curKey)
          MenuUtil.menuAction()
        }
      }
    }
  }

  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    super.render(gc, game, g)
    var counter = 0
    drawCentred("Press Tab to select next option", 200, g)
    for ( item <- choices ) {
      if (counter == curChoice) {
        g.setColor(new Color(255, 0, 0))
      }
      val text =
        if (counter != choices.length-1) s"$item: ${Input.getKeyName(keyMap(counter))}"
        else item
      drawCentred(text, 250+counter*20, g)
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
