package SChip8

case class Emulator(memory: Memory)

object Emulator {

  def main(args: Array[String]) {
    val rom = Memory.loadRom("C:\\Users\\Administrator\\Desktop\\Programming\\SChip8\\TETRIS")
    println(rom)
  }

//  def opcodeToInstruction(opcode: Int): Instruction = {
//      ???
//    }
//
//  def executeOpcode(memory: Memory): Emulator = {
//      ???
//    }
//
//  def mainEmulationLoop: Boolean = {
//      ???
//  }
}
