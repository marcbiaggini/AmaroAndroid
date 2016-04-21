# Understanding Espresso Suite

The Espresso API encourages test authors to think in terms of what a user might do while interacting with the application - locating UI elements and interacting with them. At the same time, the framework prevents direct access to activities and views of the application because holding on to these objects and operating on them off the UI thread is a major source of test flakiness. Thus, you will not see methods like getView and getCurrentActivity in the Espresso API. You can still safely operate on views by implementing your own ViewActions and ViewAssertions.

Here’s an overview of the main components of Espresso:

* **Espresso** – Entry point to interactions with views (via onView and onData). Also exposes APIs that are not necessarily tied to any view (e.g. pressBack).

		/*This is a Entry point for register a esource use it for 		handle a callback Event*/
		Espresso.registerIdlingResources(idlingResource)

* **ViewMatchers** – A collection of objects that implement Matcher<? super View> interface. You can pass one or more of these to the onView method to locate a view within the current view hierarchy.

		/*This code verify if there is a View who has an spcific Text*/
		onView(withId(myViewId)).check(matches(withText("Some Text")));


* **ViewActions** – A collection of ViewActions that can be passed to the ViewInteraction.perform() method (for example, click()).
ViewAssertions – A collection of ViewAssertions that can be passed the ViewInteraction.check() method. Most of the time, you will use the matches assertion, which uses a View matcher to assert the state of the currently selected view. 

		/*This Code Perform a click action on a Specific View*/
		onView(withId(R.id.myVIew)).perform(click());


The image below shows all the diferents sets inside of Espresso suite.
 
![MacDown logo](https://google.github.io/android-testing-support-library/assets/espresso-cheat-sheet-2.1.0.png "This is all the components for Espresso")

Now that all the differents sets of componets has been introduced, is time to develop the first Test. In this Repository there is a project called **AutomationTest**, Please fork it in order to understand better this Tutorial.

[Next >](/docs/Tests/My_First_Test.md)