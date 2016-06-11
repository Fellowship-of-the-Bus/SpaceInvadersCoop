package com.github.fellowship_of_the_bus
package spaceInvader.gameObject

trait Health {
    protected var hp: Int
    def inactivate(): Unit

    def takeDmg(amt: Int) = {
        hp -= amt
        if (hp <= 0) {
            inactivate
        }
    }

    def getHp() = hp
    def setHp(amt: Int) { hp = amt }
}
