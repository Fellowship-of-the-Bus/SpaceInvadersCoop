package com.github.fellowship_of_the_bus
package spaceInvader
package test

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import org.junit.runner.RunWith
import lib.game.GameConfig.{Width, Height}

import gameObject._
import gameObject.GameObject._
import IDMap._

/**
* The goal of this class is to test the collision function of GameObject, which does collision detection
* between itself and another (given) GameObject, returning a boolean value. There is no special handling
* in this function for different types (e.g. enemy collides with player vs. enemy collides with power-up);
* thus, not every possible permutation will be tested here, just a reasonable sampling. There is, however,
* special handling for the case of active objects vs. inactive ones.
*/
@RunWith(classOf[JUnitRunner])
class CollisionTest extends FunSuite {
  /**
  * Scenarios to cover:
  *
  * Different GameObject types (Player, Enemy, Projectile, PowerUp)
  *   Note: Enemy type 'CyberSalmon' and Projectile type 'Missile' were never implemented
  * Non-colliding (by a lot or by just a little bit)
  * Non-colliding but positioned at either edge of the screen (top vs. bottom, left vs. right, corners)
  * Touching borders (right-left, left-right, top-bottom, bottom-top, corners)
  * Overlapping (same scenarios as touching borders, plus centred/'completely' overlapping)
  * Multiple touching/overlapping [though the function will only deal with two at a time]
  * An object compared to itself
  * Inactive GameObjects should not participate in collision detection (with each other or with active ones)
  */

  // initialize the game (to get width and height values for the game screen)
  Width = 800
  Height = 600

  // objects to use for testing, all initialized in roughly the middle of the screen
  // TODO: write wrapper for parameters?
  val player = new Player(Width / 2, Height / 2)
  val enemyDrone = new Drone(Width / 2, Height / 2, Down)
  val enemyFighter = new Fighter(Width / 2, Height / 2, Down)
  val enemyTurtle = new SpaceTurtle(Width / 2, Height / 2, Down)
  val enemyTurtle2 = new SpaceTurtle(Width / 2, Height / 2, Down)
  val enemyBee = new CosmicBee(Width / 2, Height / 2, Down)
  val enemyDragon = new GalacticDragon(Width / 2, Height / 2, Down)
  val projectileBullet = new Bullet(Width / 2, Height / 2, Down)
  val projectilePBullet = new PBullet(Width / 2, Height / 2, Down)
  val projectilePBullet2 = new PBullet(Width / 2, Height / 2, Down)
  val powerUpHP = new PowerUp(PowerHPID, Width / 2, Height / 2, Left)
  val powerUpHP2 = new PowerUp(PowerHPID, Width / 2, Height / 2, Left)
  val powerUpShots = new PowerUp(PowerShotsID, Width / 2, Height / 2, Left)

  // TODO: Improvements for future: Do all permutations (or at least more of them); write function that takes a matrix??

  // this test covers the 'overlapping' and 'multiple objects overlapping' scenarios
  test("Completely overlapping objects") {
    assert(player.collision(enemyDrone) === true)
    assert(player.collision(projectileBullet) === true)
    assert(player.collision(powerUpHP) === true)
    assert(enemyFighter.collision(player) === true)
    assert(enemyBee.collision(enemyTurtle) === true)
    assert(enemyDragon.collision(projectilePBullet) === true)
    assert(enemyDrone.collision(powerUpShots) === true)
    assert(projectileBullet.collision(player) === true)
    assert(projectilePBullet.collision(enemyTurtle) === true)
    assert(projectileBullet.collision(projectilePBullet) === true)
    assert(projectilePBullet.collision(powerUpShots) === true)
    assert(powerUpShots.collision(player) === true)
    assert(powerUpHP.collision(enemyBee) === true)
    assert(powerUpHP.collision(projectileBullet) === true)
    assert(powerUpShots.collision(powerUpHP) === true)
    assert(enemyTurtle2.collision(enemyTurtle) === true)
    assert(projectilePBullet.collision(projectilePBullet2) === true)
    assert(powerUpHP2.collision(powerUpShots) === true)
  }

  // this test ensures that inactive game objects are ignored by the collision function
  test("Inactive objects") {
    // deactivate some of the test objects (this action is irreversible)
    enemyTurtle2.inactivate
    projectilePBullet2.inactivate
    powerUpHP2.inactivate

    assert(enemyTurtle2.collision(enemyTurtle) === false)
    assert(projectilePBullet.collision(projectilePBullet2) === false)
    assert(powerUpHP2.collision(powerUpHP) === false)
    assert(enemyTurtle2.collision(powerUpHP2) === false)
    assert(projectilePBullet2.collision(enemyTurtle2) === false)
    assert(powerUpHP2.collision(projectilePBullet2) === false)
    assert(enemyTurtle2.collision(player) === false)
    assert(projectilePBullet2.collision(enemyBee) === false)
    assert(powerUpHP2.collision(player) === false)
  }

