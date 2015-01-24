package spaceInvader.gameObject
import spaceInvader.SpaceInvader._

object IDMap{
    val playerID = 1

    val EnemyStart = 10
    val DroneID = 10
    val FighterID = 11
    val CosmicBeeID = 12
    val SpaceTurtleID = 13
    val EnemyEnd = 14

    val BulletID: Int  = 101
    val MissileID: Int = 102
    val PBulletID = 151

    val Power1ID = 201

    val GameOverID = 1001
    val HeartID = 1002

    val imageMap = Map(
        playerID -> "img/PlayerR.png",

        DroneID -> "img/Drone.png",
        FighterID -> "img/Fighter.png",
        CosmicBeeID -> "img/CosmicBee.png",
        SpaceTurtleID -> "img/SpaceTurtle.png",


        BulletID -> "img/Bullet.png",
        // MissileID -> "img/Missile.png",
        PBulletID -> "img/PBullet.png",

        // Power1ID -> "img/player.png"

        GameOverID -> "img/GameOver.png",
        HeartID -> "img/Heart.png"
    )

    lazy val images = imageMap.map { x =>
      val (id, loc) = x
      id -> makeImg(loc)
    }
}
