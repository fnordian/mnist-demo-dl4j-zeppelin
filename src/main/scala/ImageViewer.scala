object ImageViewer {

  import java.awt.image.{BufferedImage, _}
  import java.io.IOException

  import javax.swing.{ImageIcon, JLabel, JOptionPane}
  import org.nd4j.linalg.api.ndarray.INDArray

  def view(image: INDArray): Unit = {
    try {
      val byteimage = image.stride().map(_.toByte)
      val bufferImage = new DataBufferByte(byteimage, 28 * 28)
      val bi = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY)

      for (y <- 0 to 27) {
        for (x <- 0 to 27) {
          val c = (256 * image.getDouble(0, x + 28 * y)).toInt & 255
          val rgb = (255 << 24) + (c << 16) + (c << 8) + c
          bi.setRGB(x, y, rgb)
          print(" " + (if (c > 20) "X" else "_"))
        }
        println()
      }

      val icon = new ImageIcon(bi)
      val label = new JLabel(icon)
      // uncomment this to view image
      //JOptionPane.showMessageDialog(null, label)
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }
}
