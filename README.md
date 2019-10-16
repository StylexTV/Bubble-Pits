# Bubble-Pits
Just a silly project I am working on that produces random images. I call them "Bubble Pits".

The code in this github repository lets you create a video file ("output.mp4") with n frames at 60 fps.
With a random seed from 0 to 9999 a random animation of these interesting shapes is shown.
Here is a screenshot of the first frame of a video with the seed 10.

![alt text](https://raw.githubusercontent.com/StylexTV/Bubble-Pits/master/image.png)

The algorithm works by applying this formula to each pixel in a frame (each channel has it's own x offset):
    brightness = 1 - abs( simplex(pixelX,pixelY,frameIndex) ) * 255

Then it will apply a "3D-effect" onto the rendered frame by offsetting the r, g and b channel again:
    r(x -25)
    g(x +25)
    b(x +25)
    
A rendered frame is send to ffmpeg via a pipeline and after all frames are sent ffmpeg creates the .mp4 file and the process exits.
