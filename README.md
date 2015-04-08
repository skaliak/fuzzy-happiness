[Monster Spotter Android App](https://github.com/skaliak/fuzzy-happiness)
=============================

 - This was inspired by my 2-year-old daughter's preoccupation with "monsters" of the cute type (think Monster's Inc)
 - The main function of the app is to document sightings of monsters
   - each monster has a name and optionally a description and an image url
   - sightings are viewed/edited using embedded Google Maps
 - The REST API I made with Google App Engine is used to store the data
   - I used [Retrofit](http://square.github.io/retrofit/) to create the client code
 - Users must choose a Google account on their device to authenticate with the back-end
   - Authenticaticating the selected account to the back-end is done with oAuth
 
This was a final project for a Mobile/Cloud development class.  I had already created the back-end, but I had less than 2 weeks to:
 - Refresh my Java skills, which were a little rusty
 - Learn the basics of Android development
 - Learn how to use the Google Maps Android API
 - Learn Retrofit

And the hardest part, which was getting oAuth to work.  The oAuth API built-in to Google App Engine was intended to be used in the context of a webapp, where things like cookies and browser redirects would automatically work.  Adapting this to Android meant (in a nutshell), the client has to behave like the browser would for part of the oAuth transaction.

Screenshots
===========

![screenshot](https://github.com/skaliak/fuzzy-happiness/blob/master/screenshots/1.png?raw=true)
![screenshot](https://github.com/skaliak/fuzzy-happiness/blob/master/screenshots/2.png?raw=true)
![screenshot](https://github.com/skaliak/fuzzy-happiness/blob/master/screenshots/3.png?raw=true)
![screenshot](https://github.com/skaliak/fuzzy-happiness/blob/master/screenshots/4.png?raw=true)
![screenshot](https://github.com/skaliak/fuzzy-happiness/blob/master/screenshots/5.png?raw=true)
![screenshot](https://github.com/skaliak/fuzzy-happiness/blob/master/screenshots/6.png?raw=true)
![screenshot](https://github.com/skaliak/fuzzy-happiness/blob/master/screenshots/7.png?raw=true)
