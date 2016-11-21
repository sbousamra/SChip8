package EmulatorUnitTest

import SChip8.{Emulator, Opcodes}
import org.scalatest.{FunSpec, Matchers}

object EmulatorUnitTest {
  val testEmulator = Emulator.createEmulator

  def _ANNNTest(emulator: Emulator): Unit = {
    val newEmulator = Opcodes._ANNN(emulator)
    println(emulator)
    println(newEmulator)
  }
}
