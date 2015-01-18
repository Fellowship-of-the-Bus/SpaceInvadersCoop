package spaceInvader.gameObject
import spaceInvader.SpaceInvader._

object IDMap{
    val playerID = 1
    
    val DroneID = 2

    val BulletID: Int  = 101
    val MissileID: Int = 102

    val Power1ID = 201

    val imageMap = Map(
        playerID -> "img/player.png",

        DroneID -> "img/Drone.png",

        BulletID -> "img/Bullet.png"//,
        // MissileID -> "img/Missile.png",

        // Power1ID -> "img/player.png"
    )

    lazy val images = imageMap.map { x =>
      val (id, loc) = x
      id -> makeImg(loc)
    }
}