  // this test is the most "realistic" scenario where objects collide but are centered at different coordinates
  test("Slightly overlapping objects") {
    // slightly move some test objects
    player.x = player.x + 20
    player.y = player.y - 10
    enemyBee.x = enemyBee.x - 30
    projectilePBullet.y = projectilePBullet.y + 15

    assert(player.collision(projectileBullet) === true)
    assert(player.collision(enemyFighter) === true)
    assert(player.collision(powerUpShots) === true)
    assert(enemyDrone.collision(player) === true)
    assert(enemyBee.collision(enemyDragon) === true)
    assert(projectilePBullet.collision(enemyTurtle) === true)
    assert(projectilePBullet.collision(enemyDrone) === true)
  }

  // this test covers the 'non-colliding' and 'positioned at edge of screen' scenarios
  test("Non-colliding objects") {
    // these objects should no longer be colliding after the previous test ("slightly overlapping objects")
    assert(player.collision(enemyBee) === false)
    assert(projectilePBullet.collision(enemyBee) === false)

    // move some objects to the edges of the screen
    enemyDrone.x = enemyDrone.width / 2  // left edge
    projectilePBullet.x = Width - (projectilePBullet.width / 2) // right edge
    powerUpShots.y = powerUpShots.height / 2  // top edge
    player.y = Height - (player.height / 2) // bottom edge

    assert(enemyDrone.collision(projectilePBullet) === false)
    assert(enemyDrone.collision(player) === false)
    assert(enemyDrone.collision(powerUpHP) === false)
    assert(projectilePBullet.collision(powerUpHP) === false)
    assert(projectilePBullet.collision(powerUpShots) === false)
    assert(player.collision(powerUpShots) === false)
    assert(player.collision(projectileBullet) === false)
    assert(player.collision(projectilePBullet) === false)
    assert(powerUpShots.collision(enemyDragon) === false)
    assert(powerUpShots.collision(enemyDrone) === false)
  }

  // this test covers the 'touching borders' and 'just barely not touching' scenarios
  test("Bordering objects") {
    // move some objects so they touch other objects
    enemyFighter.x = player.x + (player.width / 2) + (enemyFighter.width / 2)
    enemyFighter.y = player.y
    enemyTurtle.y = powerUpShots.y + (powerUpShots.height / 2) + (enemyTurtle.height / 2)
    enemyTurtle.x = powerUpShots.x
    projectilePBullet.x = enemyDragon.x + (projectilePBullet.width / 2) + (enemyDragon.width / 2)
    projectilePBullet.y = enemyDragon.y - (projectilePBullet.height / 2) - (enemyDragon.height / 2)

    // TODO: more permutations
    assert(player.collision(enemyFighter) === true)
    assert(enemyTurtle.collision(powerUpShots) === true)
    assert(projectilePBullet.collision(enemyDragon) === true)

    // move the objects so they're no longer touching
    enemyFighter.x += 1
    enemyTurtle.y += 1
    projectilePBullet.x += 1

    assert(player.collision(enemyFighter) === false)
    assert(enemyTurtle.collision(powerUpShots) === false)
    assert(projectilePBullet.collision(enemyDragon) === false)
  }

  // trivial; an active object is always "colliding" with itself, and inactive objects never collide with anything
  test("Object compared to itself") {
    // active objects
    assert(player.collision(player) === true)
    assert(enemyDrone.collision(enemyDrone) === true)
    assert(enemyFighter.collision(enemyFighter) === true)
    assert(enemyTurtle.collision(enemyTurtle) === true)
    assert(enemyBee.collision(enemyBee) === true)
    assert(enemyDragon.collision(enemyDragon) === true)
    assert(projectilePBullet.collision(projectilePBullet) === true)
    assert(projectileBullet.collision(projectileBullet) === true)
    assert(powerUpHP.collision(powerUpHP) === true)
    assert(powerUpShots.collision(powerUpShots) === true)

    // inactive objects
    assert(enemyTurtle2.collision(enemyTurtle2) === false)
    assert(projectilePBullet2.collision(projectilePBullet2) === false)
    assert(powerUpHP2.collision(powerUpHP2) === false)
  }
}
