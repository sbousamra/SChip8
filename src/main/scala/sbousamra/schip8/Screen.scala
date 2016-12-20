package sbousamra.schip8

case class Screen(width: Int, height: Int, scalingFactor: Int, data: Array[Array[Boolean]]) {

  def getCoordinate(xCoordinate: Int, yCoordinate: Int): Boolean = {
    data(yCoordinate)(xCoordinate)
  }

  def setCoordinate(xCoordinate: Int, yCoordinate: Int, pixel: Boolean): Array[Boolean] = {
    data(yCoordinate).updated(xCoordinate, pixel)
  }
}
object Screen {

  def emptyScreen: Screen = {
    val emptyData = Array.fill(32)(Array.fill(64)(false))
    Screen(64, 32, 20, emptyData)
  }

  def drawImage(xCoordinate: Int, yCoordinate: Int, rawopcode: Int): Screen = {
    ???
  }
}
