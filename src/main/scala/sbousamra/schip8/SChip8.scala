package sbousamra.schip8

import org.newdawn.slick._

import scala.util.Random

case class SChip8(var emulator: Emulator) extends BasicGame("schip8") {
  var name = "bass"
  def run: Unit = {
    val appgc = new AppGameContainer(this)
    appgc.setDisplayMode(640, 480, false)
    appgc.setTargetFrameRate(60)
    appgc.start()
  }

  override def init(container: GameContainer): Unit = {
    // do nothing because 
  }

  override def update(container: GameContainer, delta: Int): Unit = {
    emulator = emulator.executeOpcode(emulator)
  }

  override def render(container: GameContainer, g: Graphics): Unit = {
    //run
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
