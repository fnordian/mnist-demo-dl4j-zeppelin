object MnistDemo {

  import org.deeplearning4j.nn.api.OptimizationAlgorithm
  import org.deeplearning4j.nn.conf.NeuralNetConfiguration
  import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
  import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
  import org.deeplearning4j.nn.weights.WeightInit
  import org.nd4j.linalg.activations.Activation
  import org.nd4j.linalg.api.ndarray.INDArray
  import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction


  def reverseOneHot(row: INDArray, classCount: Int): Int = {
    var result = -1
    var maxValue = -1.0

    for (i <- 0 to classCount -1) {
      val rowValue = row.getDouble(i)
      if (rowValue > maxValue) {
        maxValue = rowValue
        result = i
      }
    }

    result
  }

  def test(net: MultiLayerNetwork): Unit = {
    val testImages = ImageReader.readtoArray(HttpDownloader.downloadFile("http://yann.lecun.com/exdb/mnist/t10k-images-idx3-ubyte.gz", "data/mnist/t10k-images-idx3-ubyte.gz"))
    val testLabels = LabelReader.readtoArray(HttpDownloader.downloadFile("http://yann.lecun.com/exdb/mnist/t10k-labels-idx1-ubyte.gz", "data/mnist/t10k-labels-idx1-ubyte.gz"), 10)

    val predictions = net.output(testImages)
    var correctCount: Int = 0

    for (i <- 0 to (predictions.shape()(0)) - 1) {
      if (reverseOneHot(predictions.getRow(i), 10) == reverseOneHot(testLabels.getRow(i), 10)) {
        correctCount = correctCount + 1
      }
    }

    println("correct: " + correctCount)
    println("exact match ratio: " + (correctCount.toDouble / predictions.shape()(0).toDouble))
  }

  def viewExample(itemIdx: Int, net: MultiLayerNetwork): Unit = {
    ImageViewer.view(net.getInput.getRow(itemIdx))

    val exampleLabel = net.getLabels.getRow(itemIdx)
    for (i <- 0 to exampleLabel.shape()(1) -1) {
      print(exampleLabel.getDouble(0, i).toByte)
    }
    println()
  }

  def main(args: Array[String]): Unit = {
    val hl1Size = 256
    val hl2Size = 128

    val confBuilder = new NeuralNetConfiguration.Builder()
      .iterations(1)
      .learningRate(0.1)
      .weightInit(WeightInit.XAVIER)
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .list()
      .layer(0, new DenseLayer.Builder().activation(Activation.RELU).nIn(28 * 28).nOut(hl1Size).build())
      .layer(1, new DenseLayer.Builder().activation(Activation.RELU).nIn(hl1Size).nOut(hl2Size).build())
      .layer(2, new OutputLayer.Builder().activation(Activation.SOFTMAX).nIn(hl2Size).nOut(10).lossFunction(LossFunction.MCXENT).build())

    val conf = confBuilder.pretrain(false).backprop(true).build()
    val net = new MultiLayerNetwork(conf)
    net.init()

    val maxItems = 0

    net.setInput(ImageReader.readtoArray(HttpDownloader.downloadFile("http://yann.lecun.com/exdb/mnist/train-images-idx3-ubyte.gz", "data/mnist/train-images-idx3-ubyte.gz"), maxItems))
    net.setLabels(LabelReader.readtoArray(HttpDownloader.downloadFile("http://yann.lecun.com/exdb/mnist/train-labels-idx1-ubyte.gz", "data/mnist/train-labels-idx1-ubyte.gz"), 10, maxItems))
    viewExample(123, net)

    net.setInputMiniBatchSize(10)
    val numberOfEpochs = 10
    for (epoch <- 1 to numberOfEpochs) {
      net.fit()
    }

    test(net)
  }
}
