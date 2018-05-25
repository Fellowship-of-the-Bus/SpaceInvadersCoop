package com.github.fellowship_of_the_bus
package spaceInvader
import org.newdawn.slick.{GameContainer, Graphics, Color}
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.gui.AbstractComponent

import lib.game.GameConfig.{Height,Width}
import lib.slick2d.ui.{drawCentred,Pane,TextField,AnchoredImage,Label,Button}
import KeyMap._
import gameObject._
import IDMap._
import ScoreUtil._

class GameState extends BasicGameState {
  implicit val bg = Color.transparent
  var gameState = new Game(() => gameover())
  var name: TextField = null
  val gameoverUI = new Pane(0, 0, Width, Height, Color.transparent)(new Color(255, 0, 0, (0.5 * 255).asInstanceOf[Int]))
  var scoreboardUI: ScoreboardUI = null
  var scoreSubmitted = false
  var scoreTimer = 0
  val scoreTimerMax = 1000

  // reset all local state to a fresh state
  def reset(): Unit = {
    name.text = ""
    gameState = new Game(() => gameover())
    pauseTimer = 0
    scoreSubmitted = false
    scoreTimer = 0
  }

  def gotoMainMenu(game: StateBasedGame): Unit = {
    MenuUtil.time = 0
    scoreTimer = 0
    game.enterState(Mode.MenuID)
  }

  def gameover(): Unit = {
    name.setFocus(true)
    updateGameoverUI()
  }

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

    pauseTimer = Math.max(0, pauseTimer-1)

