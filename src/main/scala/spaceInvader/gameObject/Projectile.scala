package spaceInvader.gameObject
import IDMap._

object Projectile {
    val size = 10

    def apply(id: Int, x: Int, y: Int, dir: Char, dx: Int, dy: Int) {
        id match{
            case BulletID => new Bullet(x,y,dir,dx,dy)
            case MissileID => new Missile(x,y,dir,dx,dy)
        }
    }
}

abstract class Projectile (x: Int, y: Int, val dmg: Int, dir: Char, dx: Int, dy: Int) extends VelObject(x,y,dir,dx,dy) {
    val size = Projectile.size
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

