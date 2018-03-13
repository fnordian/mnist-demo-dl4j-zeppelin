Reading MNIST images
====================

MNIST images are stored as a set in one gzipped file. After a small header (magicnumber + item, row and col count in msb encoding)
, all images are concatenated until the of the file.
Every image consists of 28x28 bytes, every byte describing one pixel of the image, ordered line-wise from left to right, top to bottom.

    Header:  -------------------------
     (magic) | 00 01 02 03           |
     (imgcnt)| 00 01 02 03           |
     (rowcnt)| 00 01 02 03           |
     (colcnt)| 00 01 02 03           |
             -------------------------
    Image0:  -------------------------
     (pixels)|00 01 02 04...         |
             |28 29 30 31...         |
             |...                    |
             |...                    |
             -------------------------
    Image1:  -------------------------
     (pixels)|00 01 02 04...         |
             |28 29 30 31...         |
             |...                    |
             |...                    |
             -------------------------

All images are stored in one big data object of type ```INDArray```. ```INDArray``` is a multidimensional array,
allowing highly optimized matrix operations. Deeplearning4j performs ml-algorithms on these data structures.

The dimensions, or shape of the image-array is (number of images, 28x28). Every row of the array contains all the pixels of one image.