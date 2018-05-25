package com.github.fellowship_of_the_bus
package spaceInvader.gameObject
import IDMap._
import GameObject._

class Player (xc: Int, yc: Int) extends GameObject (xc, yc, Up) with Health with Shooter {
    val maxHp = 10
    var hp = maxHp
    var shotType: Int = PBulletID
    def id = playerID


    val shotInterval = 20
    var numShot = 1
    val shotDelay = 4

    def move(dx: Int, dy: Int) {
        x = x + dx
        y = y + dy
    }
}
