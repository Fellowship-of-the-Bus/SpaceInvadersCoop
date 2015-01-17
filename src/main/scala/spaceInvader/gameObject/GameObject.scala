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
    val size: Int = 40
    
    def move() = {}
}
    
