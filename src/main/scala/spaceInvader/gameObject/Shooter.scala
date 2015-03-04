package com.github.fellowship_of_the_bus
package spaceInvader.gameObject
import GameObject._

trait Shooter {
  def x: Int
  def y: Int
  def width: Int
  def height: Int
  def dir: Char
  def shotType: Int

  def shotInterval: Int
  var shotTimer = 0

  def numShot: Int
  def shotDelay: Int

  var shotDelayTimer = shotInterval/2
  var numShotLeft = numShot
  def tick() = {
    if (shotTimer > 0) {
      shotTimer -= 1
    }
    if (shotDelayTimer > 0) {
      shotDelayTimer -= 1
    }
  }

  def shoot = {
    if (shotTimer == 0) {
      shotDelayTimer = 0
      numShotLeft = numShot
    }

    if( shotDelayTimer == 0 && numShotLeft > 0) {
      var px: Int = x
      var py: Int = y

      if (dir == Up) { 
        py -= (height + Projectile.height) / 2
      } else if (dir == Down) { 
        py += (height + Projectile.height) / 2 
      } else if (dir == Left) { 
        px -= (width + Projectile.width) / 2 
      } else if (dir == Right) { 
        px += (width + Projectile.width) / 2 
      }
      numShotLeft -= 1
      shotDelayTimer = shotDelay
      shotTimer = shotInterval
      Some(Projectile(shotType, px, py, dir));
    } else {
      None
    }
  }
}



