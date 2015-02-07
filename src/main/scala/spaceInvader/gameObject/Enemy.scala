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
          case SpaceTurtleID => new SpaceTurtle(spawnx, spawny, Down)
          case CosmicBeeID => new CosmicBee(spawnx, spawny, Down)
          case GalacticDragonID => new GalacticDragon(spawnx, spawny, Down)
        }
    }
}

import Enemy._
abstract class Enemy (base: EnemyType, xc: Int, yc: Int, dir: Char) extends VelObject(xc,yc,dir) with Health {
  x = clamp(x,width,Width)

  var hp = base.maxHp
  def shotType = base.shotType
  val shotInterval = base.shotInterval
  val id = base.id
  def difficulty = base.difficulty
  def numShot = base.numShot
  def shotDelay = base.shotDelay

  def clamp(v: Int, size: Int, upper: Int) =
    if (v - size < 0) size/2
    else if (v + size > upper) upper-size/2
    else v
}

trait EnemyType {
    def maxHp: Int
    def id: Int
    def difficulty: Int

    // only for shooters, provide default values in
    // case it isn't needed
    def shotType: Int = BulletID
    def shotInterval: Int = 60
    def numShot = 1
    def shotDelay = 0
}

object Drone extends EnemyType {
  val id = DroneID
  val maxHp = 1
  val difficulty = 1
  override val shotInterval = 60 * 3
  override val shotType = BulletID    
}

object Fighter extends EnemyType {
    val id = FighterID
    val maxHp = 3
    val difficulty = 2
    override val shotType = BulletID
    override val shotInterval = 60 * 2
}

object SpaceTurtle extends EnemyType {
    val id = SpaceTurtleID
    val maxHp = 5
    val difficulty = 3
}

object CosmicBee extends EnemyType {
    val id = CosmicBeeID
    val maxHp = 1
    val difficulty = 1
}

object GalacticDragon extends EnemyType {
    val id = GalacticDragonID
    val maxHp = 10
    val difficulty = 10
    override val shotType = BulletID
    override val shotInterval = 60 * 2 + 30
    override val numShot = 5
    override val shotDelay = 10
}

object CyberSalmon extends EnemyType {
  val id = CyberSalmonID
  val maxHp = 50
  val difficulty = 100
  override val shotType = BulletID
  override val shotInterval = 60 * 5
  override val numShot = 3
  override val shotDelay = 10
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

class SpaceTurtle(x: Int, y: Int, dir: Char) extends Enemy(SpaceTurtle, x, y, dir) {
    dx =
      if (rand(2) % 2 == 0) {1}
      else {-1}
    dy = 1

    override def width = 60
    override def height = 30
}

class CosmicBee(x: Int, y: Int, dir: Char) extends Enemy(CosmicBee, x, y, dir) {
    dx =
      if (rand(2) % 2 == 0) {4}
      else {-4}
    dy = 3

    override def width = 20
    override def height = 20
}

class GalacticDragon(x: Int, y: Int, dir: Char) extends Enemy(GalacticDragon, x, y, dir) with Shooter {
    dx = 0
    dy = rand(2)+1

    override def width = 60
    override def height = 40
}

class CyberSalmon() extends extends Enemy(CyberSalmon, SpaceInvader.Width/2, 85/2, Down) with Shooter {
  dx = 0
  dy = 0

  override def width = 200
  override def height = 85
}
