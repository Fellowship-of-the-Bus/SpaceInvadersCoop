package com.github.fellowship_of_the_bus
package spaceInvader.gameObject

abstract class VelObject (xc: Int, yc: Int, dir: Char)  extends GameObject (xc, yc, dir) {
    var dx: Int = 0
    var dy: Int = 0

    override def move() {
        x += dx
        y += dy
    }
}
