![Intergalactic Interlopers][game logo]

![Fellowship of the Bus][logo]

## Installation
There are two recommended options for running this game.

1. Download one of the pre-built executable JAR files under [releases]. Run it by either double-clicking the JAR file or entering the command `java -jar <jar-name>` on the command line.
2. Build from source. The easiest way to do so is to use [sbt]:
    1. Install sbt.
    2. Run sbt from the command line in the project root directory.
    3. At the prompt, type `run`.

## Controls
All controls in this game can be remapped to any keyboard keys as desired. By default, the controls are:

* Move the spaceship: Arrow keys
* Shoot: Space
* Select a menu item / submit your name for the high score board: Enter
* Return to the main menu after a game over: Esc
* Return to the main menu after submitting a score to the high score board: Esc and Enter

## How to Play
The object of the game is to defeat as many enemies as possible before losing all of your health. Your current health is represented by a number of hearts in the lower left corner of the screen.

[![video image]][video]

Movement is essential to staying alive. As the game progresses, the enemies that spawn in each wave increase in both number and difficulty. Each [enemy type](#enemy-types) has different stats and behavior. You take 1 damage if you are hit by an enemy bullet. If you collide with an enemy, they are instantly killed, but you take damage equal to half of their remaining health rounded up. Since the number of onscreen enemies - many of whom fire their own projectiles - increases quickly, the game has the distinct feel of a "bullet hell" game crossed with traditional arcade space shooter games.

![Intergalactic Interlopers][gameplay]

### Player

You control a single small spaceship.

![Player][ship]

    * Health: 10
    * Speed: 5
    * Shoots one bullet every 0.3 seconds

There are two types of power-ups that drop randomly:

1. ![Health Power-up][health] <p> Health power-ups restore 3 hearts, up to a maximum of 10
2. ![Shot Power-up][shot] <p> Shot power-ups provide extra firepower by increasing the number of bullets that you can shoot in quick succession. Currently, there is no limit to the number of shot power-ups that you can accumulate.

When you are defeated, you are given the chance to enter your name (up to 20 characters) to record your play stats on a local high score board.

![Enter your name][gameover]

The score board records the top 10 results by score value. Also recorded on the score board are your number of shots fired, your number of shots hit, your accuracy as a percentage, and the number of shot power-ups you acquired.

![High score board][scoreboard]

### <a name="enemy-types"></a>Enemy Types

Enemies are randomly spawned in waves every 2 seconds from the top of the screen. Each wave of enemies increases in difficulty over the previous wave by randomly including more enemies or stronger enemies. The various types of enemies in the game are:

1. ![Drone][drone]<p> Drone:
    * Health: 1
    * Difficulty Level: 1
    * Shoots one bullet every 3 seconds
    * Moves diagonally down the screen

2. ![Comic Bee][cosmic bee]<p> Cosmic Bee
    * Health: 1
    * Difficulty Level: 1
    * Moves diagonally down the screen very quickly

3. ![Fighter][fighter]<p> Fighter:
    * Health: 3
    * Difficulty Level: 2
    * Shoots one bullet every 2 seconds
    * Moves diagonally down the screen

4. ![Space Turtle][space turtle]<p> Space Turtle:
    * Health: 5
    * Difficulty Level: 3
    * Slowly moves diagonally down the screen

5. ![Galactic Dragon][galactic dragon]<p> Galactic Dragon:
    * Health: 10
    * Difficulty Level: 10
    * Shoots 5 bullets every 2.5 seconds
    * Slowly moves straight down the screen

## Credits

### Programming
* [Rob Schluntz]
* [Abraham Dubrisingh]
* [Kevin Wu]

### Art
* Abraham Dubrisingh, using [SumoPaint](https://www.sumopaint.com/)
* GitHub for the spaceship image

### Sound Effects
* Rob Schluntz, using http://www.bfxr.net/

### Testing
* [Erin Blackmere]

### Special Thanks
* This game was heavily inspired by Space Invaders, Galaga, and other classic arcade-style space shooters
* [Arcade](http://www.fontspace.com/weknow/arcade) font created by weknow
* [Ringbearer](http://www.fontspace.com/pete-klassen/ringbearer) font created by Pete Klassen

[Rob Schluntz]: https://github.com/saitou1024
[Abraham Dubrisingh]: https://github.com/Greatrabe
[Kevin Wu]: https://github.com/smashkevin
[Erin Blackmere]: https://github.com/erin2kb

[action]: images/action.png
[video]: https://www.youtube.com/watch?v=LrigPZlEug0
[video image]: images/video.png
[screenshot]: images/screenshot.png
[scoreboard]: images/scoreboard.png
[gameover]: images/gameover.png

[game logo]: src/main/resources/img/GameLogo.png
[logo]: src/main/resources/img/FotB-Logo.png
[releases]: ../../releases
[sbt]: http://www.scala-sbt.org/

[health]: src/main/resources/img/PowerHP.png
[shot]: src/main/resources/img/PowerShots.png

[cosmic bee]: src/main/resources/img/CosmicBee.png
[drone]: src/main/resources/img/Drone.png
[fighter]: src/main/resources/img/Fighter.png
[galactic dragon]: src/main/resources/img/GalacticDragon.png
[space turtle]: src/main/resources/img/SpaceTurtle.png

[ship]: src/main/resources/img/PlayerR.png
