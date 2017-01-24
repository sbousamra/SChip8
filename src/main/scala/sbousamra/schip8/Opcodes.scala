package sbousamra.schip8

import java.awt.event.{KeyEvent, KeyListener}
import java.util.EventListener

import org.newdawn.slick.{AppGameContainer, Input, InputListener}

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
    val target = (rawOpcode & 0x00ff)
    if (emulator.vRegister(source) == (target)) {
      val newProgramCounter = emulator.programCounter + 4
      emulator.copy(programCounter = newProgramCounter)
    } else {
      val newProgramCounter = emulator.programCounter + 2
      emulator.copy(programCounter = newProgramCounter)
    }
  }

  def _4XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00ff)
    if (emulator.vRegister(source) != target) {
      val newProgramCounter = emulator.programCounter + 4
      emulator.copy(programCounter = newProgramCounter)
    } else {
      val newProgramCounter = emulator.programCounter + 2
      emulator.copy(programCounter = newProgramCounter)
    }
  }

  def _5XY0(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    if (emulator.vRegister(source) == emulator.vRegister(target)) {
      val newProgramCounter = emulator.programCounter + 4
      emulator.copy(programCounter = newProgramCounter)
    } else {
      val newProgramCounter = emulator.programCounter + 2
      emulator.copy(programCounter = newProgramCounter)
    }
  }

  def _6XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(source, (rawOpcode & 0x00ff))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _7XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(source, ((emulator.vRegister(source) + (rawOpcode & 0x00ff)) & 0xff))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _8XY0(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    val newvRegister = emulator.vRegister.updated(source, emulator.vRegister(target))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _8XY1(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    val newvRegister = emulator.vRegister.updated(source, (emulator.vRegister(source) | emulator.vRegister(target)))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _8XY2(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    val newvRegister = emulator.vRegister.updated(source, (emulator.vRegister(source) & emulator.vRegister(target)))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _8XY3(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    val newvRegister = emulator.vRegister.updated(source, (emulator.vRegister(source) ^ emulator.vRegister(target)))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _8XY4(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    val newProgramCounter = emulator.programCounter + 2
    val intermediatevRegister = emulator.vRegister.updated(source, ((emulator.vRegister(source) + emulator.vRegister(target)) & 0xff))
    if ((emulator.vRegister(source) + emulator.vRegister(target)) > 255) {
      val newvRegister = intermediatevRegister.updated(0xf, 1)
      emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    } else {
      val newvRegister = intermediatevRegister.updated(0xf, 0)
      emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    }
  }

  def _8XY5(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    val newProgramCounter = emulator.programCounter + 2
    val intermediatevRegister = emulator.vRegister.updated(source, ((emulator.vRegister(source) - emulator.vRegister(target))))
    if ((emulator.vRegister(source) > emulator.vRegister(target))) {
      val newvRegister = intermediatevRegister.updated(0xf, 1)
      emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    } else {
      val newvRegister = intermediatevRegister.updated(0xf, 0)
      emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    }
  }

  def _8XY6(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val leastSignificantBit = (emulator.vRegister(source) & 1)
    val newProgramCounter = emulator.programCounter + 2
    val newfRegister = if (leastSignificantBit == 1) 1 else 0
    emulator.copy(
      vRegister = emulator.vRegister.updated(0xf, newfRegister).updated(source, (emulator.vRegister(source)/2)),
      programCounter = newProgramCounter
    )
  }

  def _8XY7(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val target = (rawOpcode & 0x00f0) >> 4
    val newProgramCounter = emulator.programCounter + 2
      val intermediatevRegister = emulator.vRegister.updated(source, ((emulator.vRegister(target) - emulator.vRegister(source))))
    if ((emulator.vRegister(target) > emulator.vRegister(source))) {
      val newvRegister = intermediatevRegister.updated(0xf, 1)
      emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    } else {
      val newvRegister = intermediatevRegister.updated(0xf, 0)
      emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    }
  }

  def _8XYE(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val mostSignificantBit = (emulator.vRegister(source) >> 7)
    val newProgramCounter = emulator.programCounter + 2
    val newfRegister = if (mostSignificantBit == 1) 1 else 0
    emulator.copy(
      vRegister = emulator.vRegister.updated(0xf, newfRegister).updated(source, (emulator.vRegister(source) * 2)),
      programCounter = newProgramCounter
    )
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

  def _BNNN(emulator: Emulator, rawOpcode: Int): Emulator = {
    val target = (rawOpcode & 0x0fff)
    val newProgramCounter = target + emulator.vRegister(0)
    emulator.copy(programCounter = newProgramCounter)
  }

  def _CXKK(emulator: Emulator, rawOpcode: Int, randomInt: => Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(source, (randomInt & (rawOpcode & 0x00ff)))
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _DXYN(emulator: Emulator, rawOpcode: Int) = {
    val xCoordinate = emulator.vRegister((rawOpcode & 0x0f00) >> 8) % 64
    val yCoordinate = emulator.vRegister((rawOpcode & 0x00f0) >> 4) % 32
    val height = rawOpcode & 0x000F
    var carryFlag = 0
    var newScreen = emulator.screen

    for (yline <- 0 until height) {
      val sprite = emulator.memory.data(emulator.iRegister + yline)
      for (xpixel <- 0 until 8) {
        if ((sprite & (0x80 >> xpixel)) != 0) {
          val xPixelPosition = xCoordinate + xpixel
          val yPixelPosition = yCoordinate + yline
          val bitFromScreen = newScreen.getCoordinate(xPixelPosition, yPixelPosition) == true
          val newxData = newScreen.setCoordinate(xPixelPosition, yPixelPosition, bitFromScreen ^ true)
          val newScreenData = newScreen.data.updated(yPixelPosition, newxData)
          newScreen = emulator.screen.copy(data = newScreenData)
          if (bitFromScreen == true) {
            carryFlag = 1
          }
        }
      }
    }
    val newvRegister = emulator.vRegister.updated(0xf, carryFlag)
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, screen = newScreen, programCounter = newProgramCounter)
  }

  def _EX9E(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val vRegisterValue = emulator.vRegister(source)
    val newProgramCounter = emulator.keyPressed match {
      case Some(key) => if (key == vRegisterValue) {
        emulator.programCounter + 4
      } else {
        emulator.programCounter + 2
      }
      case None => emulator.programCounter + 2
    }
    emulator.copy(programCounter = newProgramCounter, keyInput = List.fill(16)(false))
  }

  def _EXA1(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val vRegisterValue = emulator.vRegister(source)
    val newProgramCounter = emulator.keyPressed match {
      case Some(key) => if (key != vRegisterValue) {
        emulator.programCounter + 4
      } else {
        emulator.programCounter + 2
      }
      case None => emulator.programCounter + 4
    }
    emulator.copy(programCounter = newProgramCounter, keyInput = List.fill(16)(false))
  }

  def _FX07(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(source, emulator.delayTimer)
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
  }

  def _FX0A(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    emulator.keyPressed match {
      case Some(key) =>
        val newvRegister = emulator.vRegister.updated(source, key)
        val newProgramCounter = emulator.programCounter + 2
        emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter, keyInput = List.fill(16)(false))
      case None => emulator
    }
  }

  def _FX15(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newDelayTimer = emulator.vRegister(source)
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(delayTimer = newDelayTimer, programCounter = newProgramCounter)
  }

  def _FX18(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newSoundTimer = emulator.vRegister(source)
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(soundTimer = newSoundTimer, programCounter = newProgramCounter)
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

  def _FX29(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val newiRegister = emulator.vRegister(source) * 5
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(iRegister = newiRegister, programCounter = newProgramCounter)
  }

  def _FX33(emulator: Emulator, rawOpcode: Int): Emulator = {
    val source = (rawOpcode & 0x0f00) >> 8
    val vRegisterValue = emulator.vRegister(source)
    val newMemoryData = emulator.memory.data.updated(emulator.iRegister, (vRegisterValue/100))
      .updated((emulator.iRegister + 1), ((vRegisterValue % 100)/10))
      .updated((emulator.iRegister + 2), ((vRegisterValue % 100) % 10))
    val newMemory = emulator.memory.copy(data = newMemoryData)
    val newProgramCounter = emulator.programCounter + 2
    emulator.copy(memory = newMemory, programCounter = newProgramCounter)
  }
}
