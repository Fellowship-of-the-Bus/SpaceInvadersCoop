package spaceInvader
import org.newdawn.slick.{AppGameContainer, BasicGame, GameContainer, Graphics, SlickException,Color, Input, Image}
import SpaceInvader.{Height,Width}
import gameObject.IDMap._
import KeyMap._
import gameObject._

class GameState extends Mode {
  val player = new Player(100, 400)
  var enemies: List[Enemy] = List()
  var alliedProjectiles: List[Projectile] = List()
  var enemyProjectiles: List[Projectile] = List()
  var powerUps: List[PowerUp] = List()
  var score = 0

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
      if (input.isKeyDown( keyMap(Left))) -moveAmt
      else if (input.isKeyDown(keyMap(Right))) moveAmt
      else 0
    val newx = player.x + xamt

    val yamt =
      if (input.isKeyDown(keyMap(Up))) -moveAmt
      else if (input.isKeyDown(keyMap(Down))) moveAmt
      else 0
    val newy = player.y + yamt

    import SpaceInvader.{Width,Height}
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
          e.takeDmg(p.dmg)
          if (!e.active) {
            score += e.difficulty * 1000
          }
          p.inactivate
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
          p.inactivate
      }
    }

    for (p <- powerUps; if (p.active)) {
      val (px1, py1) = p.topLeftCoord
      val (px2, py2) = p.bottomRightCoord
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

  var counter = 0
  val spawnTimer = 120
  var enemyPower = 5
  def update(gc: GameContainer, delta: Int) = {
    implicit val input = gc.getInput

    move
    if (player.active) {
      if (input.isKeyDown(keyMap(SpaceBar))) {
        val shot = player.shoot
        shot match {
          case Some(s) => alliedProjectiles = s :: alliedProjectiles
          case _ => ()
        }
      }
      player.tick

      for (e <- enemies; if (e.active)) {
        if (player.collision(e)) {
            player.takeDmg((e.getHp-1)/2+1)
            e.takeDmg(e.getHp+1)
        }
      }
    }

    counter = counter + 1
    // periodically spawn move enemies
    if (counter == spawnTimer) {
      // periodically remove inactive objects
      cleanup
      var curEP = enemyPower
      var startMod: Int = math.min(
                        math.max(math.floor(math.log10(score)-4).asInstanceOf[Int], 0),
                        2)
      var eTypeFrame = EnemyEnd-EnemyStart
      while (curEP > 0) {
        val e = Enemy(math.min(rand(eTypeFrame)+ EnemyStart + startMod, EnemyEnd - 1))
        curEP -= e.difficulty
        enemies = e :: enemies
      }
      powerUps = PowerUp() :: powerUps

      enemyPower += 1
      counter = 0
    }
  }


  def render(gc: GameContainer, g: Graphics) = {
    import IDMap._
    
    // draw player
    if (player.active) {
      val (px, py) = player.topLeftCoord
      g.drawImage(images(player.id), px, py)
    }

    // draw everything else
    def drawAll(objs: List[GameObject]*): Unit =
      for {
        xs <- objs
        o <- xs
        if (o.active)
        (x,y) = o.topLeftCoord
      } g.drawImage(images(o.id), x, y)
    
    drawAll(alliedProjectiles, enemies, enemyProjectiles, powerUps)

    if (! player.active) {
      import SpaceInvader.{Width,Height}
      g.setColor(new Color(255, 0, 0, (0.5 * 255).asInstanceOf[Int]))
      g.fillRect(0, 0, Width, Height)
      g.drawImage(images(GameOverID), 0, 0)
    }

    val scoreString = s"Score: $score"
    g.drawString(scoreString, Width/2 - scoreString.length * 5, Height-20)
    for (i <- 0 until player.getHp) {
      g.drawImage(images(HeartID), 20*i, Height-20)
    }
  }
}
