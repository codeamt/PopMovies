<h1 align="center"><a href="https://sites.google.com/udacity.com/gwgdevscholarship/home">#GrowWithGoogle</a> Android Development Project: <br>PopMovies App</h1>


For the second (and third) project of the Grow With Google Developer Scholarship Program (and conjointly Udacity’s Android Development Nanodegree program), students were asked to build and design a movie discovery app (written in Java) from scratch. This app makes use of [TheMovieDB API](https://www.themoviedb.org/documentation/api) and the [YouTube Player API](https://developers.google.com/youtube/android/player/) for Android.


<p align="center">
  <img src="https://drive.google.com/file/d/1m2Pa5AfsCjZb98Cn74GTawNN4CL5z6iV" >
</p>


The project was completed in two stages: 

- (1) fleshing out the UX and 
- (2) polishing the UI. 


## User Story Map
<p align="center">
<img src="https://drive.google.com/file/d/1u489e1LPeroIacTPCRJEM6Xo6tTCbYMx" />
</p>


## Learning Objectives

<b>Thinking about UX</b>

This project hones in on several useful skills – implementing custom adapters (and fragment classes), configuring child menus, and launching intents to build out a User Experience that meets a host of needs for the end user’s session. 

<p align="center">
<img src="https://drive.google.com/file/d/1nr4jYVa-iCqh-bLNOuN4yFp1R5KhGcnZ" />
</p>


Implementing the persistent search functionality was a great learning experience, as it led to more insight about the logistics of filtering a RecyclerView from within an adapter. 

<p align="center">
  <img src="https://drive.google.com/file/d/11dK0spZlS508BrtAiSxX_e8GV0s3CTq2">
</p>


<b>Network Services and Optimizing for Offline Mode</b>

- I opted in for extending this project’s Content Provider to persist favorite movie data to the app's SQL database and making these titles available offline. I also experimented with the Volley http library in order to speed up network requests.

<p align="center">
<img src="https://drive.google.com/file/d/1G9rwWXucwy8ZRCTRk8VSZbJuYOOC3ZcI" />
</p>


<b>Design and Responsive UI</b>

- Instead of making a landscape layout for this app, I implemented a helper function that takes into account device orientation and expands/contracts the number of grid columns accordingly.
- I also wanted to extend the user session time, and subsequently used the YouTube Player API to stream a trailer from within the app, itself, rather than launching an intent to the external YouTube service. To make the details page cleaner, I used a pager and tabs to segment general info, the primary movie trailer and RecyclerView list of reviews. I designed collapsible card views for reviews that exceeded 5 lines.



<b>Using Logger </b> 

This project illuminated just how invaluable logging your code can be. Much like console.log in JavaScript, using the logger for this project helped with debugging code at various points.

## Versioning and Dependencies 

<b>Target SDK Version:</b> 27

<b>Dependencies: </b>
-	com.android.support:appcompat-v7:27.1.1
- 	com.android.support:recyclerview-v7:27.1.1
-	com.android.support:design:27.1.1
-	com.android.support:cardview-v7:27.1.1
-	com.squareup.picasso:picasso:2.5.2
-	com.uncopt:android.justified:1.0
-	com.android.volley:volley:1.1.0
-	com.android.support.constraint:constraint-layout:1.1.0
-	com.android.support:support-v4:27.1.1
-	libs/YouTubeAndroidPlayerApi.jar

<b>App Repositories: </b>
-	MavenCentral

<b>Android Studio Version:</b> 3.1.2 (Stable Release)

<b>Gradle Build Tools Version:</b> 3.1.2

<b>Note:</b> To run this project on your local machine, you will have to add you your own MovieDB and YouTube Player API keys to a KeyStore.java file.

## References 

Several tutorials inspired this project: 

-	https://codeburst.io/android-swipe-menu-with-recyclerview-8f28a235ff28
-	https://guides.codepath.com/android/fragment-navigation-drawer
-	https://panavtec.me/retain-restore-recycler-view-scroll-position
-	https://gist.github.com/takeshiyako2/e776bbaf2966c6501c4f
-	https://android.jlelse.eu/font-awesome-a-better-way-to-display-symbols-and-icons-in-android-d694e5ee621f
-	http://saulmm.github.io/mastering-coordinator
- https://stackoverflow.com/questions/43937210/searchview-after-rotation-button-goes-to-the-3-dots
