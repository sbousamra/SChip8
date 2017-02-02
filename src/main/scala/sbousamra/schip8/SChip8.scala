package sbousamra.schip8

import org.newdawn.slick._

import scala.util.Random

case class SChip8(var emulator: Emulator) extends BasicGame("schip8") {
  def run: Unit = {
    val appgc = new AppGameContainer(this)
    appgc.setDisplayMode(640, 480, false)
    appgc.setVSync(true)
    appgc.setTargetFrameRate(300)
    appgc.start()
  }

  def whichKeyIsPressed(input: Input): Option[Int] = {
    if (input.isKeyPressed(Input.KEY_1)) {
      Some(0x1)
    } else if (input.isKeyPressed(Input.KEY_2)) {
      Some(0x2)
    } else if (input.isKeyPressed(Input.KEY_3)) {
      Some(0x3)
    } else if (input.isKeyPressed(Input.KEY_4)) {
      Some(0xC)
    } else if (input.isKeyPressed(Input.KEY_Q)) {
      Some(0x4)
    } else if (input.isKeyPressed(Input.KEY_W)) {
      Some(0x5)
    } else if (input.isKeyPressed(Input.KEY_E)) {
      Some(0x6)
    } else if ((input.isKeyPressed(Input.KEY_R))) {
      Some(0xD)
    } else if ((input.isKeyPressed(Input.KEY_A))) {
      Some(0x7)
    } else if ((input.isKeyPressed(Input.KEY_S))) {
      Some(0x8)
    } else if ((input.isKeyPressed(Input.KEY_D))) {
      Some(0x9)
    } else if ((input.isKeyPressed(Input.KEY_F))) {
      Some(0xE)
    } else if ((input.isKeyPressed(Input.KEY_Z))) {
      Some(0xA)
    } else if ((input.isKeyPressed(Input.KEY_X))) {
      Some(0x0)
    } else if ((input.isKeyPressed(Input.KEY_C))) {
      Some(0xB)
    } else if ((input.isKeyPressed(Input.KEY_V))) {
      Some(0xF)
    } else {
      None
    }
  }

  override def init(container: GameContainer): Unit = {
    // do nothing
  }

  override def update(container: GameContainer, delta: Int): Unit = {
    val newDelayTimer = if (emulator.delayTimer > 0) {
      (emulator.delayTimer - 1)
    } else {
      0
    }
    val newSoundTimer = if (emulator.soundTimer > 0) {
      (emulator.soundTimer - 1)
    } else {
      0
    }
    val input: Input = container.getInput
    whichKeyIsPressed(input) match {
      case Some(key) => {
        println(key)
        emulator = emulator.executeOpcode(emulator.copy(soundTimer = newSoundTimer, delayTimer = newDelayTimer, keyInput = emulator.keyInput.updated(key, true)))
      }
      case None => emulator = emulator.executeOpcode(emulator.copy(soundTimer = newSoundTimer, delayTimer = newDelayTimer))
    }
  }

  override def render(container: GameContainer, g: Graphics): Unit = {
    val tetrisSpriteOn = new Image("src/main/resources/graphics/1x1pixel.png")
    val pixelsOnScreen = emulator.screen.data.zipWithIndex.map {y =>
      y._1.zipWithIndex.map { x =>
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
    val slick2d = create("TETRIS").run
  }

  def create(romName: String): SChip8 = {
    SChip8(Emulator.loadRom("src/main/resources/roms/" + romName))
  }
}
