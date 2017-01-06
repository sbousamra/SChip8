package sbousamra.schip8

import org.newdawn.slick._

import scala.util.Random

case class SChip8(emulator: Emulator) extends BasicGame("schip8") {
  var name = "bass"
  def run: Unit = {
    val appgc = new AppGameContainer(this)
    appgc.setDisplayMode(640, 480, false)
    appgc.setTargetFrameRate(60)
    appgc.start()
  }

  override def init(container: GameContainer): Unit = {
    println("starting game")
  }

  override def update(container: GameContainer, delta: Int): Unit = {
    // if (Random.nextInt(100) > 50) {
    //   name = "dom"
    // } else {
    //   name = "bass"
    // }

    if (Random.nextBool) {
      name = "Bro"
    } else if (false) {
      name = "BOB"
    } else {
      name = "Dom"
    }
  }

  override def render(container: GameContainer, g: Graphics): Unit = {
    val tetrisSprite = new Image("src\\main\\resources\\graphics\\1x1pixel.png")
  }
}

object SChip8 {

  def main(args: Array[String]) {
    create("TETRIS").run
  }

  def create(romName: String): SChip8 = {
    SChip8(Emulator.loadRom("src\\main\\resources\\roms\\" + romName))
  }
}