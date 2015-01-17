package spaceInvader.gameObject

class Player (xc: Int, yc: Int) extends GameObject (xc, yc, Up) with Health {
    hp = 10
    
    
    def move(dx: Int, dy: Int) {
        x = x + dx
        y = y + dy
    }
}
