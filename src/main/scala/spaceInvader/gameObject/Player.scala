package spaceInvader.gameObject
import IDMap._

class Player (xc: Int, yc: Int) extends GameObject (xc, yc, Up) with Health {
    hp = 10
    var shotType: Int = BulletID
    
    
    def move(dx: Int, dy: Int) {
        x = x + dx
        y = y + dy
    }
}
