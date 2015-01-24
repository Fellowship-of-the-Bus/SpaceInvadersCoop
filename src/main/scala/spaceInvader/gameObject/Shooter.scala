package spaceInvader.gameObject
import GameObject._

trait Shooter {
    def x: Int
    def y: Int
    def width: Int
    def height: Int
    def dir: Char
    def shotType: Int

    def shotInterval: Int
    var shotTimer = shotInterval

    def tick() = {
        if ( shotTimer > 0 ) {
            shotTimer -= 1
        }
    }

    def shoot = {
        var px: Int = x
        var py: Int = y

        if ( shotTimer == 0 ) {
            shotTimer = shotInterval
            if (dir == Up) { 
                py -= (height + Projectile.height) / 2
            } else if (dir == Down) { 
                py += (height + Projectile.height) / 2 
            } else if (dir == Left) { 
                px -= (width + Projectile.width) / 2 
            } else if (dir == Right) { 
                px += (width + Projectile.width) / 2 
            }

           Some(Projectile(shotType, px, py, dir));
        } else {
            None
        }
    }
}

