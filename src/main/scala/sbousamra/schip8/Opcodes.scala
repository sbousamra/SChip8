package sbousamra.schip8

object Opcodes {

  def _00E0(emulator: Emulator, rawOpcode: Int): Emulator = {
    val resetDisplay = List.fill(32)(List.fill(64)(0))
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(screen = resetDisplay, programCounter = newProgramCounter)
    newEmulator
  }

  def _00EE(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newProgramCounter = emulator.stack(emulator.stackPointer) + 2
    val newStackPointer = emulator.stackPointer - 1
    val newEmulator = emulator.copy(stackPointer = newStackPointer, programCounter = newProgramCounter)
    newEmulator
  }

  def _1NNN(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newProgramCounter = (rawOpcode & 0x0fff)
    val newEmulator = emulator.copy(programCounter = newProgramCounter)
    newEmulator
  }

  def _2NNN(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newStackPointer = emulator.stackPointer + 1
    val newStack = emulator.stack.updated(newStackPointer, emulator.programCounter)
    val newProgramCounter = (rawOpcode & 0x0fff)
    val newEmulator = emulator.copy(stackPointer = newStackPointer, stack = newStack, programCounter = newProgramCounter)
    newEmulator
  }

  def _6XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val target = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(target, (rawOpcode & 0x00ff))
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    newEmulator
  }

  def _7XKK(emulator: Emulator, rawOpcode: Int): Emulator = {
    val target = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(target, ((emulator.vRegister(target) + (rawOpcode & 0x00ff)) & 0xff))
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    newEmulator
  }

  def _ANNN(emulator: Emulator, rawOpcode: Int): Emulator = {
    val newiRegister = (rawOpcode & 0x0fff)
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(iRegister = newiRegister, programCounter = newProgramCounter)
    newEmulator
  }
}
