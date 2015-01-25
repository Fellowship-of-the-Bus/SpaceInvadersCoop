package spaceInvader.gameObject
import spaceInvader.SpaceInvader._

object KeyMap{

  val Left = 0
  val Right = 1
  val Up = 2
  val Down = 3
  val SpaceBar = 4

  var keyMap = default
  def default = Map(
    Left -> Input.KEY_LEFT,
    Right -> Input.KEY_RIGHT,
    Up -> Input.KEY_UP,
    Down -> Input.KEY_DOWN,
    SpaceBar -> Input.KEY_SPACE
  )
  def reset = {
    keyMap = default
  }

}
