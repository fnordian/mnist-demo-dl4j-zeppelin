object HttpDownloader {

  import java.io.File
  import java.io.{BufferedOutputStream, FileOutputStream, InputStream, OutputStream}
  import java.net.{HttpURLConnection, URL}

  def fileExists(filename: String): Boolean = {
    new File(filename).exists()
  }

  def downloadFile(url: String, filename: String): String = {
    if (!fileExists(filename)) {
      val connection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
      connection.setRequestMethod("GET")
      val in: InputStream = connection.getInputStream
      val fileToDownloadAs = new java.io.File(filename)
      fileToDownloadAs.getParentFile().mkdirs()
      val out: OutputStream = new BufferedOutputStream(new FileOutputStream(fileToDownloadAs))
      val byteArray = Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
      out.write(byteArray)

      out.close()
      in.close()
    }

    filename
  }
}
