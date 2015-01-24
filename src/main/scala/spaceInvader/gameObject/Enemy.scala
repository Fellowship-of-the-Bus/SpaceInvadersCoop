package spaceInvader.gameObject
import GameObject._
import IDMap._
import scala.util.Random
import spaceInvader.SpaceInvader._

object Enemy {
    private val rand = new Random
    def apply () = {
        val spawnx = nextInt(Width)
        val spawny = -20-nextInt(60)
        new Drone( spawnx, spawny, Down)
    }
    def nextInt(i: Int) = rand.nextInt(i)
}

import Enemy._
abstract class Enemy (xc: Int, yc: Int, dir: Char) extends VelObject(xc,yc,dir) with Health {
  x = clamp(x,Width)

  def clamp(v: Int, upper: Int) =
    if (v - size < 0) size/2
    else if (v + size > upper) upper-size/2
    else v
}

object Drone {
    val shotType = BulletID
    val maxHp = 1
}

class Drone(x: Int, y: Int, dir: Char) extends Enemy(x, y, dir) with Shooter {
    def id = DroneID
    var hp = Drone.maxHp
    def shotType = Drone.shotType

    val shotInterval = 60 * 3

    dx =
      if (nextInt(2) % 2 == 0) {1}
      else {-1}
    dy = nextInt(2)+1
}
