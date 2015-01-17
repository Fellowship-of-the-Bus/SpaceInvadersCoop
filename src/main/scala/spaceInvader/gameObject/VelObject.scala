package spaceInvader.gameObject

class VelObject (xc: Int, yc: Int, dir: Char, deltax: Int, deltay: Int)  extends GameObject (xc, yc, dir) {
    var dx: Int = deltax
    var dy: Int = deltay

    override move() {
        x += dx
        y += dy
    }
}
