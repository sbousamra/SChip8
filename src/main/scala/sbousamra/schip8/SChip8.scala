package sbousamra.schip8

import org.newdawn.slick._

import scala.util.Random

case class SChip8(var emulator: Emulator) extends BasicGame("schip8") {
  def run: Unit = {
    val appgc = new AppGameContainer(this)
    appgc.setDisplayMode(640, 480, false)
    appgc.setTargetFrameRate(60)
    appgc.start()
  }

  override def init(container: GameContainer): Unit = {
    // do nothing
  }

  override def update(container: GameContainer, delta: Int): Unit = {
    emulator = emulator.executeOpcode(emulator)
  }

  override def render(container: GameContainer, g: Graphics): Unit = {
    val tetrisSpriteOn = new Image("src\\main\\resources\\graphics\\1x1pixel.png")
    val pixelsOnScreen = emulator.screen.data.zipWithIndex.map {y => y._1.zipWithIndex.map { x =>
      if (x._1 == true) {
        val xScalingFactor = (container.getWidth/y._1.length)
        val yScalingFactor = (container.getHeight/(emulator.screen.data.length))
        tetrisSpriteOn.draw((x._2 * xScalingFactor), (y._2 * yScalingFactor), xScalingFactor, yScalingFactor)
      }
    }
    }
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