    if (scoreSubmitted) {
      // enable exiting to main menu with escape key or enter key after submitting a score
      if (scoreTimer >= scoreTimerMax && (KeyMap.isKeyDown(Confirm) || KeyMap.isKeyDown(Exit))) {
        gotoMainMenu(game)
      }
      // tick the scoreboard timer
      if (scoreTimer <= scoreTimerMax) {
        scoreTimer += delta
      }
    } else {
      // enable exiting to main menu with the escape key after clearing the game, but before submitting a score
      if (gameState.endTimer >= gameState.endTimerMax && KeyMap.isKeyDown(Exit)) {
        gotoMainMenu(game)
      }
    }
  }

  // generate some default scores
  val defaultNames = List("AAA", "LAW", "GOD", "ACE", "PWN", "RAM", "TRB", "KKW", "ARD", "RJS")
  val defaultScoreBoard = for (i <- 0 until numScores)
    yield Score(
      defaultNames(i),
      scala.math.pow(2, i).asInstanceOf[Int]*500,
      i,
      i*(i+1),
      (i+1)/2
    )

  def getScore = Score(name.text, gameState.finalScore.asInstanceOf[Int], gameState.numHit, gameState.numShot, gameState.player.numShot)

  override def render(gc: GameContainer, game: StateBasedGame, g: Graphics) = {
    import IDMap._

    val player = gameState.player
    val alliedProjectiles = gameState.alliedProjectiles
    val enemies = gameState.enemies
    val enemyProjectiles = gameState.enemyProjectiles
    val powerUps = gameState.powerUps

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

    for (i <- 0 until player.getHp) {
      g.drawImage(images(HeartID), 20*i, Height-20)
    }

    if (gameState.isGameOver) {
      if (scoreSubmitted) {
        scoreboardUI.render(gc, game, g)
      } else {
        gameoverUI.render(gc, game, g)
      }
    } else {
      val score = getScore
      g.setColor(Color.white)
      g.drawString(s"Power Level: ${score.power}", 0, Height-40)
      drawCentred(score.accuracyString, Height-40,g)
      drawCentred(score.scoreString, Height-20, g)
    }
  }

  def submitScore(score: Score): Unit = {
    if (scoreSubmitted || name.text == "") return

    // read highscore table
    val file = getScoreboardFile().createIfNotExists()

    var oldScores: List[Score] = Nil
    try {
      oldScores = file.readDeserialized[List[Score]]
    } catch {
      case _: Exception => // file is corrupted or empty, ignore its contents
    }
    var scoreboard = score::oldScores++defaultScoreBoard

    // sort by score, descending
    scoreboard = scoreboard.sortBy(-_.score).distinct

    // use only the top 10 scores
    scoreboard = scoreboard.take(numScores)

    // write highscore table
    file.writeSerialized(scoreboard)

    // display table
    scoreboardUI.updateScore(scoreboard)
    scoreSubmitted = true
  }

  var updateGameoverUI = () => ()
  def init(gc: GameContainer, game: StateBasedGame) = {
    implicit val font = gc.getDefaultFont
    val numChildren = 5 // number of children of main (not root) pane
    val padding = 20 // start location of each item is padding*numChildren
    val nameWidth = font.getWidth(longestName) // allow space for up to 20 characters

    // set up name entry field and label
    val text = "Enter your initials: "
    val textWidth = font.getWidth(text)
    val textHeight = font.getHeight(text)
    val label = new Label(text, 0, 0, textWidth, textHeight)
    name = new TextField(gc, textWidth, 0, nameWidth, textHeight, (source: AbstractComponent) => {
      submitScore(getScore)
    })
    name.maxLength(longestName.length)
    val nameEntry = new Pane(0, padding*3, textWidth+name.width, textHeight, Color.transparent)
    nameEntry.addChildren(label, name)

    // dimensions of main gameover pane
    val areaWidth = nameEntry.width
    val areaHeight = padding*numChildren

    // accuracy, score, total strings
    val accuracy = new Label("", areaWidth, 0)
    val scoreText = new Label("", areaWidth, padding)
    val total = new Label("", areaWidth, padding*2)

    // callback to update the text elements when they are known
    updateGameoverUI = () => {
      val score = getScore
      accuracy.text = score.accuracyString
      scoreText.text = score.scoreString
      total.text = score.totalString
      accuracy.center()
      scoreText.center()
      total.center()
    }

    // setup return button, submit button
    // if more buttons area added, would need to increase areaWidth
    val numButtons = 2
    val buttonWidth = 120
    val buttonHeight = textHeight
    val buttonPadding = 50
    val buttonAreaWidth = areaWidth-buttonPadding*numButtons
    val startPad = (areaWidth-buttonAreaWidth)/2
    def buttonX(n: Int) = {
      val width = buttonAreaWidth/numButtons
      val center = width*n+buttonAreaWidth/numButtons/2
      startPad+center-buttonWidth/2
    }
    val mainMenu = new Button("Main Menu", buttonX(0), 0, buttonWidth, buttonHeight, () => gotoMainMenu(game))
      .setSelectable(() => gameState.endTimer >= gameState.endTimerMax)
    val submit = new Button("Submit Score", buttonX(1), 0, buttonWidth, buttonHeight, () => {
      submitScore(getScore)
    }).setSelectable(() => name.text != "")
    val buttonPane = new Pane(0, padding*4, areaWidth, textHeight)
    buttonPane.addChildren(mainMenu, submit)

    // set up center pane
    val pane = new Pane(Width/2-areaWidth/2, Height/2-areaHeight/2, areaWidth, areaHeight, Color.transparent)
    pane.addChildren(accuracy, scoreText, total, nameEntry, buttonPane)

    // setup GameOver image and center pane
    val image = new AnchoredImage(images(GameOverID), 0, 0, Width, Height)
    gameoverUI.addChildren(image, pane)
    gameoverUI.setState(getID())
    gameoverUI.init(gc, game)

    // setup scoreboard UI
    val scoreboardWidth  = font.getWidth(headerString)
    val scoreboardHeight = (numScores+2)*font.getHeight(headerString) // +2 for header and buttons
    scoreboardUI = new ScoreboardUI(Width/2-scoreboardWidth/2, Height/2-scoreboardHeight/2, scoreboardWidth, scoreboardHeight, () => gotoMainMenu(game), () => scoreTimer >= scoreTimerMax)
    scoreboardUI.setState(getID())
    scoreboardUI.init(gc, game)
  }

  def getID() = Mode.GameID
}
