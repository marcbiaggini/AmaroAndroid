#Seting the Environment
In order to run a tests written with espresso is necessary to create a test configuration in Android Studio, to test on hundreds of real Android devices:

* **Run Setup:**  In Android Studio. Open Run -> Edit Configurations. Click ‘+’ button on the left-top corner to add new configuration as Android Tests. Give a name to test configuration (here ‘New Test Config’), select module (here ‘app’) and specify the instrumentation runner (here ‘android.support.test.runner.AndroidJUnitRunner’). Your open Run/Debug Configuration dialog should look this this:


![MacDown logo](https://bytebucket.org/marcbiaggini/espressodocumentation/raw/8dfe944618783e12ebde530f4ef82516fe78780b/Images/EspressoRunSetup.png?token=e2b341d454181f90c61de367c382b2fc1f8e4812 "Runner Test Setup")

* **Recall:** You could setup the run configurations to test an entire module, a package a class or a specific method in Test option.

#Espresso Basics
In order to understand all the issues related with Espresso this Slides provide a pretty clear explaination of the basics to start to use espresso.

[Download Espresso basics](/Espresso_Basics.pptx?at=master)

#Espresso’s Test “Lifecycle”
This tutorial will show how it works Espresso life cycle and how to setup the test before an Activity has been started.

[Espresso Test Lifecycle](https://jabknowsnothing.wordpress.com/2015/11/05/activitytestrule-espressos-test-lifecycle/)

#Idiling Resource
This tutorial shows how to setup IdilingResource to handle Asyncrhonous events.

[Idiling Resources](http://blog.sqisland.com/2015/04/espresso-custom-idling-resource.html)

#Counting Idiling Resource
This site has and introduction on Counting Idling resource

[Counting Idling Resource](http://qathread.blogspot.com.br/2014/06/discovering-espresso-for-android.html)

#Tutorial My First Test
This tutorial will show how to do a complete Test using expresso

[My First Test](http://www.vogella.com/tutorials/AndroidTestingEspresso/article.html#asynctasktesting_espresso)

