package sbousamra.schip8

case class Emulator(
  memory: Memory,
  programCounter: Int,
  stackPointer: Int,
  stack: List[Int],
  vRegister: List[Int],
  iRegister: Int,
  soundTimer: Int,
  delayTimer: Int,
  keyInput: List[Int],
  screen: List[List[Int]]
) {

  def run: Unit = {
    executeOpcode(this)
  }

  def getRawOpcode = {
    val rawOpcode = memory.data(programCounter) << 8 | memory.data(programCounter + 1)
    rawOpcode
  }

  def getTopOfStack: Int = {
    stack.last
  }

  def executeOpcode(emulator: Emulator): Emulator = {
    val rawOpcode = emulator.getRawOpcode
    println(rawOpcode)
    val decodedOpcode = rawOpcode & 0xf000
    val instruction = decodedOpcode match {
      case 0x0000 =>
        val zeroDecodedOpcode = rawOpcode & 0x00ff
        zeroDecodedOpcode match {
          case 0x00e0 => Opcodes._00E0(emulator, rawOpcode)
          case 0x00ee => Opcodes._00EE(emulator, rawOpcode)
        }
//      case 0x1000 => Opcodes._1NNN(emulator, rawOpcode)
      case 0x2000 => Opcodes._2NNN(emulator, rawOpcode)
//      case 0x3000 => Opcodes._3XKK(emulator)
//      case 0x4000 => Opcodes._4XKK(emulator)
//      case 0x5000 => Opcodes._5XYO(emulator)
      case 0x6000 => Opcodes._6XKK(emulator, rawOpcode)
//      case 0x7000 => Opcodes._7XKK(emulator)
//      case 0x8000 => Opcodes._8XYN(emulator)
//      case 0x9000 => Opcodes._9XY0(emulator)
      case 0xa000 => Opcodes._ANNN(emulator, rawOpcode)
//      case 0xd000 => Opcodes._DXYN(emulator)
//      case 0xc000 => Opcodes._CXKK(emulator)
//      case 0xf000 => Opcodes._F000(emulator)
//      case 0xe000 => Opcodes._E000(emulator)
    }
    executeOpcode(instruction)
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
      keyInput = List.fill(16)(0),
      screen = List.fill(32)(List.fill(64)(0))
    )
  }

  def loadRom(rom: String): Emulator = {
    val loadedRom = Memory.loadRomIntoMemory(rom)
    createEmulator.copy(memory = loadedRom)
  }
}