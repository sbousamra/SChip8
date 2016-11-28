package sbousamra.schip8

object SChip8 {

  def main(args: Array[String]) {
    val loadedRom: Emulator = Emulator.loadRom
    loadedRom.run
  }
}
