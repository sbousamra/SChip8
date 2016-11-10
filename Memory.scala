package SChip8

import java.io.File
import scala.io.{Codec, Source}

case class Memory(data: List[Byte])

object Memory {

  val availableMemory: List[Byte] = List.fill(4096)(0.toByte)

  def loadRom(romPath: String): List[Byte] = {
    val pathToFile = new File(romPath)
    val romToBytes = Source.fromFile(pathToFile)(Codec.ISO8859).toList.map(_.toByte)
    val loadBytesToMemory = availableMemory.patch(0x200, romToBytes, romToBytes.length)
    loadBytesToMemory
  }
}
