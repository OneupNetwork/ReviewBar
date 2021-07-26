# ReviewBar
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/tw.com.oneup.www/reviewbar/badge.svg)](https://search.maven.org/artifact/tw.com.oneup.www/Baha-UrlPreview)

The library is like  [RatingBar](https://developer.android.com/reference/android/widget/RatingBar) , but ReviewBar can use custom icon , icon spacing ...

Inspire by [saran2020-DragRating](https://github.com/saran2020/DragRating)!

<p align="center">
  <img src="https://github.com/OneupNetwork/ReviewBar/blob/main/img/demo.png" width="350">
</p>

# How do I use ReviewBar

### Setup
```groovy
//Top-level build.gradle
buildscript {
  ...
    repositories {
      mavenCentral()
      ...
    }
}

//project build.gradle
dependencies {
    ...
    implementation 'tw.com.oneup.www:reviewbar:1.0.2'
}
```

### Functions

```xml
<com.baha.reviewbar.ReviewBar
        ...
        when draging min 
        app:whenDragScoreMin="1.0"
        current score                      
        app:reviewScore="3.1"
        more lhan 0.7 fill icon , less than 0.8 half icon                      
        app:halfRangeMax="0.7"
        more lhan 0.1 half icon , less than 0.2 empty icon                      
        app:halfRangeMin="0.2"
        show half icon                      
        app:halfEnable="true"
        drag enable
        app:isIndicator="true"
        score max , icon total count
        app:reviewScoreMax="5"
        icon with icon spacing                      
        app:iconSpace="5"
        icon res                      
        app:halfIcon="@drawable/xxx"
        app:emptyIcon="@drawable/xxx"
        app:fillIcon="@drawable/xxx" />
```
```Kotlin
reviewbar.setReviewIcon(fillIconRes: Int, halfIconRes: Int, emptyIconRes: Int)
reviewbar.setReviewScore(score:Float)
reviewbar.setReviewScoreMax(max: Int)
reviewbar.setIndicatorEnable(enable: Boolean)
reviewbar.setHalfEnable(enable: Boolean)
reviewbar.setHalfMinMax(min: Float, max: Float)
reviewbar.setWhenDragScoreMin(min: Float)
reviewbar.getReviewScore()
```
### Requirements
    Android 5.+ (API 21)

### Latest version
    - v1.0.2 (Jun 30 , 2021)
    
### Developed By
    OneupNetwork-SolinariWu     
    
## License
    MIT License

    Copyright (c) 2021 OneupNetwork

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.    
