package com.github.fellowship_of_the_bus
package spaceInvader
import spaceInvader.SpaceInvader._
import org.newdawn.slick.{Input}

object KeyMap{

  val Left = 0
  val Right = 1
  val Up = 2
  val Down = 3
  val SpaceBar = 4
  val Confirm = 5
  val NextOption = 6
  val Pause = 7


  var keyMap = default
  def default = Map(
    Left -> Input.KEY_LEFT,
    Right -> Input.KEY_RIGHT,
    Up -> Input.KEY_UP,
    Down -> Input.KEY_DOWN,
    SpaceBar -> Input.KEY_SPACE,
    Confirm -> Input.KEY_RETURN,
    Pause -> Input.KEY_P,
    NextOption -> Input.KEY_TAB
  )
  def reset() = {
    keyMap = default
  }
  def isKeyDown(k: Int) (implicit input: Input) = {
    input.isKeyDown(keyMap(k))
  }
  def setKeyBind(key:Int,newKey: Int) (implicit input:Input) = {
    keyMap = keyMap + (key -> newKey)
  }

}
