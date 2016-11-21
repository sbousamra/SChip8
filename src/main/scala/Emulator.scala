package SChip8

object SChip8 {

  def main(args: Array[String]) {
    val emulator: Emulator = Emulator.createEmulator
    emulator.run
  }
}

case class Emulator (
  memory: Memory,
  programCounter: Int,
  stackPointer: Int,
  stack: List[Int],
  vRegister: List[Int],
  iRegister: Int,
  soundTimer: Int,
  delayTimer: Int,
  keyInput: List[Int]
) {

  def run: Unit = {
    executeOpcode(this)
  }

  def getRawOpcode = {
    val rawOpcode = memory.data(programCounter) << 8 | memory.data(programCounter + 1)
    rawOpcode
  }

  def executeOpcode(emulator: Emulator): Emulator = {
    val decodedOpcode = emulator.getRawOpcode & 0xf000
    val instruction = decodedOpcode match {
//      case 0x0000 => Opcodes._0NNN(emulator)
      case 0x1000 => Opcodes._1NNN(emulator)
//      case 0x2000 => Opcodes._2NNN(emulator)
//      case 0x3000 => Opcodes._3XKK(emulator)
//      case 0x4000 => Opcodes._4XKK(emulator)
//      case 0x5000 => Opcodes._5XYO(emulator)
      case 0x6000 => Opcodes._6XKK(emulator)
//      case 0x7000 => Opcodes._7XKK(emulator)
//      case 0x8000 => Opcodes._8XYN(emulator)
//      case 0x9000 => Opcodes._9XY0(emulator)
      case 0xa000 => Opcodes._ANNN(emulator)
//      case 0xd000 => Opcodes._DXYN(emulator)
//      case 0xc000 => Opcodes._CXKK(emulator)
//      case 0xf000 => Opcodes._F000(emulator)
//      case 0xe000 => Opcodes._E000(emulator)
    }
    executeOpcode(instruction)
  }
}

object Opcodes {
  def _1NNN(emulator: Emulator) = {
    val newProgramCounter = (emulator.getRawOpcode & 0x0fff)
    val newEmulator = emulator.copy(programCounter = newProgramCounter)
    newEmulator
  }

  def _6XKK(emulator: Emulator) = {
    val target = (emulator.getRawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(target, (emulator.getRawOpcode & 0x00ff))
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    newEmulator
  }

  def _ANNN(emulator: Emulator) = {
    val newiRegister = emulator.getRawOpcode & 0x0fff
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(iRegister = newiRegister, programCounter = newProgramCounter)
    newEmulator
  }
}

object Emulator {

  def createEmulator: Emulator = {
    Emulator(
      memory = Memory.loadRom("C:\\Users\\Administrator\\Desktop\\Programming\\SChip8\\TETRIS"),
      programCounter = 0x200,
      stackPointer = 0,
      stack = List.fill(16)(0),
      vRegister = List.fill(16)(0),
      iRegister = 0,
      soundTimer = 0,
      delayTimer = 0,
      keyInput = List.fill(16)(0)
    )
  }
}