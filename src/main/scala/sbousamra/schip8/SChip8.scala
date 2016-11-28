package sbousamra.schip8

object SChip8 {

  def main(args: Array[String]) {
    val emulator: Emulator = Emulator.createEmulator
    val loadedRom: Emulator = emulator.loadRom
    loadedRom.run
  }
}
