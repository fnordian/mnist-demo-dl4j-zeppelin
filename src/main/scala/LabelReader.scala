object LabelReader {

  import java.io.{FileInputStream, InputStream}
  import java.util.zip.GZIPInputStream

  import org.nd4j.linalg.api.ndarray.INDArray
  import org.nd4j.linalg.factory.Nd4j


  private def oneHotEncode(n: Int, a: Int): INDArray = {
    val result = Nd4j.zeros(1, n)
    result.put(0, a, 1.0)
    result
  }

  private def getInputStream(filename: String) =
    new GZIPInputStream(new FileInputStream(filename))

  private def read32BitToInt(inputStream: InputStream) = {
    var result: Int = 0

    for (i <- 0 to 3) {
      result = result << 8
      result |= 0xff & inputStream.read()
    }

    result
  }

  private def readHeader(inputStream: InputStream) = {
    val magicNumber = read32BitToInt(inputStream)
    val numberOfItems = read32BitToInt(inputStream)

    (magicNumber, numberOfItems)
  }

  def readtoArray(filename: String, classCount: Int, userMaxItems: Int = 0): INDArray = {
    val in = getInputStream(filename)

    val header = readHeader(in)
    val maxItems = if (userMaxItems == 0 || userMaxItems > header._2) header._2 else userMaxItems

    val labelArray = Nd4j.create(maxItems, classCount)
    var i = 0

    val labels = for (b <- Iterator.continually(in.read()).takeWhile(i < maxItems - 1 && -1 != _)) {
      val oh = oneHotEncode(classCount, b)
      for (j <- 0 to classCount - 1) {
        labelArray.put(i, j, oh.getScalar(0, j))
      }
      i = i + 1
    }

    labelArray
  }
}