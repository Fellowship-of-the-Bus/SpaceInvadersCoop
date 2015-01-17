package spaceInvader.gameObject

trait Health {
    protected var hp: Int
    
    def takeDmg(amt: Int) = {
        hp -= amt
    }
 
    def addHp(amt: Int) = {
        hp = Math.min( hp + amt, maxHp)
    }

    def getHp() = hp 
}
