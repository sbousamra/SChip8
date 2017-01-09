package sbousamra.schip8

import org.scalatest.{FunSpec, Matchers}

class OpcodesSpec extends FunSpec with Matchers {

  def getTestingEmulator: Emulator = {
    Emulator(Memory.empty, 0, 0, List.fill(16)(0), List.fill(16)(0), 0, 0, 0, List.fill(16)(0), Screen.emptyScreen)
  }

  describe("_00E0") {
    it("should clear the display") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._00E0(emulatorBefore, 0x00e0)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
      emulatorAfter.screen.data should be (Screen.emptyScreen.data)
    }
  }

  describe("_00EE") {
    it("should set the program counter to the address at the top of the stack and add 2, then subtract 1 from the stack pointer") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._00EE(emulatorBefore, 0x00ee)
      emulatorAfter.programCounter should be (emulatorBefore.stack(emulatorBefore.stackPointer) + 2)
      emulatorAfter.stackPointer should be (emulatorBefore.stackPointer - 1)
    }
  }

  describe("_1NNN") {
    it("should set the program counter to NNN") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._1NNN(emulatorBefore, 0x1123)
      emulatorAfter.programCounter should be (0x123)
    }
  }

  describe("_2NNN") {
    it("should increment the stack pointer, then put the current program counter on the top of the stack. Then set program counter to nnn") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._2NNN(emulatorBefore, 0x2123)
      emulatorAfter.stackPointer should be (emulatorBefore.stackPointer + 1)
      emulatorAfter.stack(emulatorAfter.stackPointer) should be (emulatorBefore.programCounter)
      emulatorAfter.programCounter should be (0x0123)
    }
  }

  describe("_3XKK") {
    it("should compare register Vx to kk, and if they are equal, increment the program counter by 2") {
      val emulator = getTestingEmulator
      val emulatorBefore = emulator.copy(vRegister = emulator.vRegister.updated(0, 37))
      val emulatorAfter = Opcodes._3XKK(emulatorBefore, 0x3025)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 4)
    }
  }

  describe("_4XKK") {
    it("should compare register Vx to kk, and if they are not equal, increment the program counter by 2") {
      val emulator = getTestingEmulator
      val emulatorBefore = emulator.copy(vRegister = emulator.vRegister.updated(0, 35))
      val emulatorAfter = Opcodes._4XKK(emulatorBefore, 0x4123)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 4)
    }
  }

  describe("_6XKK") {
    it("should put the value kk into register Vx and add 2 to program counter") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._6XKK(emulatorBefore, 0x6123)
      emulatorAfter.vRegister(0x1) should be (0x23)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }

    it("should put the value 0x19 into register v(0x0)") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._6XKK(emulatorBefore, 0x6019)
      emulatorAfter.vRegister(0x0) should be (0x19)
    }
  }

  describe("_7XKK") {
    it("should add the value kk to the value of register Vx, then store the result in Vx") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._7XKK(emulatorBefore, 0x7123)
      emulatorAfter.vRegister(0x1) should be (emulatorBefore.vRegister(0x1) + 0x23)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }

  describe("_8XY0") {
    it("should store the value of register Vy in register Vx") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._8XY0(emulatorBefore, 0x8120)
      emulatorAfter.vRegister(1) should be (emulatorBefore.vRegister(2))
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }

//  describe("_8XY1") {
//    it("should performs a bitwise OR on the values of Vx and Vy, then store the result in Vx. A bitwise OR compares the corresponding bits from two values, and if either bit is 1, then the same bit in the result is also 1. Otherwise, it is 0") {
//      val emulatorBefore = getTestingEmulator
//      val emulatorAfter = Opcodes._8XY1(emulatorBefore, 0x8121)
//      emulatorAfter.vRegister()
//    }
//  }

  describe("_9XY0") {
    it("should skip next instruction if Vx != Vy else the program counter is increased by 2") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._9XY0(emulatorBefore, 0x9120)
      if (emulatorBefore.vRegister(1) != emulatorBefore.vRegister(2)) {
        emulatorAfter.programCounter should be (emulatorBefore.programCounter + 4)
      } else {
        emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
      }
    }
  }

  describe("_ANNN") {
    it("should set the value of register I to nnn and add 2 to program counter") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._ANNN(emulatorBefore, 0xA123)
      emulatorAfter.iRegister should be (0x123)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }

  describe("_CXKK") {
    it("should generate a random number from 0 to 255, which is then ANDed with the value kk. The results are stored in Vx") {
      val emulatorBefore = getTestingEmulator
      val randomInt = scala.util.Random.nextInt(256)
      val emulatorAfter = Opcodes._CXKK(emulatorBefore, 0xC123, randomInt)
      emulatorAfter.vRegister(1) should be (randomInt & 0x23)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }

  describe("_EXA1") {
    it("should check the keyboard, and if the key corresponding to the value of Vx is currently in the up position, PC is increased by 2") {
      val emulatorBefore = getTestingEmulator
      val emulatorBeforeKeyUp = emulatorBefore.copy(keyInput = emulatorBefore.keyInput.updated(emulatorBefore.vRegister(1), 0))
      val emulatorAfterKeyUp = Opcodes._EXA1(emulatorBeforeKeyUp, 0xE121)
      val emulatorBeforeKeyDown = emulatorBefore.copy(keyInput = emulatorBefore.keyInput.updated(emulatorBefore.vRegister(1), 2))
      val emulatorAfterKeyDown = Opcodes._EXA1(emulatorBeforeKeyDown, 0xE121)
      emulatorAfterKeyUp.programCounter should be (emulatorBeforeKeyUp.programCounter + 4)
      emulatorAfterKeyDown.programCounter should be (emulatorBeforeKeyDown.programCounter + 2)
    }
  }

  describe("_EX9E") {
    it("should check the keyboard, and if the key corresponding to the value of Vx is currently in the down position, PC is increased by 2") {
      val emulatorBefore = getTestingEmulator
      val emulatorBeforeKeyUp = emulatorBefore.copy(keyInput = emulatorBefore.keyInput.updated(emulatorBefore.vRegister(1), 0))
      val emulatorAfterKeyUp = Opcodes._EX9E(emulatorBeforeKeyUp, 0xE121)
      val emulatorBeforeKeyDown = emulatorBefore.copy(keyInput = emulatorBefore.keyInput.updated(emulatorBefore.vRegister(1), 2))
      val emulatorAfterKeyDown = Opcodes._EX9E(emulatorBeforeKeyDown, 0xE121)
      emulatorAfterKeyDown.programCounter should be (emulatorBeforeKeyUp.programCounter + 4)
      emulatorAfterKeyUp.programCounter should be (emulatorBeforeKeyDown.programCounter + 2)
    }
  }

  describe("_FX07") {
    it("should place the value of DT into Vx") {
      val emulatorBefore = getTestingEmulator.copy(delayTimer = 10)
      val emulatorAfter = Opcodes._FX07(emulatorBefore, 0xf007)
      emulatorAfter.vRegister(0) should be (emulatorBefore.delayTimer)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }

  describe("_FX1E") {
    it("should add the values of I and Vx and the results are stored in the I register") {
      val emulator = getTestingEmulator
      val emulatorBefore = emulator.copy(vRegister = emulator.vRegister.updated(0, 10), iRegister = 5)
      val emulatorAfter = Opcodes._FX1E(emulatorBefore, 0xf01e)
      emulatorAfter.iRegister should be (emulatorBefore.iRegister + 10)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }

  describe("_FX15") {
    it("should set DT equal to the value of Vx") {
      val emulator = getTestingEmulator
      val emulatorBefore = emulator.copy(vRegister = emulator.vRegister.updated(0, 20), delayTimer = 15)
      val emulatorAfter = Opcodes._FX15(emulatorBefore, 0xf015)
      emulatorAfter.delayTimer should be (emulatorBefore.vRegister(0))
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }
}
