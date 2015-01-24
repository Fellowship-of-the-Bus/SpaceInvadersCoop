package spaceInvader
import java.util.logging.{Level, Logger}
import org.newdawn.slick.{AppGameContainer, BasicGame, GameContainer, Graphics, SlickException,Color, Input, Image}

class SpaceInvader(gamename: String) extends BasicGame(gamename) {
  import gameObject._

  val player = new Player(100, 400)
  var enemies: List[Enemy] = List()
  var alliedProjectiles: List[Projectile] = List()
  var enemyProjectiles: List[Projectile] = List()
  // var powerUps: PowerUp = null

  override def init(gc: GameContainer) = {
    gc.setShowFPS(true)
    // images(player.id)
  }

  /** removes inactive game objects */
  def cleanup() = {
    alliedProjectiles = alliedProjectiles.filter(_.active)
    enemyProjectiles = enemyProjectiles.filter(_.active)
    enemies = enemies.filter(_.active)
  }

  /** moves all objects in the scene */
  def move(implicit input: Input) = {
    val moveAmt = 5

    // move the player if the appropriate key is depressed
    val xamt =
      if (input.isKeyDown(Input.KEY_LEFT)) -moveAmt
      else if (input.isKeyDown(Input.KEY_RIGHT)) moveAmt
      else 0
    val newx = player.x + xamt

    val yamt =
      if (input.isKeyDown(Input.KEY_UP)) -moveAmt
      else if (input.isKeyDown(Input.KEY_DOWN)) moveAmt
      else 0
    val newy = player.y + yamt

    import SpaceInvader.{Width,Height}
    if (newx + player.size/2 < Width && newx - player.size/2 > 0) {
      player.move(xamt, 0)
    }
    if (newy + player.size/2 < Height && newy - player.size/2 > Height/4) {
      player.move(0, yamt)
    }

    // move all active allied projectiles and 
    // detect collisions with enemies
    for (p <- alliedProjectiles; if (p.active)) {
      val (px1, py1) = p.topLeftCoord
      val (px2, py2) = p.bottomRightCoord
      p.move()
      // detect leaving the game bounds
      if (px1 > Width || px2 < 0) {
        p.inactivate
      }
      if (py1 > Height || py2 < 0) {
        p.inactivate
      }
      // detect collision with enemies
      for (e <- enemies; if (e.active)) {
        if (p.collision(e)) {
            e.takeDmg(p.dmg)
            p.inactivate
        }
      }
    }

    // move all active enemies and deactivate if 
    // they leave the bottom of the arena
    for (e <- enemies; if (e.active)) {
      e.move()
      // e.shoot()
      if (e.x + e.size/2 > Width || e.x - e.size/2 < 0) {
        e.dx = -e.dx
      }
      if (e.y - e.size/2 > Height) {
        e.inactivate
      }
    }
  }

  var counter = 0
  var threshhold = 240
  override def update(gc: GameContainer, delta: Int) = {
    implicit val input = gc.getInput
    
    move

    if (input.isKeyDown(Input.KEY_SPACE)) {
      val shot = player.shoot
      shot match {
        case Some(s) => alliedProjectiles = s :: alliedProjectiles
        case _ => ()
      }
    }
    player.tick

    for (e <- enemies; if (e.active)) {
      if (player.collision(e)) {
          e.takeDmg(100)
          player.takeDmg(e.getHp)
          if (player.getHp <= 0) {
            player.inactivate
          }
        }
    }

    counter = counter + 1
    // periodically remove inactive objects
    if (counter % 120 == 0) {
      cleanup
    }
    // periodically spawn move enemies
    if (counter == threshhold) {
      def spawnEnemy() = for {
        i <- 0 until 10
      } enemies = Enemy() :: enemies
      spawnEnemy()
      if (threshhold % 60 == 0) {
        spawnEnemy()
      }
      if (threshhold > 125) {
        threshhold -= 5
      }
      counter = 0
    }
  }
  override def render(gc: GameContainer, g: Graphics) = {
    import IDMap._
    
    
    // draw player
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
        (x,y) = o.topLeftCoord
      } g.drawImage(images(o.id), x, y)
    
    drawAll(alliedProjectiles, enemies, enemyProjectiles)

    // g.drawString("Hi!", 100, 100)
  }
}

object SpaceInvader extends App {
  def makeImg(loc: String) = new Image(loc)

  val Width = 640
  val Height = 480
  val FrameRate = 60

  try {
    println("Library path is: " + System.getProperty("java.library.path"))
    val appgc = new AppGameContainer(new SpaceInvader("Simple Slick Game"))
    appgc.setDisplayMode(Width, Height, false)
    appgc.setTargetFrameRate(FrameRate)
    appgc.setVSync(true)
    appgc.start()
  } catch {
    case ex: SlickException => Logger.getLogger(SpaceInvader.getClass.getName()).log(Level.SEVERE, null, ex)
    case _: Throwable => println("Library path is: " + System.getProperty("java.library.path"))
  }
}
