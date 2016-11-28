package sbousamra.schip8

import java.io.File

import scala.io.{Codec, Source}

case class Memory(data: List[Int])

object Memory {

  val availableMemory: List[Int] = List.fill(4096)(0)

  def loadRomIntoMemory(romPath: String): Memory = {
    val pathToFile: File = new File(romPath)
    val romToBytes: List[Int] = Source.fromFile(pathToFile)(Codec.ISO8859).toList.map(x => (x.toByte) & 0xff)
    val loadBytesToMemory: List[Int] = availableMemory.patch(0x200, romToBytes, romToBytes.length)
    Memory(loadBytesToMemory)
  }

  def empty: Memory = {
    Memory(List.empty[Int])
  }
}
