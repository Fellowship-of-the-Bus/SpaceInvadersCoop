package com.github.fellowship_of_the_bus
package spaceInvader.gameObject

object GameObject {
    val Up    = 'w'
    val Down  = 'a'
    val Left  = 's'
    val Right = 'd'
}

abstract class GameObject (xc : Int, yc : Int, val dir: Char) {
    var x: Int = xc
    var y: Int = yc
    def width = 40
    def height = 40

    def id: Int

    private var isActive = true
    def active = isActive
    def inactivate() = isActive = false
    def move() = {}

    def topLeftCoord() = (x-width/2, y-height/2)
    def bottomRightCoord() = (x+width/2, y+height/2)

    def collision(cand: GameObject) = if (active && cand.active) {
        val (x1, y1) = topLeftCoord
        val (x2, y2) = bottomRightCoord
        val (cx1, cy1) = cand.topLeftCoord
        val (cx2, cy2) = cand.bottomRightCoord

        def inRange(v: Int, min: Int, max: Int) = {
          (v >= min) && (v <= max)
        }

        val xOver = inRange(cx1,x1,x2) || inRange(x1, cx1, cx2)
        val yOver = inRange(cy1,y1,y2) || inRange(y1, cy1, cy2)

        xOver && yOver
    } else false
}
