package spaceInvader.gameObject

object Enemy {
    def apply () {
        new Drone( 400, 400, Down, 4, 4)
    }
}

abstract class Enemy (x: Int, y: Int, dir: Char, dx: Int, dy: Int) extends VelObject(x,y,dir,dx,dy) with Health {}

object Drone {
    val shotType = BulletID
    val maxHp = 1
}

class Drone(x: Int, y: Int, dir: Char, dx: Int, dy: Int) extends Projectile(x, y, dmg, dir, dx, dy) {
    def id = DroneID
    hp = maxHp
}
