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

        if (dir == Up) { y += size / 2 }

        new Projectile(x,y,dmg,dir,dx,dy);
    }
}

