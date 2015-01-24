package spaceInvader.gameObject
import spaceInvader.SpaceInvader._

object IDMap{
  val playerID = 1

  val EnemyStart = 10
  val DroneID = 10
  val FighterID = 11
  val CosmicBeeID = 12
  val SpaceTurtleID = 13
  val GalacticDragonID = 14
  val EnemyEnd = 15

  val BulletID: Int  = 101
  val MissileID: Int = 102
  val PBulletID = 151

  val PowerHPID = 201
  val PowerShotsID = 202

  val GameOverID = 1001
  val HeartID = 1002

  val imageMap = Map(
    playerID -> "img/PlayerR.png",

    DroneID -> "img/Drone.png",
    FighterID -> "img/Fighter.png",
    CosmicBeeID -> "img/CosmicBee.png",
    SpaceTurtleID -> "img/SpaceTurtle.png",
    GalacticDragonID -> "img/GalacticDragon.png",

    BulletID -> "img/Bullet.png",
    MissileID -> "img/Missile.png",
    PBulletID -> "img/PBullet.png",

    PowerHPID -> "img/PowerHP.png",
    PowerShotsID -> "img/PowerShots",

    GameOverID -> "img/GameOver.png",
    HeartID -> "img/Heart.png"
  )

  lazy val images = imageMap.map { x =>
    val (id, loc) = x
    id -> makeImg(loc)
  }
}
