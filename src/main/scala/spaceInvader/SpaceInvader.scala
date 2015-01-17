package spaceInvader
import java.util.logging.{Level, Logger}
import org.newdawn.slick.{AppGameContainer, BasicGame, GameContainer, Graphics, SlickException,Color, Input}

class SpaceInvader(gamename: String) extends BasicGame(gamename) {
  import gameObject._

  val player = new Player(100, 400)
  var enemies: List[Enemy] = List()
  var alliedProjectiles: List[Projectile] = List()
  var enemyProjectiles: List[Projectile] = List()
  // var powerUps: PowerUp = null

  override def init(gc: GameContainer) = {
    gc.setShowFPS(true)
  }

  def cleanup() = {
    alliedProjectiles = alliedProjectiles.filter(_.active)
    enemyProjectiles = enemyProjectiles.filter(_.active)
    enemies = enemies.filter(_.active)
  }

  def move(implicit input: Input) = {
    val moveAmt = 5

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

    import SpaceInvader._
    if (newx + player.size/2 < Width && newx - player.size/2 > 0) {
      player.move(xamt, 0)
    }
    if (newy + player.size/2 < Height && newy - player.size/2 > Height/4) {
      player.move(0, yamt)
    }

    for (p <- alliedProjectiles) {
      p.move()
      if (p.x - p.size/2 > Width || p.x + p.size/2 < 0) {
        p.inactivate
      }
      if (p.y - p.size/2 > Height || p.y + p.size/2 < 0) {
        p.inactivate
      }
      for (e <- enemies) {
        var ex1 = e.x - e.size /2
        var ex2 = e.x + e.size /2
        var ey1 = e.y - e.size /2
        var ey2 = e.y + e.size /2
        var px1 = p.x - p.size /2
        var px2 = p.x + p.size /2
        var py1 = p.y - p.size /2
        var py2 = p.y + p.size /2
        
        def valueInRange(v: Int, min: Int, max: Int) = {
          (v >= min) && (v <= max)
        }

        val xOver = valueInRange(ex1,px1,px2) || valueInRange(px1, ex1, ex2)
        val yOver = valueInRange(ey1,py1,py2) || valueInRange(py1, ey1, ey2)

        if (xOver && yOver) {
            e.takeDmg(p.dmg)
            p.inactivate
        }
      }
    }

    for (e <- enemies) {
      e.move()
      e.shoot()
      if (e.x - e.size/2 > Width || e.x + e.size/2 < 0) {
        e.dx = -e.dx
      }
      if (e.y - e.size/2 > Height || e.y + e.size/2 < 0) {
        e.inactivate
      }
    }
  }

  var counter = 0
  var threshhold = 600
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

    counter = counter + 1
    if (counter % 120 == 0) {
      cleanup
    }
    if (counter == threshhold) {
      enemies = Enemy :: enemies
      if (threshhold % 60 == 0) {
        enemies = Enemy :: enemies
      }
      if (threshhold > 125)  
        threshhold -= 5
      }
      counter = 0
    }
  }
  override def render(gc: GameContainer, g: Graphics) = {
    g.setColor(Color.yellow)
    g.drawRect(player.x-player.size/2, player.y-player.size/2, player.size, player.size)

    for (p <- alliedProjectiles) g.drawOval(p.x-p.size/2, p.y-p.size/2, p.size, p.size)

    g.drawString("Hi!", 100, 100)
  }
}

object SpaceInvader extends App {
  val Width = 640
  val Height = 480

  try {
    println("Library path is: " + System.getProperty("java.library.path"))
    val appgc = new AppGameContainer(new SpaceInvader("Simple Slick Game"))
    appgc.setDisplayMode(Width, Height, false)
    appgc.setTargetFrameRate(60)
    appgc.setVSync(true)
    appgc.start()
  } catch {
    case ex: SlickException => Logger.getLogger(SpaceInvader.getClass.getName()).log(Level.SEVERE, null, ex)
    case _: Throwable => println("Library path is: " + System.getProperty("java.library.path"))
  }
}
