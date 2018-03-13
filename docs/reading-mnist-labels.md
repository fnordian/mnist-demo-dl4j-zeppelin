Reading MNIST labels
====================

The label file format is similar to the images. There is a small header followed by the image labels (magicnumber + item count in msb encoding). 
Every label is exactly one byte. The labels are in the same order as the images.

    Header:  -------------------------
     (magic) | 00 01 02 03           |
     (imgcnt)| 00 01 02 03           |
             -------------------------
    Label0:  -------------------------
             |00                     |
             -------------------------
    Label1:  -------------------------
             |00                     |
             -------------------------


As with images, the labels are also stored in an ```INDArray```.

But unlike the images which are stored directly into to array, labels have to reencoded using the OneHot encoding schema. 
This is required by the ml algorithm we are going to use.