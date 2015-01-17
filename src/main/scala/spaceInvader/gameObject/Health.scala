package spaceInvader.gameObject

trait Health {
    protected var hp: Int
    
    def takeDmg(amt: Int) = {
        hp -= amt
        if (hp <= 0) {
            inactivate
        }
    }

    def getHp() = hp 
}
