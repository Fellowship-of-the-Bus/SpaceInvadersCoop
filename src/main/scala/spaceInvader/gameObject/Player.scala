package spaceInvader.gameObject
import IDMap._
import GameObject._

class Player (xc: Int, yc: Int) extends GameObject (xc, yc, Up) with Health with Shooter {
    var hp = 10
    var shotType: Int = PBulletID
    def id = playerID
    

    val shotInterval = 20
    val numShot = 1
    val shotDelay = 4
    
    def move(dx: Int, dy: Int) {
        x = x + dx
        y = y + dy
    }
}
