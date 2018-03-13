Image classifier using a small neural network
=============================================

We use deeplearning4j to build an image classification network and feed it the mnist train-dataset.

The neural net has an inputlayer of size 28x28, 2 hidden, relu-activated layers with 256 and 128 nodes and a softmax output layer with 10 classes. 

After training, the network is then evaluated using the mnist-test-dataset.

To further optimize the classifier performance, you can play around with the network configuration and hyper-parameters.

Have a look at the MNIST-homepage for some benchmarks, to see how you are doing.

Good luck!