package spaceInvader.gameObject
import GameObject._

trait Shooter {
    def x: Int
    def y: Int
    def size: Int
    def dir: Int
    def shotType: Int

    def shoot(dmg: Int, dx: Int, dy: Int) = {
        val px: Int = x
        val py: Int = y

        if (dir == Up) { y -= (size + Projectile.size) / 2 }
        if (dir == Down) { y += (size + Projectile.size) / 2 }
        if (dir == Left) { x -= (size + Projectile.size) / 2 }
        if (dir == Right) { x += (size + Projectile.size) / 2 }

        Projectile(shotType, x, y, dir, dx, dy);
    }
}

