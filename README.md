### About

**_WeatherX_** is an Android application developed using Java programming language. Its main purpose is to show the weather forecast of the day based on location. Upon launch and granting location permission, the application will show the forecast of the device’s last known location. Also, users can type and search weather forecasts of any city. The weather data is accessed from the API provided by [__weatherapi.com__](https://www.weatherapi.com). The application the information like current temperature, weather status with feel and wind speed, and also provides information about sunrise, and sunset, and shows a 24-hour forecast. It has also a history section where users’ searched cities are saved in a list.

### A Group Project by

- __Ayub Ali Emon__, Student ID: 202001051911, Email: aaemon98@163.com
- __Phonesamay Phoutthavong__, Student ID: 202001061406, Email: phphonesamay@gmail.com

`Students of Software Engineering 2020(International Students)`

### Project Assigned by

- __Haixia Liu__

__`Shandong Univerity of Science and Technology`__

### Build Tools

- Android Studio Bumblebee | 2021.1.1 Patch 3
- Gradel Version 7.2
- Dependencies:
  
  ```gradle
  dependencies {

    implementation 'androidx.appcompat:appcompat:1.3.0'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'com.android.volley:volley:1.2.1'
    implementation 'com.squareup.picasso:picasso:2.8'
    implementation 'com.google.android.gms:play-services-location:21.0.1'
    implementation 'com.google.android.material:material:1.2.0'
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
  }
  ```

### Screenshots

<img src="https://github.com/alfa-echo-niner-ait/WeatherX/assets/78315132/8bf9f5d8-2492-41c1-8758-985034d24b7f"
  alt="Night view" width="350">

  **Night Time View**

<img src="https://github.com/alfa-echo-niner-ait/WeatherX/assets/78315132/d3506b16-b7de-48d2-91f2-6f63b2b6a905"
alt="Day view" width="350">

**Day Time View**

<img src="https://github.com/alfa-echo-niner-ait/WeatherX/assets/78315132/266e90e6-5a20-44eb-bd99-847932d85325"
alt="History view" width="350">

**Location History View**
