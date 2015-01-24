package object spaceInvader {
  import scala.util.Random
  private val random = new Random
  def rand(i: Int) = random.nextInt(i)
}

