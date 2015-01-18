package spaceInvader.gameObject
import GameObject._

trait Shooter {
    def x: Int
    def y: Int
    def size: Int
    def dir: Char
    def shotType: Int

    var shotTimer = 0

    def tick() = {
        if ( shotTimer > 0 ) {
            shotTimer -= 1
        }
    }

    def shoot = {
        var px: Int = x
        var py: Int = y

        if ( shotTimer == 0 ) {
            shotTimer = 5

            if (dir == Up) { 
                py -= (size + Projectile.size) / 2
            } else if (dir == Down) { 
                py += (size + Projectile.size) / 2 
            } else if (dir == Left) { 
                px -= (size + Projectile.size) / 2 
            } else if (dir == Right) { 
                px += (size + Projectile.size) / 2 
            }

           Some(Projectile(shotType, px, py, dir));
        } else {
            None
        }
    }
}

