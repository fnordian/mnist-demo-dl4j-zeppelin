object ImageReader {

  import java.io.{FileInputStream, InputStream}
  import java.util.zip.GZIPInputStream

  import org.nd4j.linalg.api.ndarray.INDArray
  import org.nd4j.linalg.factory.Nd4j


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
    val numberOfRows = read32BitToInt(inputStream)
    val numberOfColums = read32BitToInt(inputStream)

    (magicNumber, numberOfItems, numberOfRows, numberOfColums)
  }


  private def scale(v: Double) = v / 256

  def readtoArray(filename: String, userMaxItems: Int = 0): INDArray = {
    val in = getInputStream(filename)

    val header = readHeader(in)
    val rowSize = header._3 * header._4
    val maxItems = if (userMaxItems == 0 || userMaxItems > header._2) header._2 else userMaxItems

    val imageArray = Nd4j.create(maxItems, rowSize)
    var (i, j) = (0, 0)

    for (b <- Iterator.continually(in.read()).takeWhile(i < maxItems - 1 && -1 != _)) {
      j = j + 1
      if (j % rowSize == 0) {
        j = 0
        i = i + 1
      }
      imageArray.put(i, j, scale(b.toDouble))
    }

    imageArray
  }
}
