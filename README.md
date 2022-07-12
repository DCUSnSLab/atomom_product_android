# atomom_product_android

## Overview
This repository contains the code for Android Application. 

The application takes an image of the product, selects the product name as a drag, sends it to the server, and proceeds with text detection and recognition and receives the result.




## Getting Started
### Dependency
- This work was tested with Android Studio Bumblebee 2021.1.1.22, OpenCV 4.5.5 android sdk, AVD(Pixel 2, Android 11.0, Rlease Name : R, API Level : 30, ABI x86), Windows 10

- Download Android Studio and OpenCV
 
 | *Name* |*Link* |
|    :-:      |     :-:      |
|   Android Studio      |    [Click](https://drive.google.com/file/d/1yZWDJ_DtFArBI0zzphUcHvnn5EcDhXhA/view?usp=sharing)      |
OpenCV | [Click](https://drive.google.com/file/d/1-vAaR1IkPoVzcJ95ZQlAbs-cIzqLTAmd/view?usp=sharing)|

**This code has OpenCV built, so you do not need to install OpenCV.**


* Follow the 4 steps below to get started.
``` 
1. Download Android Studio
2. Input your server ip and port in ./app/src/main/java/com/example/roicamera/RoiActivity.java(line 95).
3. Create Virtual Device or use your android device.
4. Run 'app'
```


### Sample Results
| *test1* | *test2* | *test3* |
| :-:         |     :-:      |     :-:      |
|   <img src="https://user-images.githubusercontent.com/96579745/178452979-0ce2666f-3f52-43d1-a4e1-36faccd18867.gif" width="1000" alt="teaser">   |   <img src="https://user-images.githubusercontent.com/96579745/178453017-6c305d94-8bcf-4ea2-be11-0dc7dce88158.gif" width="1000" alt="teaser">   |   <img src="https://user-images.githubusercontent.com/96579745/178453029-dc9f3291-e21b-4f59-a99d-b72d0f8964cf.gif" width="1000" alt="teaser">   |
