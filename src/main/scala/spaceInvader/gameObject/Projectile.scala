package spaceInvader.gameObject
import IDMap._

abstract class Projectile (x: Int, y: Int, val dmg: Int, dir: Char, dx: Int, dy: Int) extends VelObject(x,y,dir,dx,dy) {

}

object Bullet {
    val dmg: Int = 1
}

class Bullet(x: Int, y: Int, dir: Char, dx: Int, dy: Int) extends Projectile(x, y, dmg, dir, dx, dy) {
    def id = BulletID
}

object Missile {
    val dmg: Int = 2
}

class Missile(x: Int, y: Int, dir: Char, dx: Int, dy: Int) extends Projectile(x, y, dmg, dir, dx, dy) {
    def id = MissileID
}

