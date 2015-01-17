package spaceInvader.gameObject
import GameObject._
import IDMap._
import scala.util.Random
import SpaceInvader._

object Enemy {
    def apply () {
        val spawnx = nextInt(Width)
        val spawny = nextInt(Height / 4)
        val dx =
            if (nextInt(1) % 2 == 0) {1}
            else {-1}
        val dy = nextInt(2)
        new Drone( spawnx, spawny, Down, dx, dy)
    }
}

abstract class Enemy (x: Int, y: Int, dir: Char, dx: Int, dy: Int) extends VelObject(x,y,dir,dx,dy) with Health {}

object Drone {
    val shotType = BulletID
    val maxHp = 1
}

class Drone(x: Int, y: Int, dir: Char, dx: Int, dy: Int) extends Enemy(x, y, dir, dx, dy) with Shooter {
    def id = DroneID
    var hp = Drone.maxHp
}
