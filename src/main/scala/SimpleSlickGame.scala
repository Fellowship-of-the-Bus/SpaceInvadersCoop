package simpleslickgame
import java.util.logging.{Level, Logger}
import org.newdawn.slick.{AppGameContainer, BasicGame, GameContainer, Graphics, SlickException}

class SimpleSlickGame(gamename: String) extends BasicGame(gamename) {
  override def init(gc: GameContainer) = gc.setShowFPS(true) 
  override def update(gc: GameContainer, i: Int) = {}
  override def render(gc: GameContainer, g: Graphics) = g.drawString("Hi!", 10, 10)
}

object SimpleSlickGame extends App {
  try {
    println("Library path is: " + System.getProperty("java.library.path"))
    val appgc = new AppGameContainer(new SimpleSlickGame("Simple Slick Game"))
    appgc.setDisplayMode(640, 480, false)
    appgc.start()
  } catch {
    case ex: SlickException => Logger.getLogger(SimpleSlickGame.getClass.getName()).log(Level.SEVERE, null, ex)
    case _: Throwable => println("Library path is: " + System.getProperty("java.library.path"))
  }
}