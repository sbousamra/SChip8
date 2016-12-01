package sbousamra.schip8

import org.scalatest.{FunSpec, Matchers}

class OpcodesSpec extends FunSpec with Matchers {

  def getTestingEmulator: Emulator = {
    Emulator(Memory.empty, 0, 0, List.fill(16)(0), List.fill(16)(0), 0, 0, 0, List.fill(16)(0), List.fill(32)(List.fill(64)(0)))
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

  describe("_ANNN") {
    it("should set the value of register I to nnn and add 2 to program counter") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._ANNN(emulatorBefore, 0xA123)
      emulatorAfter.iRegister should be (0x123)
      emulatorAfter.programCounter should be (emulatorBefore.programCounter + 2)
    }
  }



}
