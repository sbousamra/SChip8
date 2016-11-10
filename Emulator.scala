package SChip8

object SChip8 {

  def main(args: Array[String]) {
    val rom: List[Byte] = Memory.loadRom("C:\\Users\\Administrator\\Desktop\\Programming\\SChip8\\TETRIS")
    println(rom)
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

  def opcodeToInstruction(rawOpcode: Int) = {
    def _0NNN = {
      val zeroOpcodeCheck = rawOpcode & 0xf0ff

      if (zeroOpcodeCheck == 0x00e0) {
        _00E0
      } else if (zeroOpcodeCheck == 0x00ee) {
        _00EE
      } else {
        println("Unknown _0NNN instruction " + rawOpcode)
      }
    }

    def _00E0 = {
      val updatedDisplay: List[List[Int]] = List.fill(32)(List.fill(64)(0))
      val updatedProgramCounter: Int = programCounter + 2
    }

    def _00EE = {
      val intermediateProgramCounter: Int = stack(stackPointer)
      val updatedStackPointer: Int = stackPointer - 1
      val updatedProgramCounter: Int = intermediateProgramCounter + 2
    }

    def _1NNN = {
      val updatedProgramCounter: Int = (rawOpcode & 0x0fff)
    }

    def _2NNN = {
      val updatedStackPointer = stackPointer + 1
      val updatedStack = stack.updated(updatedStackPointer, programCounter)
      val updatedProgramCounter = (rawOpcode & 0x0fff)
    }

    def _3XKK = {
      val source = (rawOpcode & 0x0f00) >> 8

      if (vRegister(source) == (rawOpcode & 0x00ff)) {
        val updatedProgramcounter = programCounter + 4
      } else {
        val updatedProgramCounter = programCounter + 2
      }
    }

    def _4XKK = {
      val source = (rawOpcode & 0x0f00) >> 8

      if (vRegister(source) != (rawOpcode & 0x00ff)) {
        val updatedProgramCounter = programCounter + 4
      } else {
        val updatedProgramCounter = programCounter + 2
      }
    }

    def _5XY0 = {
      val source = (rawOpcode & 0x0f00) >> 8
      val target = (rawOpcode & 0x00f0) >> 4

      if (vRegister(source) == vRegister(target)) {
        val updatedProgramCounter = programCounter + 4
      } else {
        val updatedProgramCounter = programCounter + 2
      }
    }

    def _6XKK = {
      val target = (rawOpcode & 0x0f00) >> 8
      val updatedVregister = vRegister.updated(target, (rawOpcode & 0x00ff))
      val updatedProgramCounter = programCounter + 2
    }

    def _7XKK = {
      val target = (rawOpcode & 0x0f00) >> 8
      val updatedVregister = vRegister.updated(target, ((vRegister(target) + (rawOpcode & 0x00ff)) & 0xff))
      val updatedProgramCounter = programCounter + 2
    }

    def _8XYN = {
      val eightOpcodeCheck = (rawOpcode & 0xf00f)

      if (eightOpcodeCheck == 0x8000) {
        _8XY0(rawOpcode)
      } else if (eightOpcodeCheck == 0x8001) {
        _8XY1(rawOpcode)
      } else if (eightOpcodeCheck == 0x8002) {
        _8XY2(rawOpcode)
      } else if (eightOpcodeCheck == 0x8003) {
        _8XY3(rawOpcode)
      } else if (eightOpcodeCheck == 0x8004) {
        _8XY4(rawOpcode)
      } else if (eightOpcodeCheck == 0x8005) {
        _8XY5(rawOpcode)
      } else if (eightOpcodeCheck == 0x8006) {
        _8XY6(rawOpcode)
      } else if (eightOpcodeCheck == 0x8007) {
        _8XY7(rawOpcode)
      } else if (eightOpcodeCheck == 0x800e) {
        _8XYE(rawOpcode)
      } else {
        print("Unknown _8XYN instruction " + rawOpcode)
      }
    }

    def _8XY0 = {
      val target = (rawOpcode & 0x0f00) >> 8
      val source = (rawOpcode & 0x00f0) >> 4
      val updatedVregister = vRegister.updated(target, vRegister(source))
      val updatedProgramCounter = programCounter + 2
    }

    def _8XY1 = {
      val target = (rawOpcode & 0x0f00) >> 8
      val source = (rawOpcode & 0x00f0) >> 4
      val updatedVregister = vRegister.updated(target, (vRegister(target) | vRegister(source)))
      val updatedProgramCounter = programCounter + 2
    }

    def _8XY2 = {
      val target = (rawOpcode & 0x0f00) >> 8
      val source = (rawOpcode & 0x00f0) >> 4
      val updatedVregister = vRegister.updated(target, (vRegister(target) & vRegister(source)))
      val updatedProgramCounter = programCounter + 2
    }

    def _8XY3 = {
      val target = (rawOpcode & 0x0f00) >> 8
      val source = (rawOpcode & 0x00f0) >> 4
      val updatedVregister = vRegister.updated(target, (vRegister(target) ^ vRegister(source)))
      val updatedProgramCounter = programCounter + 2
    }

    def _8XY4 = {
      val target = (rawOpcode & 0x0f00) >> 8
      val source = (rawOpcode & 0x00f0) >> 4
      val temporaryValue = vRegister(target) + vRegister(source)

      if (temporaryValue > 255) {
        val intermediateVregister = vRegister.updated(0xf, 1)
      } else {
        val intermediateVregister = vRegister.updated(0xf, 0)
      }

      //      val updatedVregister = intermediateVregister.updated(target, ((vRegister(target) + vRegister(source)) & 0xff)) how do i access the result of the boolean above to use in this expression?
      val updatedProgramCounter = programCounter + 2
    }

    def _8XY5 = {
      val target = (rawOpcode & 0x0f00) >> 8
      val source = (rawOpcode & 0x00f0) >> 4
      val vRegisterTargetValue = vRegister(target)
      val vRegisterSourceValue = vRegister(source)

      if (vRegisterTargetValue > vRegisterSourceValue) {
        val intermediateVregister = vRegister.updated(0xf, 1)
      }
      else {
        val intermediateVregister = vRegister.updated(0xf, 0)
      }

      val updatedVregister = intermediateVregister.updated(vRegister(target), (vRegister(target) - vRegister(source)) & 0xff)
    }
  }
//    }

//  def executeOpcode(rawOpcode: Int): Emulator = {
//    val decodedOpcode = rawOpcode & 0xf000
//    val runInstruction = {
//      if (decodedOpcode == 0x0000)
//        _0NNN(rawOpcode)
//
//      else if (decodedOpcode == 0x1000)
//        _1NNN(rawOpcode)
//
//      else if (decodedOpcode == 0x2000)
//        _2NNN(rawOpcode)
//
//      else if (decodedOpcode == 0x3000)
//        _3XKK(rawOpcode)
//
//      else if (decodedOpcode == 0x4000)
//        _4XKK(rawOpcode)
//
//      else if (decodedOpcode == 0x5000)
//        _5XYO(rawOpcode)
//
//      else if (decodedOpcode == 0x6000)
//        _6XKK(rawOpcode)
//
//      else if (decodedOpcode == 0x7000)
//        _7XKK(rawOpcode)
//
//      else if (decodedOpcode == 0x8000)
//        _8XYN(rawOpcode)
//
//      else if (decodedOpcode == 0x9000)
//        _9XY0(rawOpcode)
//
//      else if (decodedOpcode == 0xa000)
//        _ANNN(rawOpcode)
//
//      else if (decodedOpcode == 0xd000)
//        _DXYN(rawOpcode)
//
//      else if (decodedOpcode == 0xc000)
//        _CXKK(rawOpcode)
//
//      else if (decodedOpcode == 0xf000)
//        _F000(rawOpcode)
//
//      else if (decodedOpcode == 0xe000)
//        _E000(rawOpcode)
//
//      else
//        raise Exception("Unknown Opcode: " + hex(rawopcode))
//    }
//    runInstruction
//  }

//  def mainEmulationLoop: Boolean = {
//      ???
//  }
}
