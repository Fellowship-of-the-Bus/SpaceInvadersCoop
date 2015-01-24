package spaceInvader.gameObject
import IDMap._
import GameObject._

object Projectile {
    val size = 10

    def apply(id: Int, x: Int, y: Int, dir: Char) = {
        id match{
            case BulletID => new Bullet(x,y,dir)
            case MissileID => new Missile(x,y,dir)
            case PBulletID => new PBullet(x,y,dir)
        }
    }
}

abstract class Projectile (x: Int, y: Int, val dmg: Int, dir: Char, vel: Int) extends VelObject(x,y,dir) {
    override val size = Projectile.size

    def setVelocity(amt: Int) = {
        if (dir == Up) {
            dx = 0
            dy = -amt
        } else if (dir == Down) {
            dx = 0
            dy = amt
        } else if (dir == Left) {
            dx = -amt
            dy = 0
        } else if (dir == Right) {
            dx = amt
            dy = 0
        }
    }
    setVelocity(vel)
}

object Bullet {
    val dmg: Int = 1
    val vel = 2
}

class Bullet(x: Int, y: Int, dir: Char) extends Projectile(x, y, Bullet.dmg, dir, Bullet.vel) {
    def id = BulletID
}

object Missile {
    val dmg: Int = 2
    val vel = 1
}

class Missile(x: Int, y: Int, dir: Char) extends Projectile(x, y, Missile.dmg, dir, Missile.vel) {
    def id = MissileID
}

object PBullet {
    val dmg: Int = 1
    val vel = 2
}

class PBullet(x: Int, y: Int, dir: Char) extends Projectile(x, y, PBullet.dmg, dir, PBullet.vel) {
    def id = PBulletID
}

