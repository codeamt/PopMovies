<h1 align="center"><a href="https://sites.google.com/udacity.com/gwgdevscholarship/home">#GrowWithGoogle</a> Android Development Project: <br>Popular Movies App</h1>


For the second project of the Grow With Google Developer Scholarship Program (and conjointly Udacity’s Android Development Nanodegree program), students were asked to build and design a movie discovery app (written in Java) from scratch. This app makes use of [TheMovieDB API](https://www.themoviedb.org/documentation/api) and the [YouTube Player API](https://developers.google.com/youtube/android/player/) for Android.


<p align="center">
  <img src="https://ucarecdn.com/b03069b7-1f46-439b-836b-c5fdacee911f/" >
</p>


The project was completed in two stages: 
- (1) fleshing out the UX and 
- (2) polishing the UI. 


## User Story Map
<p align="center">
	<img src="https://ucarecdn.com/54baaedc-f2ad-416a-86c1-7ec331018e4a/" />
</p>


## Learning Objectives

- <b>Thinking about UX</b>

This project hones in on several useful skills – implementing custom adapters (and fragment classes), configuring child menus, and launching intents to build out a User Experience that meets a host of needs for the end user’s session. Implementing the persistent search functionality was a great learning experience, as it led to more insight about using a custom filter class from within an adapter. 

<p align="center">
  <img src="https://ucarecdn.com/57121f70-3b3c-4374-b5fc-05f95c5a5093/">
</p>

<p align="center">
  <img src="https://ucarecdn.com/d1a18265-2b78-4102-b966-5d8c3602a99c/">
</p>

- <b>Network Services and Optimizing for Offline Mode</b>

I opted in for extending this project’s Content Provider to store movie data, making the app more efficient in various network scenarios and used custom cursors to aid grid filtering, as well. I also experimented with the Volley http library in order to speed up network requests. Last, I appended a sort order parameter to the movie endpoint (instead of querying several endpoints) to help keep the code more DRY.  

<p align="center">
  <img src="https://ucarecdn.com/6c262875-62e5-4898-8149-f0b377d0ebed/">
</p>


- <b>Design and Responsive UI</b>

Instead of making a landscape layout for this app, I implemented a helper function that takes into account device orientation and expands/contracts the number of grid columns accordingly. 

<p align="center">
  <img src="https://ucarecdn.com/54295f17-1055-422f-ba21-1122c2ea0c07/" width="70%" height="60%">
</p>

I also wanted to extend the user session time, and subsequently used the YouTube Player API to stream a trailer from within the app, itself, rather than launching an intent to the external YouTube service. To make the details page cleaner, I used a pager and tabs to segment general info and reviews. I designed collapsible card views for reviews that exceeded 5 lines.

- <b>Using Logger </b> 
This project illuminated just how invaluable logging your code can be. Much like console.log in JavaScript, using the logger for this project helped with debugging code at various points.

## Versioning and Dependencies 

<b>Target SDK Version:</b> 27

<b>Dependencies: </b>
-	com.android.support:appcompat-v7:27.1.1
-	com.android.support:design:27.1.1
-	com.android.support:cardview-v7:27.1.1
-	com.squareup.picasso:picasso:2.5.2
-	com.android.volley:volley:1.1.0
-	com.android.support.constraint:constraint-layout:1.1.0
-	com.android.support:support-v4:27.1.1
-	libs/YouTubeAndroidPlayerApi.jar

<b>App Repositories: </b>
-	MavenCentral

<b>Android Studio Version:</b> 3.1.2 (Stable Release)

<b>Gradle Build Tools Version:</b> 3.1.2

<b>Note:</b> To run this project on your local machine, you will have to add you your own MovieDB and YouTube Player API keys to a KeyStore.java file.
