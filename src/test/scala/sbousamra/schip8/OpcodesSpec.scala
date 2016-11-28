package sbousamra.schip8

import org.scalatest.{FunSpec, Matchers}

class OpcodesSpec extends FunSpec with Matchers {

  def getTestingEmulator: Emulator = {
    Emulator(Memory.empty, 0, 0, List.fill(16)(0), List.fill(16)(0), 0, 0, 0, List.fill(16)(0))
  }

  describe("_1NNN") {
    it("should set the program counter to NNN") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._1NNN(emulatorBefore, 0x1123)
      emulatorAfter.programCounter should be (0x123)
    }
  }

  describe("_6XKK") {
    it("should put the value kk into register Vx and add 2 to program counter") {
      val emulatorBefore = getTestingEmulator
      val emulatorAfter = Opcodes._6XKK(emulatorBefore, 0x6123)
      emulatorAfter.vRegister(0x1) should be (0x23)
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
