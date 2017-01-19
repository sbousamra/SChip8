package sbousamra.schip8

import org.newdawn.slick
import org.newdawn.slick.Input

import scala.annotation.tailrec
import scala.runtime.RichInt
import scala.util.Random

case class Emulator(
  memory: Memory,
  programCounter: Int,
  stackPointer: Int,
  stack: List[Int],
  vRegister: List[Int],
  iRegister: Int,
  soundTimer: Int,
  delayTimer: Int,
  keyInput: List[Boolean],
  screen: Screen
) {

  def run: Unit = {
    loop
  }

  @tailrec
  private def loop: Unit = {
    println(getRawOpcode.toHexString)
    println(vRegister)
    println(programCounter.toHexString)
    println(delayTimer)
    println("\n")
    executeOpcode(this).loop
  }

  def getRawOpcode = {
    val rawOpcode = memory.data(programCounter) << 8 | memory.data(programCounter + 1)
    rawOpcode
  }

  def getTopOfStack: Int = {
    stack.last
  }

  def keyPressed: Option[Int] = {
    keyInput.zipWithIndex.find(key => key._1 == true).map(key => key._2)
  }

  def executeOpcode(emulator: Emulator): Emulator = {
    val rawOpcode = emulator.getRawOpcode
    val decodedOpcode = rawOpcode & 0xf000
    decodedOpcode match {
      case 0x0000 =>
        val zeroDecodedOpcode = rawOpcode & 0x00ff
        zeroDecodedOpcode match {
          case 0x00e0 => Opcodes._00E0(emulator, rawOpcode)
          case 0x00ee => Opcodes._00EE(emulator, rawOpcode)
        }
      case 0x1000 => Opcodes._1NNN(emulator, rawOpcode)
      case 0x2000 => Opcodes._2NNN(emulator, rawOpcode)
      case 0x3000 => Opcodes._3XKK(emulator, rawOpcode)
      case 0x4000 => Opcodes._4XKK(emulator, rawOpcode)
      case 0x5000 => Opcodes._5XY0(emulator, rawOpcode)
      case 0x6000 => Opcodes._6XKK(emulator, rawOpcode)
      case 0x7000 => Opcodes._7XKK(emulator, rawOpcode)
      case 0x8000 =>
        val eightDecodedOpcode = rawOpcode & 0xf00f
        eightDecodedOpcode match {
          case 0x8000 => Opcodes._8XY0(emulator, rawOpcode)
          case 0x8001 => Opcodes._8XY1(emulator, rawOpcode)
          case 0x8002 => Opcodes._8XY2(emulator, rawOpcode)
          case 0x8003 => Opcodes._8XY3(emulator, rawOpcode)
          case 0x8004 => Opcodes._8XY4(emulator, rawOpcode)
          case 0x8005 => Opcodes._8XY5(emulator, rawOpcode)
          case 0x8006 => Opcodes._8XY6(emulator, rawOpcode)
          case 0x8007 => Opcodes._8XY7(emulator, rawOpcode)
          case 0x800e => Opcodes._8XYE(emulator, rawOpcode)
        }
      case 0x9000 => Opcodes._9XY0(emulator, rawOpcode)
      case 0xa000 => Opcodes._ANNN(emulator, rawOpcode)
      case 0xb000 => Opcodes._BNNN(emulator, rawOpcode)
      case 0xd000 => Opcodes._DXYN(emulator, rawOpcode)
      case 0xc000 => Opcodes._CXKK(emulator, rawOpcode, Random.nextInt(256))
      case 0xf000 =>
        val fDecodedOpcode = (rawOpcode & 0xf0ff)
        fDecodedOpcode match {
          case 0xf007 => Opcodes._FX07(emulator, rawOpcode)
          case 0xf00a => Opcodes._FX0A(emulator, rawOpcode)
          case 0xf015 => Opcodes._FX15(emulator, rawOpcode)
          case 0xf018 => Opcodes._FX18(emulator, rawOpcode)
          case 0xf01e => Opcodes._FX1E(emulator, rawOpcode)
          case 0xf029 => Opcodes._FX29(emulator, rawOpcode)
          case 0xf033 => Opcodes._FX33(emulator, rawOpcode)
        }
      case 0xe000 =>
        val eDecodedOpcode = (rawOpcode & 0xf0ff)
        eDecodedOpcode match {
          case 0xe09e => Opcodes._EX9E(emulator, rawOpcode)
          case 0xe0a1 => Opcodes._EXA1(emulator, rawOpcode)
        }
    }
  }
}

object Emulator {

  def createEmulator: Emulator = {
    Emulator(
      memory = Memory.empty,
      programCounter = 0x200,
      stackPointer = 0,
      stack = List.fill(16)(0),
      vRegister = List.fill(16)(0),
      iRegister = 0,
      soundTimer = 0,
      delayTimer = 0,
      keyInput = List.fill(16)(false),
      screen = Screen.emptyScreen
    )
  }

  def loadRom(rom: String): Emulator = {
    val loadedRom = Memory.loadRomIntoMemory(rom)
    createEmulator.copy(memory = loadedRom)
  }
}
