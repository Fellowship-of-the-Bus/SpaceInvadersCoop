package spaceInvader.gameObject
import spaceInvader._
import spaceInvader.SpaceInvader._
import GameObject._
import IDMap._

object PowerUp {
  def apply () = {
    var spawnx: Int = 0 
    val width = 30
    var dir: Char = 0

    if (rand(2) % 2 == 0) {
        spawnx = Width / 2
        dir = Right
    } else {
        spawnx = Width / 2 + (width / 2)
        dir = Left
    } 
    val spawny = rand(Height/4 * 3) + (Height / 4)
    val id = rand(EndPowerUp - StartPowerUp) + StartPowerUp

    new PowerUp(id, spawnx, spawny, dir)
  }
}

class PowerUp (val id: Int, xc: Int, yc: Int, dir: Char) extends VelObject(xc, yc, dir) {
    override def width = 30
    override def height = 30
    if (xc < 0) {
        dx = 2
    } else {
        dx = -2
    }
}


