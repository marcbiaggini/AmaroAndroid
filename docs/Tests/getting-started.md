#UITest for Android Using Espresso

##Introduction

User interface (UI) testing lets you ensure that your app meets its functional requirements and achieves a high standard of quality such that it is more likely to be successfully adopted by users. One approach to UI testing is to simply have a human tester perform a set of user operations on the target app and verify that it is behaving correctly.

##UITest For Android
Android (including iOS) developers have had unlimited access to some advanced cloud-based solution, like Testdroid Cloud, to run automated tests on a large scale of real devices for quality assurance. Also the emergence of different Android testing frameworks has substantially eased Android developers’ lives.

UITest allows the developer to  run on actual device or emulator (they are instrumentation based tests) and behave as if an actual user is using the app (i.e. if a particular view is off screen, the test won't be able to interact with it).

There is severals numbers of extensible API, for automatic synchronization of test actions with the UI of the app under test, and rich failure information there is some frameworks pretty popular in UITest segment:

* Robotium: This frameWork was once the most widely used Android testing framework in the early days of Android world. With a similarity with Selenium in Android, it makes testing API simpler. Robotium is an open source library extending JUnit with plenty of useful methods for Android UI testing. It provides powerful and robust automatic black-box test cases for Android apps (native and hybrid) and web testing. With Robotium you can write function, system and acceptance test scenarios, and test applications where the source code is available.

* UIAutomator: This Framework allows you to do more in testing Android apps and games. Google’s test framework allows to test user interface (UI) of a native Android app on one or more devices. Another advantage of uiautomator is that it runs JUnit test cases with special privileges, which means test cases can span across different processes.

* Espresso: This framwwork is the latest Android test automation framework that got open-sourced by Google, making it available for developers and testers to hammer out their UIs. Espresso has an API that is small, predictable, easy to learn and built on top of the Android instrumentation framework. You can quickly write concise and reliable Android UI tests with it. It is supported on API level 8 (Froyo), 10 (Gingerbread), and 15 (Ice Cream Sandwich) and afterwards. Espresso is quite reliable, synchronizing with the UI thread and fast because there is no need for any sleeps (tests run on same millisecond when an app becomes idle). But it does not have support for webviews as well.

##Working With Espresso

This part covers installing Espresso using the SDK Manager and building it using Gradle. Android Studio is recommended.

First of all On your device, under Settings->Developer options disable the following 3 settings:

* Window animation scale: 

 [![IMAGE ALT TEXT](http://img.youtube.com/vi/qH6Bf1pNu4I/0.jpg)](https://www.youtube.com/watch?v=qH6Bf1pNu4I)

* Transition animation scale:
 
 [![IMAGE ALT TEXT](http://img.youtube.com/vi/e-YhVJZUsTE/0.jpg)](https://www.youtube.com/watch?v=e-YhVJZUsTE)

* Animator duration scale: 

 [![IMAGE ALT TEXT](http://img.youtube.com/vi/iaOEQeF_vbg/0.jpg)](https://www.youtube.com/watch?v=iaOEQeF_vbg)

####Setting Espresso

1.) Set the instrumentation runner. Add to the same build.gradle file the following line in: 

    testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"


![alt text](  https://bytebucket.org/marcbiaggini/espressodocumentation/raw/3c98d24ab068546bc7d5a9b609d7de1bf4561722/Images/Espresso%20Setup1.png?token=b8aa808f81fbd6e78542927c6d45bd6200aa4541
)

2.) Set the testing dependecies. Add to the same build.gradle file the following line in:

 	androidTestCompile 'com.android.support.test.espresso:espresso-core:2.2.1'
    androidTestCompile 'com.android.support.test:runner:0.4.1'
    androidTestCompile 'com.android.support:support-annotations:23.2.1'
    
3.) Set the idlingResources. Add to the same build.gradle file the following line in:

	// IdlingResource is used in the app under test
    androidTestCompile 'com.android.support.test.espresso:espresso-idling-resource:2.2.1'
    // For CountingIdlingResource:
    androidTestCompile 'com.android.support.test.espresso:espresso-contrib:2.2.1'

![alt text](  https://bytebucket.org/marcbiaggini/espressodocumentation/raw/3c98d24ab068546bc7d5a9b609d7de1bf4561722/Images/EspressoSetup2.png?token=fb5715b66d94511f220437a884c1da9f16f984fc
)



[Next >](docs/Tests/Understanding_Espresso_Suite.md)