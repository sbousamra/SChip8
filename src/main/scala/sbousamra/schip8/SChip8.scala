package sbousamra.schip8

object SChip8 {

  def main(args: Array[String]) {
    val loadedRom: Emulator = Emulator.loadRom("C:\\Users\\Administrator\\Desktop\\Programming\\SChip8\\src\\main\\resources\\roms\\TETRIS")
    loadedRom.run
  }
}
