package SChip8

import java.io.File
import scala.io.{Codec, Source}

case class Memory(data: List[Byte])

object Memory {

  val availableMemory = List.fill(0, 4096)(0)

  def loadRom(romPath: String): List[Byte] = {
    val pathToFile = new File(romPath)
    val romSource = Source.fromFile(pathToFile).toList.map(_.toByte)
    val romData = availableMemory
    romSource
  }
}
