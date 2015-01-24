package spaceInvader.gameObject
import GameObject._
import IDMap._
import spaceInvader.SpaceInvader._
import spaceInvader._

object Enemy {
    def apply (eid: Int) = {
        val spawnx = rand(Width)
        val spawny = -20-rand(60)
        eid match {
          case DroneID => new Drone(spawnx, spawny, Down)
          case FighterID => new Fighter(spawnx, spawny, Down)
        }
    }
}

import Enemy._
abstract class Enemy (base: EnemyType, xc: Int, yc: Int, dir: Char) extends VelObject(xc,yc,dir) with Health {
  x = clamp(x,Width)

  var hp = base.maxHp
  def shotType = base.shotType
  val shotInterval = base.shotInterval
  val id = base.id
  def difficulty = base.difficulty

  def clamp(v: Int, upper: Int) =
    if (v - size < 0) size/2
    else if (v + size > upper) upper-size/2
    else v
}

trait EnemyType {
    def shotType: Int
    def maxHp: Int
    def shotInterval: Int
    def id: Int
    def difficulty: Int
}

object Drone extends EnemyType {
    val shotType = BulletID
    val maxHp = 1
    val shotInterval = 60 * 3
    val id = DroneID
    val difficulty = 1
}

object Fighter extends EnemyType {
    val shotType = BulletID
    val maxHp = 3
    val shotInterval = 60 * 2
    val id = FighterID
    val difficulty = 2
}

class Drone(x: Int, y: Int, dir: Char) extends Enemy(Drone, x, y, dir) with Shooter {
    dx =
      if (rand(2) % 2 == 0) {1}
      else {-1}
    dy = rand(2)+1
}

class Fighter(x: Int, y: Int, dir: Char) extends Enemy(Fighter, x, y, dir) with Shooter {
    dx =
      if (rand(2) % 2 == 0) {1}
      else {-1}
    dy = rand(2)+1
}
