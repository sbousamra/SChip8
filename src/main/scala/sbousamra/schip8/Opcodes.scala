package sbousamra.schip8

import scala.util.Random

object Opcodes {

  def _00E0(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newScreen = Screen.emptyScreen
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(screen = newScreen, programCounter = newProgramCounter)
  }

  def _00EE(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newProgramCounter = emulator.stack(emulator.stackPointer) + 2
    val newStackPointer = emulator.stackPointer - 1
    emulator.copy(stackPointer = newStackPointer, programCounter = newProgramCounter)
  }

  def _1NNN(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newProgramCounter = (rawOpcode & 0x0fff)
    emulator.copy(programCounter = newProgramCounter)
  }

  def _2NNN(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newStackPointer = emulator.stackPointer + 1
    val newStack = emulator.stack.updated(newStackPointer, emulator.programCounter)
    val newProgramCounter = (rawOpcode & 0x0fff)
    emulator.copy(stackPointer = newStackPointer, stack = newStack, programCounter = newProgramCounter)
  }

  def _3XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00ff) >> 8
    if (emulator.vRegister(source) == (target)) {
      val newProgramCounter = emulator.programCounter + 4
      emulator.copy(programCounter = newProgramCounter)
    } else {
      val newProgramCounter = emulator.programCounter + 2
      emulator.copy(programCounter = newProgramCounter)
    }
  }

  def _6XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val target = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(target, (rawOpcode & 0x00ff))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _4XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    if (emulator.vRegister(source) != (rawOpcode & 0x00ff)) {
      val newProgramCounter = emulator.programCounter + 4
      emulator.copy(programCounter = newProgramCounter)
    } else {
      val newProgramCounter = emulator.programCounter + 2
      emulator.copy(programCounter = newProgramCounter)
    }
  }

  def _7XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val target = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(target, ((emulator.vRegister(target) + (rawOpcode & 0x00ff)) & 0xff))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _8XY0(emulator: Emulator, rawOpcode: Int): Emulator = {
    val target = (rawOpcode & 0x0f00) >> 8
    val source = (rawOpcode & 0x00f0) >> 4
    val newvRegister = emulator.vRegister.updated(target, emulator.vRegister(source))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _9XY0(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    if (emulator.vRegister(source) != emulator.vRegister(target)) {
      val newProgramCounter = emulator.programCounter + 4
      emulator.copy(programCounter = newProgramCounter)
    } else {
      val newProgramCounter = emulator.programCounter + 2
      emulator.copy(programCounter = newProgramCounter)
    }
  }

  def _ANNN(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newiRegister = (rawOpcode & 0x0fff)
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(iRegister = newiRegister, programCounter = newProgramCounter)
  }

  def _CXKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val target = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(target, (Random.nextInt(256) & (rawOpcode & 0x00ff)))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _DXYN(emulator: Emulator, rawOpcode: Int) = {
    val coordx = emulator.vRegister((rawOpcode & 0x0f00) >> 8)
    val coordy = emulator.vRegister((rawOpcode & 0x00f0) >> 4)
    val height = rawOpcode & 0x000F
    var carryFlag = 0
    var screen = emulator.screen

    for (yline <- 0 until height) {
      val data = emulator.memory.data(emulator.iRegister + yline)
      var xpixelinv = 8
      for (xpixel <- 0 until 8) {
        xpixelinv -= 1
        val mask = 1 << xpixelinv
        if ((data & mask) != 0) {
          val x = coordx + xpixel
          val y = coordy + yline
          if ((x < 64) && (y < 32)) {
            if (screen.data(x)(y) == 1) {
              carryFlag = 1
            }
            screen.data(x)(y) = !screen.data(x)(y) //flip the bool
          }
        }
      }
    }

    val newVRegister = emulator.vRegister.updated(0xF, carryFlag)

    emulator.copy(
      vRegister = newVRegister,
      screen = screen.copy()
    )
  }

  def _FX1E(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val sourceRegister = emulator.vRegister(source)
    val newiRegister = ((emulator.iRegister + (emulator.vRegister(source))) & 0xffff)
    val newProgramCounter = emulator.programCounter + 2
    if ((sourceRegister + emulator.iRegister) > 0xffff) {
      val newvRegister = emulator.vRegister.updated(0xf, 1)
      emulator.copy(vRegister = newvRegister, iRegister = newiRegister, programCounter = newProgramCounter)
    } else {
      val newvRegister = emulator.vRegister.updated(0xf, 0)
      emulator.copy(vRegister = newvRegister, iRegister = newiRegister, programCounter = newProgramCounter)
    }
  }

  def _FX15(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newDelayTimer = emulator.vRegister(source)
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(delayTimer = newDelayTimer, programCounter = newProgramCounter)
  }
}
