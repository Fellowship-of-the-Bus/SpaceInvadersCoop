package com.github.fellowship_of_the_bus
package spaceInvader.gameObject
import spaceInvader.SpaceInvader._

object IDMap{
  val playerID = 1

  val EnemyStart = 11
  val DroneID = 11
  val FighterID = 12
  val CosmicBeeID = 13
  val SpaceTurtleID = 14
  val GalacticDragonID = 15
  val EnemyEnd = 16

  val CyberSalmonID = 51

  val BulletID: Int  = 101
  val MissileID: Int = 102
  val PBulletID = 151

  val StartPowerUp = 201
  val PowerHPID = 201
  val PowerShotsID = 202
  val EndPowerUp = 203

  val GameOverID = 1001
  val HeartID = 1002
  val TopBorderID = 1003

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
    PowerShotsID -> "img/PowerShots.png",

    GameOverID -> "img/GameOver.png",
    HeartID -> "img/Heart.png",
    TopBorderID -> "img/TopBorder.png"


  )

  lazy val images = imageMap.map { x =>
    val (id, loc) = x
    id -> makeImg(loc)
  }
}
