package com.github.fellowship_of_the_bus
package spaceInvader
import org.newdawn.slick.{GameContainer, Input, Sound}
import org.newdawn.slick.state.StateBasedGame

import lib.game.GameConfig.{Height,Width}
import gameObject.IDMap._
import KeyMap._
import gameObject._

class Game {
  val shotSound = new Sound("sfx/Shoot.wav")
  val playerDeathSound = new Sound("sfx/PlayerDeath.wav")
  val playerHitSound = new Sound("sfx/PlayerHit.wav")
  val enemyDeathSound = new Sound("sfx/EnemyDeath.wav")
  val enemyHitSound = new Sound("sfx/EnemyHit.wav")
  val powerUpSound = new Sound("sfx/Powerup.wav")

  def playSound(s: Sound) = {
    s.stop
    s.play
  }


  val player = new Player(100, 400)
  var enemies: List[Enemy] = List()
  var alliedProjectiles: List[Projectile] = List()
  var enemyProjectiles: List[Projectile] = List()
  var powerUps: List[PowerUp] = List()
  var score = 0
  var numHit = 0
  var numShot = 0
  /** removes inactive game objects */
  def cleanup() = {
    alliedProjectiles = alliedProjectiles.filter(_.active)
    enemyProjectiles = enemyProjectiles.filter(_.active)
    enemies = enemies.filter(_.active)
    powerUps = powerUps.filter(_.active)
  }

  /** moves all objects in the scene */
  def move(implicit input: Input) = {
    val moveAmt = 5

    // move the player if the appropriate key is depressed
    val xamt =
      if (KeyMap.isKeyDown(Left)) -moveAmt
      else if (KeyMap.isKeyDown(Right)) moveAmt
      else 0
    val newx = player.x + xamt

    val yamt =
      if (KeyMap.isKeyDown(Up)) -moveAmt
      else if (KeyMap.isKeyDown(Down)) moveAmt
      else 0
    val newy = player.y + yamt

    if (newx + player.width/2 < Width && newx - player.width/2 > 0) {
      player.move(xamt, 0)
    }
    if (newy + player.height/2 < Height && newy - player.height/2 > Height/4) {
      player.move(0, yamt)
    }

    // move all active allied projectiles and
    // detect collisions with enemies
    for (p <- alliedProjectiles; if (p.active)) {
      val (px1, py1) = p.topLeftCoord
      val (px2, py2) = p.bottomRightCoord
      p.move()
      // detect leaving the game bounds
      if (px1 > Width || px2 < 0) {
        p.inactivate
      }
      if (py1 > Height || py2 < 0) {
        p.inactivate
      }
      // detect collision with enemies
      for (e <- enemies; if (e.active)) {
        if (p.collision(e)) {
          playSound(enemyHitSound)
          e.takeDmg(p.dmg)
          if (!e.active) {
            score += e.difficulty * 1000
            playSound(enemyDeathSound)
          }
          p.inactivate
          numHit += 1
        }
      }
    }

    for (p <- enemyProjectiles; if (p.active)) {
      val (px1, py1) = p.topLeftCoord
      val (px2, py2) = p.bottomRightCoord
      p.move()
      // detect leaving the game bounds
      if (px1 > Width || px2 < 0) {
        p.inactivate
      }
      if (py1 > Height || py2 < 0) {
        p.inactivate
      }
      // detect collision with enemies
      if (p.collision(player)) {
          player.takeDmg(p.dmg)
          playSound(playerHitSound)
          p.inactivate
      }
    }

    for (p <- powerUps; if (p.active)) {
      val (px1, _) = p.topLeftCoord
      val (px2, _) = p.bottomRightCoord
      p.move()
      // detect leaving the game bounds
      if (px1 > (Width + p.width) || (px2 < -p.width)) {
        p.inactivate
      }

      // detect collision with player
      if (p.collision(player)) {
          p.id match {
            case PowerHPID =>
              player.setHp(math.min(10, player.getHp + 3))
            case PowerShotsID =>
              player.numShot += 1
          }
          p.inactivate

          playSound(powerUpSound)
      }
    }


    // move all active enemies and deactivate if
    // they leave the bottom of the arena
    for (e <- enemies; if (e.active)) {
      e.move()
      e match {
        case sh: Shooter =>
          val shot = sh.shoot
          shot match {
            case Some(s) => enemyProjectiles = s :: enemyProjectiles
            case _ => ()
          }
          sh.tick()
        case _ => ()
      }
      val (ex1, ey1) = e.topLeftCoord
      val (ex2, _) = e.bottomRightCoord
      if (ex2 > Width || ex1 < 0) {
        e.dx = -e.dx
      }
      if (ey1 > Height) {
        e.inactivate
      }
    }
  }

  private var counter = 0
  private val spawnTimer = 120
  private var enemyPower = 5
  def update(gc: GameContainer, game: StateBasedGame, delta: Int) = {
    implicit val input = gc.getInput

    val isActive = player.active
    move
    if (player.active) {
      if (KeyMap.isKeyDown(SpaceBar)) {
        val shot = player.shoot
        shot match {
          case Some(s) =>
            alliedProjectiles = s :: alliedProjectiles
            numShot += 1
            playSound(shotSound)
          case _ => ()
        }
      }
      player.tick

      for (e <- enemies; if (e.active)) {
        if (player.collision(e)) {
          player.takeDmg((e.getHp-1)/2+1)
          e.takeDmg(e.getHp+1)
          playSound(playerHitSound)
        }
      }
    }

    counter = counter + 1
    // periodically spawn move enemies
    if (counter == spawnTimer) {
      // periodically remove inactive objects
      cleanup
      var curEP = enemyPower
      val startMod: Int = math.min(
                        math.max(math.floor(math.log10(score)-4).asInstanceOf[Int], 0),
                        2)
      val eTypeFrame = EnemyEnd-EnemyStart
      while (curEP > 0) {
        val e = Enemy(math.min(rand(eTypeFrame)+ EnemyStart + startMod, EnemyEnd - 1))
        curEP -= e.difficulty
        enemies = e :: enemies
      }
      powerUps = PowerUp() :: powerUps

      enemyPower += 1
      counter = 0
    }

    if (! player.active && isActive) {
      gameOver()
    } else if (isGameOver) {
      endTimer += delta
    }
  }

  var endTimer: Int = 0
  var finalScore = 0.0
  var isGameOver = false
  def gameOver() = {
    endTimer = 0
    finalScore = score * (1+1.0*numHit/numShot)
    isGameOver = true
    playSound(playerDeathSound)
  }
}
