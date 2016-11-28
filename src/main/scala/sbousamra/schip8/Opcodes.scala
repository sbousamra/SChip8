package sbousamra.schip8

object Opcodes {
  def _1NNN(emulator: Emulator, rawOpcode: Int) = {
    val newProgramCounter = (rawOpcode & 0x0fff)
    val newEmulator = emulator.copy(programCounter = newProgramCounter)
    newEmulator
  }

  def _6XKK(emulator: Emulator, rawOpcode: Int) = {
    val target = (rawOpcode & 0x0f00) >> 8
    val newvRegister = emulator.vRegister.updated(target, (rawOpcode & 0x00ff))
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(vRegister = newvRegister, programCounter = newProgramCounter)
    newEmulator
  }

  def _ANNN(emulator: Emulator, rawOpcode: Int) = {
    val newiRegister = (rawOpcode & 0x0fff)
    val newProgramCounter = emulator.programCounter + 2
    val newEmulator = emulator.copy(iRegister = newiRegister, programCounter = newProgramCounter)
    newEmulator
  }
}
