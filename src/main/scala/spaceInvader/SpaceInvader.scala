package spaceInvader
import java.util.logging.{Level, Logger}
import org.newdawn.slick.{AppGameContainer, BasicGame, GameContainer, Graphics, SlickException,Color, Input}

class SpaceInvader(gamename: String) extends BasicGame(gamename) {
  import gameObject._

  val player = new Player(100, 100)
  var enemies: List[Enemy] = List()
  var alliedProjectiles: List[Projectile] = List()
  var enemyProjectiles: List[Projectile] = List()
  // var powerUps: PowerUp = null

  override def init(gc: GameContainer) = {
    gc.setShowFPS(true)
  }
  override def update(gc: GameContainer, delta: Int) = {
    val input = gc.getInput
    val moveAmt = 5

    if (input.isKeyDown(Input.KEY_UP)) {
      player.move(0, -moveAmt)
    } else if (input.isKeyDown(Input.KEY_DOWN)) {
      player.move(0, moveAmt)
    }

    if (input.isKeyDown(Input.KEY_LEFT)) {
      player.move(-moveAmt, 0)
    } else if (input.isKeyDown(Input.KEY_RIGHT)) {
      player.move(moveAmt, 0)
    } 

    if (input.isKeyDown(Input.KEY_SPACE)) {
      alliedProjectiles = player.shoot :: alliedProjectiles
    }

    for (p <- alliedProjectiles) p.move()
  }
  override def render(gc: GameContainer, g: Graphics) = {
    g.setColor(Color.yellow)
    g.drawRect(player.x-player.size/2, player.y-player.size/2, player.size, player.size)

    for (p <- alliedProjectiles) g.drawOval(p.x-p.size/2, p.y-p.size/2, p.size, p.size)

    g.drawString("Hi!", 100, 100)
  }
}

object SpaceInvader extends App {
  try {
    println("Library path is: " + System.getProperty("java.library.path"))
    val appgc = new AppGameContainer(new SpaceInvader("Simple Slick Game"))
    appgc.setDisplayMode(640, 480, false)
    appgc.setTargetFrameRate(60)
    appgc.setVSync(true)
    appgc.start()
  } catch {
    case ex: SlickException => Logger.getLogger(SpaceInvader.getClass.getName()).log(Level.SEVERE, null, ex)
    case _: Throwable => println("Library path is: " + System.getProperty("java.library.path"))
  }
}