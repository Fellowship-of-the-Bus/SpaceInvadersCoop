package spaceInvader.gameObject
import spaceInvader.SpaceInvader._

object IDMap{
    val playerID = 1
    
    val DroneID = 2

    val BulletID: Int  = 101
    val MissileID: Int = 102
    val PBulletID = 151

    val Power1ID = 201

    val GameOverID = 1001

    val imageMap = Map(
        playerID -> "img/PlayerR.png",

        DroneID -> "img/Drone.png",


        BulletID -> "img/Bullet.png",
        // MissileID -> "img/Missile.png",
        PBulletID -> "img/PBullet.png",

        // Power1ID -> "img/player.png"

        GameOverID -> "img/GameOver.png"
    )

    lazy val images = imageMap.map { x =>
      val (id, loc) = x
      id -> makeImg(loc)
    }
}
